package com.uliga.uliga_backend.domain.Category.api;

import com.uliga.uliga_backend.domain.Category.application.CategoryService;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO.CategoryUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "카테고리", description = "카테고리 관련 API 입니다")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "카테고리 삭제 API", description = "카테고리 삭제 API 입니다")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@Parameter(name = "id", description = "카테고리 아이디", in = ParameterIn.PATH) @PathVariable("id") Long id) {

        log.info("카테고리 삭제 api 호출");
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("DELETED");
    }

    @Operation(summary = "카테고리 업데이트 요청 API", description = "카테고리 업데이트 요청 API입니다")
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryUpdateRequest> updateCategory(@PathVariable("id") Long id, @RequestBody Map<String, Object> map) {

        log.info("카테고리 업데이트 api 호출");
        return ResponseEntity.ok(categoryService.updateCategory(id, map));
    }
}
