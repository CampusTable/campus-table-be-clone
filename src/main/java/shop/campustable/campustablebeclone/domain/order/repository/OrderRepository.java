package shop.campustable.campustablebeclone.domain.order.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.campustable.campustablebeclone.domain.order.dto.OrderSearchRequest;
import shop.campustable.campustablebeclone.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>,OrderRepositoryCustom{

  @Query("""
          SELECT DISTINCT o
          FROM Order o
          JOIN FETCH o.cafeteria c
          WHERE (o.user.id = :userId)
          AND (:#{#request.startDate} IS NULL
            OR o.createdAt >= :#{#request.startDate})
          AND (:#{#request.endDate} IS NULL
            OR o.createdAt <= :#{#request.endDate}
          )
          AND (:#{#request.status} IS NULL
            OR o.status = :#{#request.status}
          )
          AND (:#{#request.cafeteriaId} IS NULL
            OR c.id = :#{#request.cafeteriaId}
          )
      """
  )
  Page<Order> findOrdersWithCafeteriaByUserId(
      @Param("userId") Long userId,
      @Param("request") OrderSearchRequest request,
      Pageable pageable
  );

}
