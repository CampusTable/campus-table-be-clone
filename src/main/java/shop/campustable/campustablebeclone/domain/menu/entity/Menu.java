package shop.campustable.campustablebeclone.domain.menu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.campustable.campustablebeclone.domain.category.entity.Category;
import shop.campustable.campustablebeclone.global.common.BaseTimeEntity;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "menu_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Column(nullable = false)
  private String menuName;

  @Column(nullable = false)
  private int price;

  @Column(length = 500)
  @Setter
  private String menuUrl;

  @Column(nullable = false)
  private Boolean available;

  @Column(nullable = false)
  private int stockQuantity;

  public void update(String menuName, Integer price, Integer stockQuantity, Boolean available) {
    if (menuName != null && !menuName.isBlank()) {
      this.menuName = menuName;
    }
    if (price != null && price > 0) {
      this.price = price;
    }
    if (stockQuantity != null && stockQuantity >= 0) {
      this.stockQuantity = stockQuantity;
    }
    if (this.getStockQuantity() == 0) {
      this.available = false;
    } else if (available != null) {
      this.available = available;
    }
  }

  public void decreaseStockQuantity(int quantity) {
    int restStock = this.stockQuantity - quantity;
    if (restStock < 0) {
      throw new CustomException(ErrorCode.MENU_OUT_OF_STOCK);
    }
    this.stockQuantity = restStock;
    if (this.stockQuantity == 0) {
      this.available = false;
    }
  }

}
