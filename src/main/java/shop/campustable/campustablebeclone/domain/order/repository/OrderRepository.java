package shop.campustable.campustablebeclone.domain.order.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.campustable.campustablebeclone.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {


  @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
  @Query("select o from Order o where o.id = :id")
  Optional<Order> findByIdWithForceIncrement(@Param("id") Long id);

  @Query("SELECT DISTINCT o FROM Order o " +
         "JOIN FETCH o.cafeteria " +
         "JOIN FETCH o.orderItems " +
         "WHERE o.user.id = :userId")
  List<Order> findOrdersWithItemsAndCafeteriaByUserId(@Param("userId") Long userId);

}
