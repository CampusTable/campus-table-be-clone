package shop.campustable.campustablebeclone.domain.order.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;
import shop.campustable.campustablebeclone.domain.cafeteria.repository.CafeteriaRepository;
import shop.campustable.campustablebeclone.domain.cart.dto.CartResponse;
import shop.campustable.campustablebeclone.domain.cart.entity.Cart;
import shop.campustable.campustablebeclone.domain.cart.repository.CartRepository;
import shop.campustable.campustablebeclone.domain.category.repository.CategoryRepository;
import shop.campustable.campustablebeclone.domain.order.dto.OrderResponse;
import shop.campustable.campustablebeclone.domain.order.entity.Order;
import shop.campustable.campustablebeclone.domain.order.entity.OrderItem;
import shop.campustable.campustablebeclone.domain.order.entity.OrderStatus;
import shop.campustable.campustablebeclone.domain.order.repository.OrderItemRepository;
import shop.campustable.campustablebeclone.domain.order.repository.OrderRepository;
import shop.campustable.campustablebeclone.domain.user.entity.User;
import shop.campustable.campustablebeclone.domain.user.repository.UserRepository;
import shop.campustable.campustablebeclone.global.common.SecurityUtil;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final CartRepository cartRepository;
  private final CafeteriaRepository cafeteriaRepository;
  private final OrderItemRepository orderItemRepository;
  private final CategoryRepository categoryRepository;

  public OrderResponse createOrder() {
    Long userId = SecurityUtil.getCurrentUserId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("createOrder: 유효하지 않은 user {}", userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    Cart cart = cartRepository.findByUserWithItems(user)
        .orElseThrow(() -> {
          log.warn("createOrder: 유저 {}에게 cart가 존재하지 않습니다.", userId);
          return new CustomException(ErrorCode.CART_NOT_FOUND);
        });

    List<OrderItem> orderItems = cart.getCartItems().stream()
        .map(cartItem -> {

          cartItem.getMenu().decreseStockQuantity(cartItem.getQuantity());

          return OrderItem.builder()
              .menu(cartItem.getMenu())
              .quantity(cartItem.getQuantity())
              .build();
        })
        .toList();

    Cafeteria cafeteria = cafeteriaRepository.findById(cart.getCafeteriaId())
        .orElseThrow(() -> {
          log.warn("createOrder: 유효하지 않은 cafeteria {}", cart.getCafeteriaId());
          return new CustomException(ErrorCode.CAFETERIA_NOT_FOUND);
        });

    Order order = Order.builder()
        .user(user)
        .cafeteria(cafeteria)
        .orderItems(orderItems)
        .build();

    Order savedOrder = orderRepository.save(order);

    cart.clearCart();

    log.info("주문 생성 완료: 주문ID={}, 유저ID={}", savedOrder.getId(), userId);
    return OrderResponse.from(savedOrder);

  }

  public List<OrderResponse> getMyOrders() {
    Long userId = SecurityUtil.getCurrentUserId();

    List<Order> orders = orderRepository.findOrdersByUserId(userId);

    return orders.stream()
        .map(OrderResponse::from)
        .toList();
  }

  public void markCategoryAsReady(Long orderId, Long categoryId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> {
          log.warn("markCategoryAsReady: 주문이 존재하지 않습니다. {}", orderId);
          return new CustomException(ErrorCode.ORDER_NOT_FOUND);
        });
    if (!categoryRepository.existsById(categoryId)) {
      throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
    }

    List<OrderItem> targetItems = order.getOrderItems().stream()
        .filter(orderItem -> orderItem.getCategoryId().equals(categoryId))
        .toList();

    if (targetItems.isEmpty()) {
      throw new CustomException(ErrorCode.ORDER_ITEM_NOT_FOUND);
    }

    targetItems.forEach(OrderItem::markAsReady);

    boolean allReady = order.getOrderItems().stream()
        .allMatch(orderItem -> orderItem.getStatus() == OrderStatus.READY ||
                               orderItem.getStatus() == OrderStatus.COMPLETED);
    if (allReady) {
      order.markAsReady();
    }

  }

  public void markCategoryAsCompleted(Long orderId, Long categoryId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> {
          log.warn("markCategoryAsCompleted: 주문이 존재하지 않습니다. {}", orderId);
          return new CustomException(ErrorCode.ORDER_NOT_FOUND);
        });
    if (!categoryRepository.existsById(categoryId)) {
      throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
    }

    List<OrderItem> targetItems = order.getOrderItems().stream()
        .filter(orderItem -> orderItem.getCategoryId().equals(categoryId))
        .toList();

    if (targetItems.isEmpty()) {
      throw new CustomException(ErrorCode.ORDER_ITEM_NOT_FOUND);
    }

    targetItems.forEach(OrderItem::markAsCompleted);

    boolean allCompleted = order.getOrderItems().stream()
        .allMatch(orderItem -> orderItem.getStatus() == OrderStatus.COMPLETED);

    if (allCompleted) {
      order.markAsCompleted();
    }
  }

}

