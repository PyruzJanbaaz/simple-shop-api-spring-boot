package org.pyruz.api.shop.service;

import org.junit.jupiter.api.Test;
import org.pyruz.api.shop.configuration.ApplicationProperties;
import org.pyruz.api.shop.model.dto.BaseDTO;
import org.pyruz.api.shop.model.entity.Product;
import org.pyruz.api.shop.model.mapper.ProductMapper;
import org.pyruz.api.shop.repository.CategoryRepository;
import org.pyruz.api.shop.repository.CommentRepository;
import org.pyruz.api.shop.repository.ProductRepository;
import org.pyruz.api.shop.repository.RateRepository;
import org.pyruz.api.shop.security.JwtTokenProvider;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private final JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
    private final ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final CategoryService categoryService = mock(CategoryService.class);
    private final RateRepository rateRepository = mock(RateRepository.class);
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final UserService userService = mock(UserService.class);
    private final ProductMapper productMapper = mock(ProductMapper.class);

    private final ProductService productService = new ProductService(jwtTokenProvider, applicationProperties,
            productRepository, categoryRepository, categoryService,
            rateRepository, commentRepository, userService, productMapper);


    @Test
    void findProductByTitle() {
        when(productRepository.findByTitle(anyString())).thenReturn(Optional.of(new Product()));
        Product product = productService.findProductByTitle(anyString());
        assertNotNull(product);
    }

    @Test
    void searchProducts() {
        short minRate = 0, maxRate = 5;
        BaseDTO result = productService.searchProducts("", 0.0f, 1000.0f, minRate, maxRate);
        assertNotNull(result.getData());
        List<Product> products = null;
        assertDoesNotThrow(()->(List<Product>)result.getData());
        products = (List<Product>)result.getData();
        assertNotNull(products);
    }
}
