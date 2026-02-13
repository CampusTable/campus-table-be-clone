package shop.campustable.campustablebeclone.domain.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Entity
@Getter
@NoArgsConstructor
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_id")
  private Menu menu;

  private String menuName;
  private int price;
  private int quantity;

  private Long categoryId;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  public void setOrder(Order order){
    this.order = order;
  }

  @Builder
  public OrderItem(Menu menu, int quantity) {
    this.menu = menu;
    this.menuName = menu.getMenuName();
    this.price = menu.getPrice();
    this.quantity = quantity;
    this.status = OrderStatus.PREPARING;
    this.categoryId = menu.getCategory().getId();
  }

  public int getSubtotal(){
    return price * quantity;
  }

  public void markAsReady(){

    if(status == OrderStatus.READY){
      return;
    }

    if(this.status != OrderStatus.PREPARING){
      throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
    }
    this.status=OrderStatus.READY;
  }

  public void markAsCompleted(){

    if(status == OrderStatus.COMPLETED){
      return;
    }

    if(this.status != OrderStatus.READY){
      throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
    }
    this.status=OrderStatus.COMPLETED;
  }

}
