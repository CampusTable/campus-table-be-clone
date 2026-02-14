package shop.campustable.campustablebeclone.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TopMenuResponse {

  private Long rank;
  private MenuResponse menu;

  public static TopMenuResponse of(Long rank, Menu menu,String fullUrl) {
    return TopMenuResponse.builder()
        .rank(rank)
        .menu(MenuResponse.from(menu,fullUrl))
        .build();
  }

}
