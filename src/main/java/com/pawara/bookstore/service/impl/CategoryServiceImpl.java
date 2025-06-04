package com.pawara.bookstore.service.impl;

import com.pawara.bookstore.dto.category.CategoryRequestDto;
import com.pawara.bookstore.dto.category.CategoryResponseDto;
import com.pawara.bookstore.entity.Category;
import com.pawara.bookstore.exception.DuplicateResourceException;
import com.pawara.bookstore.exception.ResourceNotFoundException;
import com.pawara.bookstore.repository.CategoryRepository;
import com.pawara.bookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        log.info("Creating category with name: {}", categoryRequestDto.getName());

        if (categoryRepository.existsByNameAndIsDeletedFalse(categoryRequestDto.getName())) {
            throw new DuplicateResourceException("Category already exists: " + categoryRequestDto.getName());
        }

        Category category = new Category();
        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());
        category.setIsDeleted(false);

        Category savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with ID: {}", savedCategory.getId());

        return mapToResponseDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(Long id) {
        log.info("Fetching category with ID: {}", id);
        Category category = categoryRepository.findActiveCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        return mapToResponseDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        log.info("Fetching all active categories");
        return categoryRepository.findAllActiveCategories()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        log.info("Updating category with ID: {}", id);

        Category existingCategory = categoryRepository.findActiveCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        // Check for duplicate name (excluding current category)
        if (!existingCategory.getName().equals(categoryRequestDto.getName()) &&
            categoryRepository.existsByNameAndIsDeletedFalse(categoryRequestDto.getName())) {
            throw new DuplicateResourceException("Category already exists: " + categoryRequestDto.getName());
        }

        existingCategory.setName(categoryRequestDto.getName());
        existingCategory.setDescription(categoryRequestDto.getDescription());

        Category updatedCategory = categoryRepository.save(existingCategory);
        log.info("Category updated successfully with ID: {}", updatedCategory.getId());

        return mapToResponseDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting category with ID: {}", id);

        Category category = categoryRepository.findActiveCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        category.setIsDeleted(true);
        categoryRepository.save(category);

        log.info("Category soft deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return categoryRepository.existsByNameAndIsDeletedFalse(name);
    }

    private CategoryResponseDto mapToResponseDto(Category category) {
        int bookCount = category.getBooks() != null ? 
                (int) category.getBooks().stream().filter(book -> !book.getIsDeleted()).count() : 0;
        
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                bookCount
        );
    }
}
