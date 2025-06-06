package com.uliga.uliga_backend.category.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uliga.uliga_backend.domain.category.application.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "카테고리", description = "카테고리 관련 API 입니다")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/category")
public class CategoryControllerV1 {
  private final CategoryService categoryService;

  // @Operation(summary = "카테고리 삭제 API", description = "카테고리 삭제 API 입니다")
  // @DeleteMapping("/{id}")

  // public ResponseEntity<String> deleteCategory(
  // @Parameter(name = "id", description = "카테고리 아이디", in = ParameterIn.PATH)
  // @PathVariable("id") Long id) {

  // categoryService.deleteCategory(id);
  // return ResponseEntity.ok("DELETED");
  // }

  // @Operation(summary = "카테고리 업데이트 요청 API", description = "카테고리 업데이트 요청 API입니다")
  // @PatchMapping("/{id}")

  // public ResponseEntity<CategoryUpdateRequest>
  // updateCategory(@PathVariable("id") Long id,
  // @RequestBody Map<String, Object> map) {

  // return ResponseEntity.ok(categoryService.updateCategory(id, map));
  // }
}
