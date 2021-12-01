package org.pyruz.api.shop.service;

import org.pyruz.api.shop.configuration.ApplicationProperties;
import org.pyruz.api.shop.exception.ServiceException;
import org.pyruz.api.shop.model.domain.CategoryNewRequest;
import org.pyruz.api.shop.model.domain.CategoryUpdateRequest;
import org.pyruz.api.shop.model.dto.BaseDTO;
import org.pyruz.api.shop.model.dto.CategoryDTO;
import org.pyruz.api.shop.model.dto.MetaDTO;
import org.pyruz.api.shop.model.entity.Category;
import org.pyruz.api.shop.model.mapper.CategoryMapper;
import org.pyruz.api.shop.repository.CategoryRepository;
import org.pyruz.api.shop.security.JwtTokenProvider;
import org.pyruz.api.shop.service.intrface.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService extends BaseService implements ICategoryService {

    final CategoryRepository categoryRepository;
    final CategoryMapper categoryMapper;

    protected CategoryService(JwtTokenProvider jwtTokenProvider, ApplicationProperties applicationProperties, CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        super(jwtTokenProvider, applicationProperties);
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Category findCategoryById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> ServiceException.builder()
                        .code(applicationProperties.getCode("application.message.notFoundRecord.code"))
                        .message(applicationProperties.getProperty("application.message.notFoundRecord.text"))
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build()
        );
    }

    public Category findCategoryByTitle(String title) {
        return categoryRepository.findByTitle(title).orElse(null);
    }

    public boolean isDuplicateCategory(String title) {
        return findCategoryByTitle(title) != null;
    }

    public BaseDTO findAllCategories() {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        categoryRepository.findAll().forEach(i -> categoryDTOS.add(categoryMapper.CATEGORY_DTO(i)));
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .data(categoryDTOS)
                .build();
    }

    public BaseDTO addNewCategory(CategoryNewRequest categoryNewRequest) {
        if (!isDuplicateCategory(categoryNewRequest.getTitle())) {
            categoryRepository.save(Category.builder().title(categoryNewRequest.getTitle()).build());
            return BaseDTO.builder()
                    .meta(MetaDTO.getInstance(applicationProperties))
                    .build();
        } else {
            throw ServiceException.builder()
                    .code(applicationProperties.getCode("application.message.duplicate.code"))
                    .message(applicationProperties.getProperty("application.message.duplicate.text"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public BaseDTO updateCategory(CategoryUpdateRequest categoryUpdateRequest) {
        Category category = findCategoryById(categoryUpdateRequest.getId());
        category.setTitle(categoryUpdateRequest.getTitle());
        categoryRepository.save(category);
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .build();
    }

    public BaseDTO deleteCategory(Integer id) {
        Category category = findCategoryById(id);
        category.setIsDeleted(true);
        categoryRepository.save(category);
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .build();
    }
}
