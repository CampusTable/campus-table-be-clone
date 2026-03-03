package shop.campustable.campustablebeclone.domain.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuSearchRequest {

  private String menuName;
  private Long categoryId;

  @Schema(description = "정렬 조건", allowableValues = {"price_asc", "price_desc","newest"})
  private String sort;

}
