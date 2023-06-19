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

    /**
     * 가계부 카테고리 생성
     * @param categoryNames 생성할 카테고리 이름 리스트
     * @param accountBook 카테고리를 생성할 가계부
     */
    @Transactional
    public List<String> createCategories(List<String> categoryNames, AccountBook accountBook) {
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
        return categoryNames;
    }

    /**
     * 가계부에 카테고리 한개 추가
     * @param accountBook 추가할 가계부
     * @param name 생성할 카테고리 이름
     */
    @Transactional
    public void addCategoryToAccountBook(AccountBook accountBook, String name) {
        Category newCategory = Category.builder()
                .accountBook(accountBook)
                .name(name)
                .build();
        categoryRepository.save(newCategory);
    }

    /**
     * 카테고리 업데이트
     * @param categoryId 업데이트 할 카테고리 아이디
     * @param map 업데이트할 정보 map
     * @return 업데이트 결과
     */
    @Transactional
    public CategoryUpdateRequest updateCategory(Long categoryId, Map<String, Object> map) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 카테고리가 없습니다"));
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

    /**
     * 카테고리 삭제
     * @param id 삭제할 카테고리 아이디
     */
    @Transactional
    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new NotFoundByIdException("해당 아이디로 존재하는 카테고리가 없습니다");
        }
    }
}

