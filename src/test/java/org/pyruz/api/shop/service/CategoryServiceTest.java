package org.pyruz.api.shop.service;

import org.junit.jupiter.api.Test;
import org.pyruz.api.shop.configuration.ApplicationProperties;
import org.pyruz.api.shop.exception.ServiceException;
import org.pyruz.api.shop.model.entity.Category;
import org.pyruz.api.shop.model.mapper.CategoryMapper;
import org.pyruz.api.shop.repository.CategoryRepository;
import org.pyruz.api.shop.security.JwtTokenProvider;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CategoryServiceTest {
    private final JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
    private final ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final CategoryMapper categoryMapper = mock(CategoryMapper.class);


    private final CategoryService service = new CategoryService(jwtTokenProvider, applicationProperties, categoryRepository, categoryMapper);

    @Test
    void findCategoryById() {
        Category category = new Category("laptop", new ArrayList<>());
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
        Category resultCategory = service.findCategoryById(anyInt());
        assertNotNull(resultCategory);

        when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ServiceException.class,()->service.findCategoryById(12));
    }

    @Test
    void isDuplicateCategory() {
        when(categoryRepository.findByTitle(anyString())).thenReturn(Optional.of(new Category()));
        assertTrue(service.isDuplicateCategory(anyString()));

        when(categoryRepository.findByTitle(anyString())).thenReturn(Optional.empty());
        assertFalse(service.isDuplicateCategory(anyString()));

    }
}
