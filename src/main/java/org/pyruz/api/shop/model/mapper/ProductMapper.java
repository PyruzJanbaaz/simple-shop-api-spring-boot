package org.pyruz.api.shop.model.mapper;

import org.mapstruct.Mapper;
import org.pyruz.api.shop.model.dto.ProductDTO;
import org.pyruz.api.shop.model.dto.ProductSimpleDTO;
import org.pyruz.api.shop.model.entity.Product;

@Mapper
public interface ProductMapper {
    ProductDTO PRODUCT_DTO(Product product);
    ProductSimpleDTO PRODUCT_SIMPLE_DTO(Product product);
}
