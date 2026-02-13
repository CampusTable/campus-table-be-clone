package shop.campustable.campustablebeclone.domain.order.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.campustable.campustablebeclone.domain.order.dto.OrderResponse;
import shop.campustable.campustablebeclone.domain.order.service.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

  private final OrderService orderService;

  @PostMapping("/orders")
  public ResponseEntity<OrderResponse> createOrder(){
    return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder());
  }

  @GetMapping("/orders")
  public ResponseEntity<List<OrderResponse>> getMyOrders(){
    return ResponseEntity.ok(orderService.getMyOrders());
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PatchMapping("/admin/orders/{order-id}/categories/{category-id}/ready")
  public ResponseEntity<Void> markCategoryAsReady(
      @PathVariable(name = "order-id")Long orderId,
      @PathVariable(name = "category-id")Long categoryId
  ){
    orderService.markCategoryAsReady(orderId, categoryId);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PatchMapping("/admin/orders/{order-id}/categories/{category-id}/complete")
  public ResponseEntity<Void> markCategoryAsCompleted(
      @PathVariable(name = "order-id")Long orderId,
      @PathVariable(name = "category-id")Long categoryId
  ){
    orderService.markCategoryAsCompleted(orderId, categoryId);
    return ResponseEntity.ok().build();
  }

}
