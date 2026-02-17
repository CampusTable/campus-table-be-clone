package shop.campustable.campustablebeclone.domain.order.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;
import shop.campustable.campustablebeclone.domain.user.entity.User;
import shop.campustable.campustablebeclone.global.common.BaseTimeEntity;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor
public class Order extends BaseTimeEntity {

  @Id
  @Column(name = "order_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafeteria_id")
  private Cafeteria cafeteria;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @BatchSize(size = 100)
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> orderItems = new ArrayList<>();

  private int totalPrice;

  @Version
  private Long version;

  @Builder
  public Order(User user, Cafeteria cafeteria, List<OrderItem> orderItems) {
    this.user = user;
    this.cafeteria = cafeteria;
    this.status = OrderStatus.PREPARING;
    if (orderItems != null) {
      orderItems.forEach(this::addOrderItem);
    }
    this.totalPrice = calculateTotalPrice();
  }

  public void addOrderItem(OrderItem orderItem) {
    this.orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  private int calculateTotalPrice() {
    return orderItems.stream()
        .mapToInt(OrderItem::getSubtotal)
        .sum();
  }

  public boolean canMarkAsReady() {
    return this.orderItems.stream()
        .allMatch(orderItem ->
            orderItem.getStatus() == OrderStatus.READY ||
            orderItem.getStatus() == OrderStatus.COMPLETED);
  }

  public boolean canMarkAsComplete() {
    return this.orderItems.stream()
        .allMatch(orderItem ->
            orderItem.getStatus() == OrderStatus.COMPLETED);
  }

  public void markAsReady() {

    if (status == OrderStatus.READY) {
      return;
    }

    if(!canMarkAsReady()) {
      throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
    }

    if (this.status != OrderStatus.PREPARING) {
      throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
    }

    this.status = OrderStatus.READY;
  }

  public void markAsCompleted() {

    if (status == OrderStatus.COMPLETED) {
      return;
    }

    if(!canMarkAsComplete()) {
      throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
    }

    if (this.status != OrderStatus.READY) {
      throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
    }

    this.status = OrderStatus.COMPLETED;
  }

}
