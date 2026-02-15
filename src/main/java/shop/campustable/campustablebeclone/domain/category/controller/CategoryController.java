package shop.campustable.campustablebeclone.domain.category.controller;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.campustable.campustablebeclone.domain.category.dto.CategoryRequest;
import shop.campustable.campustablebeclone.domain.category.dto.CategoryResponse;
import shop.campustable.campustablebeclone.domain.category.service.CategoryService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerDocs{

  private final CategoryService categoryService;

  @PostMapping("/admin/cafeterias/{cafeteria-id}/categories")
  public ResponseEntity<CategoryResponse> createCategory(
      @PathVariable(name = "cafeteria-id") Long cafeteriaId,
      @RequestBody @Valid CategoryRequest request) {

    CategoryResponse response = categoryService.createCategory(cafeteriaId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);

  }

  @GetMapping("/categories")
  public ResponseEntity<List<CategoryResponse>> getAllCategories() {
    List<CategoryResponse> responses = categoryService.getAllCategories();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/cafeterias/{cafeteria-id}/categories")
  public ResponseEntity<List<CategoryResponse>> getCategoriesByCafeteriaId(@PathVariable(name = "cafeteria-id") Long cafeteriaId) {
    List<CategoryResponse> responses = categoryService.getCategoriesByCafeteriaId(cafeteriaId);
    return ResponseEntity.ok(responses);
  }

  @PatchMapping("/admin/categories/{category-id}")
  public ResponseEntity<CategoryResponse> updateCategory(@PathVariable(name = "category-id") Long categoryId, @RequestBody @Valid CategoryRequest request) {
    CategoryResponse response = categoryService.updateCategory(categoryId, request);
    return ResponseEntity.ok(response);
  }

}
