package shop.campustable.campustablebeclone.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
public class OrderController implements OrderControllerDocs {

  private final OrderService orderService;

  @Override
  @PostMapping("/orders")
  public ResponseEntity<OrderResponse> createOrder(){
    return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder());
  }

  @Override
  @GetMapping("/orders")
  public ResponseEntity<Page<OrderResponse>> getMyOrders(
      @PageableDefault(size = 10, sort = "id", direction = Direction.DESC) Pageable pageable){
    return ResponseEntity.ok(orderService.getMyOrders(pageable));
  }
  //생성일, 좋아요 순 --> 파라미터 받을떄 (동적) 공부

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  @PatchMapping("/admin/orders/{order-id}/categories/{category-id}/ready")
  public ResponseEntity<Void> markCategoryAsReady(
      @PathVariable(name = "order-id")Long orderId,
      @PathVariable(name = "category-id")Long categoryId
  ){
    orderService.markCategoryAsReady(orderId, categoryId);
    return ResponseEntity.ok().build();
  }

  @Override
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
