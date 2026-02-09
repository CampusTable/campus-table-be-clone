package shop.campustable.campustablebeclone.domain.menu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.category.entity.Category;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuRequest;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuResponse;
import shop.campustable.campustablebeclone.global.common.BaseTimeEntity;

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
  @JoinColumn(name = "category_id",nullable = false)
  private Category category;

  @Column(nullable = false)
  private String menuName;

  @Column(nullable = false)
  private Integer price;

  @Column(length = 500)
  private String menuUrl;

  @Column(nullable = false)
  private Boolean available;

  @Column(nullable = false)
  private Integer stockQuantity;

  public void update(MenuRequest request) {
    if(request.getMenuName() != null && !request.getMenuName().isBlank()) {
      this.menuName = request.getMenuName();
    }
    if(request.getPrice() != null && request.getPrice() > 0) {
      this.price = request.getPrice();
    }
    if(request.getStockQuantity() != null &&  request.getStockQuantity() >=0) {
      this.stockQuantity = request.getStockQuantity();
    }
    if(this.getStockQuantity() == 0 ) {
      this.available = false;
    }
    else if(request.getAvailable() != null){
      this.available = request.getAvailable();
    }
  }

}
