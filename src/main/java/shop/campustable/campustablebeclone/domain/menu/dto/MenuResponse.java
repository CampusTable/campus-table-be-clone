package shop.campustable.campustablebeclone.domain.menu.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MenuResponse {

  private Long menuId;
  private Long  categoryId;
  private String menuName;
  private Integer price;
  private String menuUrl;
  private Boolean available;
  private Integer stockQuantity;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static MenuResponse from(Menu menu) {

    String s3Domain = "https://campus-table-s3.s3.ap-northeast-2.amazonaws.com/";
    String fullUrl = (menu.getMenuUrl() != null) ? s3Domain + menu.getMenuUrl() : null;

    return MenuResponse.builder()
        .menuId(menu.getId())
        .categoryId(menu.getCategory().getId())
        .menuName(menu.getMenuName())
        .price(menu.getPrice())
        .menuUrl(menu.getMenuUrl())
        .available(menu.getAvailable())
        .stockQuantity(menu.getStockQuantity())
        .createdAt(menu.getCreatedAt())
        .updatedAt(menu.getUpdatedAt())
        .build();
  }

}
