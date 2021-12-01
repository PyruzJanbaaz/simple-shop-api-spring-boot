package org.pyruz.api.shop.model.mapper;

import org.mapstruct.Mapper;
import org.pyruz.api.shop.model.dto.CategoryDTO;
import org.pyruz.api.shop.model.entity.Category;

@Mapper
public interface CategoryMapper {
    CategoryDTO CATEGORY_DTO(Category category);
}
