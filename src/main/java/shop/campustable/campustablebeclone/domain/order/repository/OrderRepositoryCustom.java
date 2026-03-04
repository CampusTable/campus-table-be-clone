package shop.campustable.campustablebeclone.domain.order.repository;

import java.util.Optional;
import shop.campustable.campustablebeclone.domain.order.entity.Order;

public interface OrderRepositoryCustom {

  Optional<Order> findByIdWithForceIncrement(Long id);

//  Page<Order> findOrdersWithCafeteriaByUserId(Long userId, OrderSearchRequest request,Pageable pageable);

  Optional<Order> findByIdWithDetails(Long orderId);

}
