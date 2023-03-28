package com.uliga.uliga_backend.domain.Category.application;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;


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
    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new NotFoundByIdException();
        }
    }
}

