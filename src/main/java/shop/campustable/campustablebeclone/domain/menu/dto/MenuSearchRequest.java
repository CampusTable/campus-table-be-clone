package shop.campustable.campustablebeclone.domain.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuSearchRequest {

  private String menuName;
  private Long categoryId;

}
