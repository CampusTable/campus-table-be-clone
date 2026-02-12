package shop.campustable.campustablebeclone.domain.order.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;
import shop.campustable.campustablebeclone.domain.cafeteria.repository.CafeteriaRepository;
import shop.campustable.campustablebeclone.domain.cart.entity.Cart;
import shop.campustable.campustablebeclone.domain.cart.repository.CartRepository;
import shop.campustable.campustablebeclone.domain.order.dto.OrderResponse;
import shop.campustable.campustablebeclone.domain.order.entity.Order;
import shop.campustable.campustablebeclone.domain.order.entity.OrderItem;
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
        .map(cartItem -> OrderItem.builder()
            .menu(cartItem.getMenu())
            .quantity(cartItem.getQuantity())
            .build())
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

    Order savedOrder =  orderRepository.save(order);

    cart.clearCart();

    return OrderResponse.from(savedOrder);

  }

}

