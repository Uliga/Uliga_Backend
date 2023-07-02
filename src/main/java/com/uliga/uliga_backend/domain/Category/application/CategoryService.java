package com.uliga.uliga_backend.domain.Category.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.repository.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.exception.UnauthorizedAccountBookCategoryCreateException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.repository.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO.CategoryCreateRequest;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO.CategoryCreateResult;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO.CategoryUpdateRequest;
import com.uliga.uliga_backend.domain.Category.exception.DuplicateCategoryException;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.JoinTable.repository.AccountBookMemberRepository;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final AccountBookRepository accountBookRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper mapper;
    private final AccountBookMemberRepository accountBookMemberRepository;

    private final List<String> defaultCategories = new ArrayList<>(
            Arrays.asList("\uD83C\uDF7D️ 식비",
                    "☕ 카페 · 간식",
                    "\uD83C\uDFE0 생활",
                    "\uD83C\uDF59 편의점,마트,잡화",
                    "\uD83D\uDC55 쇼핑",
                    "기타")
    );

    @Transactional
    public void createDefaultCategories(AccountBook accountBook) {
        List<Category> categories = new ArrayList<>();
        for (String defaultCategory : defaultCategories) {
            Category newCategory = Category.builder()
                    .accountBook(accountBook)
                    .name(defaultCategory)
                    .build();
            categories.add(newCategory);
        }
        categoryRepository.saveAll(categories);
    }


    /**
     * 카테고리 생성
     *
     * @param currentMemberId       현재 멤버 아이디
     * @param categoryCreateRequest 카테고리 생성 요청
     * @return 카테고리 생성 결과
     */
    @Transactional
    public CategoryCreateResult createCategories(Long currentMemberId, CategoryCreateRequest categoryCreateRequest) {
        Long accountBookId = categoryCreateRequest.getId();
        AccountBook accountBook = accountBookRepository.findById(accountBookId).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
        if (!accountBookMemberRepository.existsAccountBookMemberByMemberIdAndAccountBookId(currentMemberId, accountBookId)) {
            throw new UnauthorizedAccountBookCategoryCreateException();
        }

        HashSet<String> categoryNamesByAccountBookId = categoryRepository.findCategoryNamesByAccountBookId(accountBook.getId());

        List<Category> categories = new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (String category : categoryCreateRequest.getCategories()) {
            if (!categoryNamesByAccountBookId.contains(category)) {
                Category newCategory = Category.builder()
                        .accountBook(accountBook)
                        .name(category)
                        .build();
                categories.add(newCategory);
                result.add(category);
                categoryNamesByAccountBookId.add(category);
            }
        }
        categoryRepository.saveAll(categories);

        return CategoryCreateResult.builder().id(accountBook.getId()).created(result).build();
    }

    /**
     * 가계부 카테고리 조회
     *
     * @param accountBookId 가계부 아이디
     * @return 조회 결과
     */
    @Transactional(readOnly = true)
    public CategoryDTO.AccountBookCategories getAccountBookCategories(Long accountBookId) {
        return new CategoryDTO.AccountBookCategories(categoryRepository.findAccountBookCategoryInfoById(accountBookId));
    }

    @Transactional
    public void updateAccountBookCategory(Long accountBookId, List<String> categories) {
        HashSet<String> categoryNamesByAccountBookId = categoryRepository.findCategoryNamesByAccountBookId(accountBookId);
        AccountBook accountBook = accountBookRepository.findById(accountBookId).orElseThrow(NotFoundByIdException::new);
        HashSet<String> finalCategory = new HashSet<>();
        List<Category> createCategories = new ArrayList<>();
        for (String category : categories) {
            finalCategory.add(category);
            if (!categoryNamesByAccountBookId.contains(category)) {
                Category newCategory = Category.builder()
                        .accountBook(accountBook)
                        .name(category)
                        .build();
                createCategories.add(newCategory);
                categoryNamesByAccountBookId.add(category);
            }
        }
        for (String category : categoryNamesByAccountBookId) {
            if (!finalCategory.contains(category)) {
                categoryRepository.deleteByNameAndAccountBookId(category, accountBookId);
            }
        }
        categoryRepository.saveAll(createCategories);
    }

    /**
     * 카테고리 업데이트
     *
     * @param categoryId 업데이트 할 카테고리 아이디
     * @param map        업데이트할 정보 map
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
     *
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

