package shop.campustable.campustablebeclone.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.campustable.campustablebeclone.domain.order.dto.OrderResponse;
import shop.campustable.campustablebeclone.domain.order.service.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderResponse> createOrder(){
    return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder());
  }

}
