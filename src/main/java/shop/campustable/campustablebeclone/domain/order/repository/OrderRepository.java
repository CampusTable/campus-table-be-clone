package shop.campustable.campustablebeclone.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.campustable.campustablebeclone.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
