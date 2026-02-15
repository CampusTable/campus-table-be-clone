package shop.campustable.campustablebeclone.domain.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.campustable.campustablebeclone.domain.cart.dto.CartItemRequest;
import shop.campustable.campustablebeclone.domain.cart.dto.CartResponse;
import shop.campustable.campustablebeclone.domain.cart.service.CartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController implements CartControllerDocs{

  private final CartService cartService;

  @Override
  @PostMapping
  public ResponseEntity<CartResponse> addOrUpdateCart(@Valid @RequestBody CartItemRequest request) {
    CartResponse response = cartService.addOrUpdateItem(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  @GetMapping
  public ResponseEntity<CartResponse> getMyCart() {
    return ResponseEntity.ok(cartService.getMyCart());
  }

  @Override
  @DeleteMapping
  public ResponseEntity<Void> clearMyCart() {
    cartService.clearMyCart();
    return ResponseEntity.noContent().build();
  }

  @Override
  @DeleteMapping("/items/{cart-item-id}")
  public ResponseEntity<CartResponse> deleteCartItem(@PathVariable("cart-item-id") Long cartItemId) {
    CartResponse response = cartService.deleteCartItem(cartItemId);
    return ResponseEntity.ok(response);
  }

}
