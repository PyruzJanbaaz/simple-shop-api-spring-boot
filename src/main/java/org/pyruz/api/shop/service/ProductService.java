package org.pyruz.api.shop.service;


import org.pyruz.api.shop.configuration.ApplicationProperties;
import org.pyruz.api.shop.exception.ServiceException;
import org.pyruz.api.shop.model.domain.ProductCommentRequest;
import org.pyruz.api.shop.model.domain.ProductNewRequest;
import org.pyruz.api.shop.model.domain.ProductRateRequest;
import org.pyruz.api.shop.model.domain.ProductUpdateRequest;
import org.pyruz.api.shop.model.dto.*;
import org.pyruz.api.shop.model.entity.*;
import org.pyruz.api.shop.model.mapper.ProductMapper;
import org.pyruz.api.shop.repository.CategoryRepository;
import org.pyruz.api.shop.repository.CommentRepository;
import org.pyruz.api.shop.repository.ProductRepository;
import org.pyruz.api.shop.repository.RateRepository;
import org.pyruz.api.shop.security.JwtTokenProvider;
import org.pyruz.api.shop.service.intrface.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ProductService extends BaseService implements IProductService {

    final ProductRepository productRepository;
    final CategoryRepository categoryRepository;
    final CategoryService categoryService;
    final RateRepository rateRepository;
    final CommentRepository commentRepository;
    final UserService userService;
    final ProductMapper productMapper;

    static final String[] images = {"computer-img.png", "laptop-img.png", "mobile-img.png"};

    protected ProductService(JwtTokenProvider jwtTokenProvider, ApplicationProperties applicationProperties, ProductRepository productRepository, CategoryRepository categoryRepository, CategoryService categoryService, RateRepository rateRepository, CommentRepository commentRepository, UserService userService, ProductMapper productMapper) {
        super(jwtTokenProvider, applicationProperties);
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.rateRepository = rateRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.productMapper = productMapper;
    }

    public BaseDTO findProduct(Integer id) {
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .data(productMapper.PRODUCT_DTO(findProductById(id)))
                .build();
    }

    @Override
    public BaseDTO findProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductSimpleDTO> productDTOS = new ArrayList<>();
        products.forEach(i -> productDTOS.add(productMapper.PRODUCT_SIMPLE_DTO(i)));
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .data(productDTOS)
                .build();
    }

    private Product findProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(
                () -> ServiceException.builder()
                        .code(applicationProperties.getCode("application.message.notFoundRecord.code"))
                        .message(applicationProperties.getProperty("application.message.notFoundRecord.text"))
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build()
        );
    }

    public Product findProductByTitle(String title) {
        return productRepository.findByTitle(title).orElse(null);
    }

    public boolean isDuplicateProduct(String title) {
        return findProductByTitle(title) != null;
    }

    public BaseDTO findProductsByCategoryId(Integer categoryId) {
        List<Product> products = productRepository.findProductByCategoryId(categoryId);
        List<ProductSimpleDTO> productSimpleDTOS = new ArrayList<>();
        products.forEach(i-> productSimpleDTOS.add(productMapper.PRODUCT_SIMPLE_DTO(i)));
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .data(productSimpleDTOS)
                .build();
    }

    public BaseDTO searchProducts(String title, Float minPrice, Float maxPrice, Short minRate, Short maxRate) {
        List<ProductSearchResultDTO> productSearchResultDTOS = new ArrayList<>();
        List<Object[]> searchResult = productRepository.searchProducts(title.toLowerCase(), minPrice, maxPrice, minRate, maxRate);
        for (Object[] item : searchResult) {
            productSearchResultDTOS.add(
                    ProductSearchResultDTO.builder()
                            .rate((BigDecimal) item[2])
                            .price((Float) item[0])
                            .title((String) item[1])
                            .image((String) item[3])
                            .build()
            );
        }
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .data(productSearchResultDTOS)
                .build();
    }

    @Transactional
    public BaseDTO addNewProduct(ProductNewRequest productNewRequest) {
        if (!(isDuplicateProduct(productNewRequest.getTitle()))) {
            Category category = categoryService.findCategoryById(productNewRequest.getCategoryId());
            Product product = productRepository.save(
                    Product.builder()
                            .category(category)
                            .price(productNewRequest.getPrice())
                            .title(productNewRequest.getTitle())
                            .image(images[new Random().nextInt(3)])
                            .build()
            );
            rateRepository.save(
                    Rate.builder()
                            .product(product)
                            .user(userService.findUserByUsername(getUsername()))
                            .rate(0F)
                            .build()
            );
            return BaseDTO.builder()
                    .meta(MetaDTO.getInstance(applicationProperties))
                    .build();
        } else {
            throw ServiceException.builder()
                    .code(applicationProperties.getCode("application.message.duplicate.code"))
                    .message(applicationProperties.getProperty("application.message.duplicate.text"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public BaseDTO updateProduct(ProductUpdateRequest productUpdateRequest) {
        Product product = findProductById(productUpdateRequest.getId());
        product.setPrice(productUpdateRequest.getPrice());
        product.setTitle(productUpdateRequest.getTitle());
        productRepository.save(product);
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .build();
    }

    @Override
    public BaseDTO deleteProduct(Integer id) {
        Product product = findProductById(id);
        product.setIsDeleted(true);
        productRepository.save(product);
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .build();
    }

    public BaseDTO rateOnProduct(ProductRateRequest productRateRequest) {
        User user = userService.findUserByUsername(getUsername());
        Product product = findProductById(productRateRequest.getProductId());
        Rate rate = rateRepository.findByProductIdAndUserId(productRateRequest.getProductId(), user.getId());
        if (rate != null) {
            throw ServiceException.builder()
                    .code(applicationProperties.getCode("application.message.has.rated.code"))
                    .message(applicationProperties.getProperty("application.message.has.rated.text"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        rateRepository.save(
                Rate.builder()
                        .user(user)
                        .product(product)
                        .rate(productRateRequest.getRate())
                        .build()
        );
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .build();
    }

    public BaseDTO commentOnProduct(ProductCommentRequest productCommentRequest) {
        User user = userService.findUserByUsername(getUsername());
        Product product = findProductById(productCommentRequest.getProductId());
        commentRepository.save(
                Comment.builder()
                        .user(user)
                        .product(product)
                        .comment(productCommentRequest.getComment())
                        .build()
        );
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .build();
    }


}
