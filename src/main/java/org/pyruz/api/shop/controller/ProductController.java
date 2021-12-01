package org.pyruz.api.shop.controller;

import io.swagger.annotations.ApiParam;
import org.pyruz.api.shop.model.domain.ProductCommentRequest;
import org.pyruz.api.shop.model.domain.ProductNewRequest;
import org.pyruz.api.shop.model.domain.ProductRateRequest;
import org.pyruz.api.shop.model.domain.ProductUpdateRequest;
import org.pyruz.api.shop.model.dto.BaseDTO;
import org.pyruz.api.shop.service.intrface.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class ProductController extends BaseController {

    final IProductService iProductService;

    public ProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @GetMapping("v1/product/findByCategoryId")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseDTO findByCategoryId(@ApiParam(value = "1", name = "categoryId", required = true)
                                    @RequestParam Integer categoryId) {
        return iProductService.findProductsByCategoryId(categoryId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("v1/product")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public BaseDTO addNewProduct(@Valid @RequestBody ProductNewRequest productNewRequest) {
        return iProductService.addNewProduct(productNewRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("v1/product")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseDTO updateProduct(@Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
        return iProductService.updateProduct(productUpdateRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "v1/product")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseDTO deleteProduct(@ApiParam(value = "1", name = "id", required = true) @RequestParam Integer id) {
        return iProductService.deleteProduct(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("v1/product/rate")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseDTO rateProduct(@Valid @RequestBody ProductRateRequest productUpdateRequest) {
        return iProductService.rateOnProduct(productUpdateRequest);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("v1/product/comment")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseDTO commentOnProduct(@Valid @RequestBody ProductCommentRequest productCommentRequest) {
        return iProductService.commentOnProduct(productCommentRequest);
    }

    @GetMapping(value = "v1/product")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseDTO getProductById(@RequestParam Integer id) {
        return iProductService.findProduct(id);
    }

    @GetMapping(value = "v1/products")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseDTO getProducts() {
        return iProductService.findProducts();
    }

    @GetMapping(value = "v1/product/search")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseDTO searchProducts(@RequestParam(required = false, defaultValue = "") String title, @RequestParam(required = false, defaultValue = "0") Float minPrice,
                                  @RequestParam(required = false, defaultValue = "0") Float maxPrice, @RequestParam(required = false, defaultValue = "0") Short minRate,
                                  @RequestParam(required = false, defaultValue = "5") Short maxRate) {
        return iProductService.searchProducts(title, minPrice, maxPrice, minRate, maxRate);
    }

}
