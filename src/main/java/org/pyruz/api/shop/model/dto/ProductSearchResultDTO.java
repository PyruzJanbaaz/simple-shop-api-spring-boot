package org.pyruz.api.shop.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductSearchResultDTO {
    private BigDecimal rate;
    private Float price;
    private String title;
    private String image;
}
