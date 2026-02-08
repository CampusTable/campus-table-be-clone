package shop.campustable.campustablebeclone.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;
import shop.campustable.campustablebeclone.domain.category.entity.Category;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

  @NotBlank(message = "카테고리 이름은 필수입니다.")
  private String name;

  public Category toEntity(Cafeteria cafeteria) {
    return Category.builder()
        .cafeteria(cafeteria)
        .name(this.name)
        .build();
  }

}
