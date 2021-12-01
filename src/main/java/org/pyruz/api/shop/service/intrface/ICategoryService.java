package org.pyruz.api.shop.service.intrface;

import org.pyruz.api.shop.model.domain.CategoryNewRequest;
import org.pyruz.api.shop.model.domain.CategoryUpdateRequest;
import org.pyruz.api.shop.model.dto.BaseDTO;

public interface ICategoryService {

    BaseDTO findAllCategories();

    BaseDTO addNewCategory(CategoryNewRequest categoryNewRequest);

    BaseDTO updateCategory(CategoryUpdateRequest categoryUpdateRequest);

    BaseDTO deleteCategory(Integer id);
}
