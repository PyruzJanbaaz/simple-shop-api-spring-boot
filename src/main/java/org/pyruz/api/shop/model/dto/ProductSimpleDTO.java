package org.pyruz.api.shop.model.dto;

import lombok.Data;

@Data
public class ProductSimpleDTO extends BaseEntityDTO<Integer> {
    private String title;
    private Float price;
    private CategoryDTO category;
}
