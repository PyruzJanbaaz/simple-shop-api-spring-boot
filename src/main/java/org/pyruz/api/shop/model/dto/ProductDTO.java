package org.pyruz.api.shop.model.dto;

import lombok.*;
import java.util.List;

@Data
public class ProductDTO extends BaseEntityDTO<Integer> {
    private String title;
    private String image;
    private Float price;
    private CategoryDTO category;
    private List<CommentDTO> comments;
    private List<RateDTO> rates;
}
