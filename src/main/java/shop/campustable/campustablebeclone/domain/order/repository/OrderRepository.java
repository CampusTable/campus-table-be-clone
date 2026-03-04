package shop.campustable.campustablebeclone.domain.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.campustable.campustablebeclone.domain.order.dto.OrderSearchRequest;
import shop.campustable.campustablebeclone.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

  @Query(value = """
          SELECT DISTINCT o
          FROM Order o
          JOIN FETCH o.cafeteria c
          WHERE (o.user.id = :userId)
          AND o.createdAt >= COALESCE(:#{#request.startDate}, o.createdAt)
          AND o.createdAt <= COALESCE(:#{#request.endDate}, o.createdAt)
          AND o.status = COALESCE(:#{#request.status}, o.status)
          AND c.id = COALESCE(:#{#request.cafeteriaId}, c.id)
      """,
      countQuery = """
              SELECT COUNT(DISTINCT o)
              FROM Order o
              JOIN o.cafeteria c
              WHERE (o.user.id = :userId)
              AND o.createdAt >= COALESCE(:#{#request.startDate}, o.createdAt)
              AND o.createdAt <= COALESCE(:#{#request.endDate}, o.createdAt)
              AND o.status = COALESCE(:#{#request.status}, o.status)
              AND c.id = COALESCE(:#{#request.cafeteriaId}, c.id)
          """
  )
  Page<Order> findOrdersWithCafeteriaByUserId(
      @Param("userId") Long userId,
      @Param("request") OrderSearchRequest request,
      Pageable pageable
  );
}


