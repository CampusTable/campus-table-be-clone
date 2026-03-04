package shop.campustable.campustablebeclone.domain.menu.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuSortType {

  PRICE_ASC("가격 낮은 순"),
  PRICE_DESC("가격 높은 순"),
  NEWEST("최신 순"),
  OLDEST("오래된 순");

  private final String description;

}
