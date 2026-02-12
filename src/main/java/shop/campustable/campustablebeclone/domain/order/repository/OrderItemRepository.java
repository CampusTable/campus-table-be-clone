package shop.campustable.campustablebeclone.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.campustable.campustablebeclone.domain.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
