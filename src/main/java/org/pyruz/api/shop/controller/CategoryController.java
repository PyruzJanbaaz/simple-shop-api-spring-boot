package org.pyruz.api.shop.controller;

import io.swagger.annotations.ApiParam;
import org.pyruz.api.shop.model.domain.CategoryNewRequest;
import org.pyruz.api.shop.model.domain.CategoryUpdateRequest;
import org.pyruz.api.shop.model.dto.BaseDTO;
import org.pyruz.api.shop.service.intrface.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class CategoryController extends BaseController{

    final ICategoryService iCategoryService;

    public CategoryController(ICategoryService iCategoryService) {
        this.iCategoryService = iCategoryService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "v1/category")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public BaseDTO addNewCategory(@Valid @RequestBody CategoryNewRequest categoryNewRequest) {
        return iCategoryService.addNewCategory(categoryNewRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "v1/category")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseDTO updateCategory(@Valid @RequestBody CategoryUpdateRequest categoryUpdateRequest) {
        return iCategoryService.updateCategory(categoryUpdateRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "v1/category")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseDTO deleteCategory(@ApiParam(value = "1", name = "id", required = true) @RequestParam Integer id) {
        return iCategoryService.deleteCategory(id);
    }

    @GetMapping(value = "v1/category")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseDTO getCategories() {
        return iCategoryService.findAllCategories();
    }
}
