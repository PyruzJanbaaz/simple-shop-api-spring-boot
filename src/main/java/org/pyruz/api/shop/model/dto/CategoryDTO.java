package org.pyruz.api.shop.model.dto;

import lombok.Data;
import org.pyruz.api.shop.model.entity.BaseEntity;

@Data
public class CategoryDTO extends BaseEntityDTO<Integer> {
    private String title;
}
