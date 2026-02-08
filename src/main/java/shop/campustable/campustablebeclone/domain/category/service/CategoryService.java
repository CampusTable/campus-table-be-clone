package shop.campustable.campustablebeclone.domain.category.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;
import shop.campustable.campustablebeclone.domain.cafeteria.repository.CafeteriaRepository;
import shop.campustable.campustablebeclone.domain.category.entity.Category;
import shop.campustable.campustablebeclone.domain.category.dto.CategoryRequest;
import shop.campustable.campustablebeclone.domain.category.dto.CategoryResponse;
import shop.campustable.campustablebeclone.domain.category.repository.CategoryRepository;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CafeteriaRepository cafeteriaRepository;


  public CategoryResponse createCategory(Long cafeteriaId,CategoryRequest request) {
    Cafeteria cafeteria = cafeteriaRepository.findById(cafeteriaId)
        .orElseThrow(()->{
          log.error("createCategory: 유효하지 않은 cafeteriaId {}", cafeteriaId);
          return new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
        });

    categoryRepository.findByCafeteriaAndName(cafeteria,request.getName())
        .ifPresent(category -> {
          log.warn("createCategory: 해당 식당에 Category가 존재합니다. 생성이 아닌 수정을 통해 진행해주세요.");
          throw new CustomException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        });

    Category category = request.toEntity(cafeteria);
    categoryRepository.save(category);
    return CategoryResponse.from(category);
  }

  public List<CategoryResponse> getAllCategories(){
    List<Category> categories = categoryRepository.findAll();
    return categories.stream()
        .map(CategoryResponse::from)
        .toList();
  }

  public List<CategoryResponse> getCategoriesByCafeteriaId(Long cafeteriaId){
    Cafeteria cafeteria = cafeteriaRepository.findById(cafeteriaId)
        .orElseThrow(()->{
          log.error("getCategoriesByCafeteriaId: 유효하지 않은 cafeteriaId {}", cafeteriaId);
          return new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
        });

    List<Category> categories = categoryRepository.findByCafeteria(cafeteria);

    return categories.stream()
        .map(CategoryResponse::from)
        .toList();
  }

  public CategoryResponse updateCategory(Long id, CategoryRequest request) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(()->{
          log.error("updateCategory: 유효하지 않은 categoryId {}", id);
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });

    categoryRepository.findByCafeteriaAndName(category.getCafeteria(),request.getName())
            .ifPresent(existingCafeteria -> {
              log.warn("이미 존재하는 category입니다.");
              throw new CustomException(ErrorCode.CATEGORY_ALREADY_EXISTS);
            });

    category.update(request.getName());
    return  CategoryResponse.from(category);
  }

}
