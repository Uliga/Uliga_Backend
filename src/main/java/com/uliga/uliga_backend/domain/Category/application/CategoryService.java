package com.uliga.uliga_backend.domain.Category.application;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public void createCategories(List<String> categoryNames, AccountBook accountBook) {

        List<Category> categories = new ArrayList<>();
        for (String category : categoryNames){
            Category newCategory = Category.builder()
                    .accountBook(accountBook)
                    .name(category)
                    .build();
            categories.add(newCategory);
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
}

