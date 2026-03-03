package shop.campustable.campustablebeclone.domain.order.repository;

import static shop.campustable.campustablebeclone.domain.cafeteria.entity.QCafeteria.cafeteria;
import static shop.campustable.campustablebeclone.domain.order.entity.QOrder.order;
import static shop.campustable.campustablebeclone.domain.order.entity.QOrderItem.orderItem;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import shop.campustable.campustablebeclone.domain.order.entity.Order;
import shop.campustable.campustablebeclone.domain.order.dto.OrderSearchRequest;
import shop.campustable.campustablebeclone.domain.order.entity.OrderStatus;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Order> findByIdWithForceIncrement(Long orderId) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(order)
//            .leftJoin(order.orderItems, orderItem).fetchJoin()
            .where(order.id.eq(orderId))
            .setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
            .fetchOne()
    );
  }

//  @Override
//  public Page<Order> findOrdersWithCafeteriaByUserId(Long userId, OrderSearchRequest request, Pageable pageable) {
//
//    List<Order> content = queryFactory
//        .selectFrom(order)
//        .join(order.cafeteria, cafeteria).fetchJoin()
//        .where(
//            order.user.id.eq(userId),
//            dateBetween(request.getStartDate(), request.getEndDate()),
//            statusEq(request.getStatus()),
//            cafeteriaIdEq(request.getCafeteriaId())
//        )
//        .offset(pageable.getOffset())
//        .limit(pageable.getPageSize())
//        .orderBy(order.createdAt.desc())
//        .fetch();
//
//    long total = queryFactory
//        .select(order.count())
//        .from(order)
//        .where(
//            order.user.id.eq(userId),
//            dateBetween(request.getStartDate(), request.getEndDate()),
//            statusEq(request.getStatus()),
//            cafeteriaIdEq(request.getCafeteriaId())
//            )
//        .fetchOne();
//
//    return new PageImpl<>(content, pageable, total);
//  }

  @Override
  public Optional<Order> findByIdWithDetails(Long orderId) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(order)
            .distinct()
            .join(order.cafeteria, cafeteria).fetchJoin()
            .leftJoin(order.orderItems, orderItem).fetchJoin()
            .where(order.id.eq(orderId))
            .fetchOne()
    );
  }

  private BooleanExpression dateBetween(LocalDateTime start, LocalDateTime end) {
    if(start != null && end != null) {
      return order.createdAt.between(start, end);
    } else if (start != null) {
      return order.createdAt.goe(start);
    } else if (end != null) {
      return order.createdAt.loe(end);
    }
    return null;
  }

  private BooleanExpression statusEq(OrderStatus status) {
    return status != null ? order.status.eq(status) : null;
  }

  private BooleanExpression cafeteriaIdEq(Long cafeteriaId) {
    return cafeteriaId != null ? order.cafeteria.id.eq(cafeteriaId) : null;
  }

}
