package org.pyruz.api.shop.service.intrface;

import org.pyruz.api.shop.model.domain.ProductCommentRequest;
import org.pyruz.api.shop.model.domain.ProductNewRequest;
import org.pyruz.api.shop.model.domain.ProductRateRequest;
import org.pyruz.api.shop.model.domain.ProductUpdateRequest;
import org.pyruz.api.shop.model.dto.BaseDTO;

public interface IProductService {

    BaseDTO findProduct(Integer id);

    BaseDTO findProducts();

    BaseDTO findProductsByCategoryId(Integer categoryId);

    BaseDTO addNewProduct(ProductNewRequest productNewRequest);

    BaseDTO updateProduct(ProductUpdateRequest productUpdateRequest);

    BaseDTO deleteProduct(Integer id);

    BaseDTO rateOnProduct(ProductRateRequest productRateRequest);

    BaseDTO commentOnProduct(ProductCommentRequest productCommentRequest);

    BaseDTO searchProducts(String title, Float minPrice, Float maxPrice, Short minRate, Short maxRate);

}
