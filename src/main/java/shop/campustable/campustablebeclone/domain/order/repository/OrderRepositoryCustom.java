package shop.campustable.campustablebeclone.domain.order.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.campustable.campustablebeclone.domain.order.entity.Order;

public interface OrderRepositoryCustom {

  Optional<Order> findByIdWithForceIncrement(Long id);

  Page<Order> findOrdersWithCafeteriaByUserId(Long userId, Pageable pageable);

  Optional<Order> findByIdWithDetails(Long userId);

}
