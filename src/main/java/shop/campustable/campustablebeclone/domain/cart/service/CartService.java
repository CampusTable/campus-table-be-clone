package shop.campustable.campustablebeclone.domain.cart.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.campustable.campustablebeclone.domain.cart.dto.CartItemResponse;
import shop.campustable.campustablebeclone.domain.cart.dto.CartRequest;
import shop.campustable.campustablebeclone.domain.cart.dto.CartResponse;
import shop.campustable.campustablebeclone.domain.cart.entity.Cart;
import shop.campustable.campustablebeclone.domain.cart.repository.CartRepository;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;
import shop.campustable.campustablebeclone.domain.menu.repository.MenuRepository;
import shop.campustable.campustablebeclone.domain.user.entity.User;
import shop.campustable.campustablebeclone.domain.user.repository.UserRepository;
import shop.campustable.campustablebeclone.global.common.SecurityUtil;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CartService {

  private final CartRepository cartRepository;
  private final MenuRepository menuRepository;
  private final UserRepository userRepository;

  @Value("${spring.cloud.aws.s3.domain}")
  private String s3Domain;

  private String getFullUrl(String menuUrl) {
    if (menuUrl == null || menuUrl.isBlank()) {
      return null;
    }
    String baseUrl = s3Domain.endsWith("/") ? s3Domain : s3Domain + "/";
    String path = menuUrl.startsWith("/") ? menuUrl.substring(1) : menuUrl;
    return baseUrl + path;
  }

  public CartResponse addOrUpdateItem(CartRequest request) {
    Long userId = SecurityUtil.getCurrentUserId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("addOrUpdateItem: 유효하지 않은 user {}", userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    Menu menu = menuRepository.findById(request.getMenuId())
        .orElseThrow(() -> {
          log.warn("addOrUpdateItem: 해당 메뉴는 존재하지 않습니다. menuId: {}", request.getMenuId());
          return new CustomException(ErrorCode.MENU_NOT_FOUND);
        });

    Cart cart = cartRepository.findByUser(user)
        .orElseGet(() -> cartRepository.save(new Cart(user)));

    cart.addOrUpdateItem(menu, request.getQuantity());
    log.info("addOrUpdateItem: 유저 {} - 메뉴 {} 를 {}개 담았습니다.", userId, menu.getMenuName(), request.getQuantity());
    return getMyCart();
  }

  @Transactional(readOnly = true)
  public CartResponse getMyCart() {

    Long userId = SecurityUtil.getCurrentUserId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("getMyCart: 유효하지 않은 user {}", userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    Cart cart = cartRepository.findByUserWithItems(user)
        .orElse(null);

    if (cart == null || cart.getCartItems().isEmpty()) {
      return CartResponse.empty();
    }

    List<CartItemResponse> responses = cart.getCartItems().stream()
        .map(cartItem -> CartItemResponse.from(
            cartItem,
            getFullUrl(cartItem.getMenu().getMenuUrl())
        ))
        .toList();

    int totalPrice = responses.stream()
        .mapToInt(CartItemResponse::getSubtotal)
        .sum();

    return CartResponse.from(cart, responses, totalPrice);
  }

  public CartResponse deleteCartItem(Long cartItemId) {
    Long userId = SecurityUtil.getCurrentUserId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("deleteCartItem: 유효하지 않은 user {}", userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });
    Cart cart = cartRepository.findByUser(user)
        .orElseThrow(() -> {
          log.warn("deleteCartItem: 유저 {}에게 cart가 존재하지 않습니다.", userId);
          return new CustomException(ErrorCode.CART_NOT_FOUND);
        });
    cart.removeItem(cartItemId);
    return getMyCart();
  }

  public void clearMyCart() {
    Long userId = SecurityUtil.getCurrentUserId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("clearCart: 유효하지 않은 user {}", userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    Cart cart = cartRepository.findByUser(user)
        .orElseThrow(() -> {
          log.warn("clearCart: 유저 {}에게 cart가 존재하지 않습니다.", userId);
          return new CustomException(ErrorCode.CART_NOT_FOUND);
        });

    cart.clearCart();
  }


}
