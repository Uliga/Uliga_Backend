package com.uliga.uliga_backend.domain.Category.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO.CategoryUpdateRequest;
import com.uliga.uliga_backend.domain.Category.exception.DuplicateCategoryException;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ObjectMapper mapper;


    @Transactional
    public void createCategories(List<String> categoryNames, AccountBook accountBook) {
        Set<String> createCategories = new HashSet<>();
        List<Category> categories = new ArrayList<>();
        for (String category : categoryNames){
            if (!createCategories.contains(category)) {
                Category newCategory = Category.builder()
                        .accountBook(accountBook)
                        .name(category)
                        .build();
                categories.add(newCategory);
                createCategories.add(category);
            }
        }
        categoryRepository.saveAll(categories);
    }

    @Transactional
    public void addCategoryToAccountBook(AccountBook accountBook, String name) {
        Category newCategory = Category.builder()
                .accountBook(accountBook)
                .name(name)
                .build();
        categoryRepository.save(newCategory);
    }

    @Transactional
    public CategoryUpdateRequest updateCategory(Long categoryId, Map<String, Object> map) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(NotFoundByIdException::new);
        AccountBook accountBook = category.getAccountBook();
        CategoryUpdateRequest categoryUpdateRequest = mapper.convertValue(map, CategoryUpdateRequest.class);
        if (categoryUpdateRequest.getName() != null) {
            if (categoryRepository.existsByAccountBookIdAndName(accountBook.getId(), categoryUpdateRequest.getName())) {
                throw new DuplicateCategoryException();
            } else {
                category.setName(category.getName());
            }
        }
        return null;
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new NotFoundByIdException();
        }
    }
}

