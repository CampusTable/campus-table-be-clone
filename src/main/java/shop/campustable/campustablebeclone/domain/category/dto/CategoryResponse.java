package shop.campustable.campustablebeclone.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.category.entity.Category;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

  private Long id;
  private String name;
  private Long cafeteriaId;

  public static CategoryResponse from(Category category) {
    return CategoryResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .cafeteriaId(category.getCafeteria().getId())
        .build();
  }

}
