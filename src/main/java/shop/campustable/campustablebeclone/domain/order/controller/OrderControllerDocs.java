package shop.campustable.campustablebeclone.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import shop.campustable.campustablebeclone.domain.order.dto.OrderResponse;

@Tag(name = "Order API", description = "주문 생성 및 주문 상태 관리 API")
public interface OrderControllerDocs {

  // ==========================================================
  // 1. 주문 생성
  // ==========================================================

  @Operation(
      summary = "주문 생성",
      description = """
                ### 요청 파라미터
                없음
                
                ※ 로그인된 사용자의 장바구니 기준으로 주문이 생성됩니다.
                ※ Request Body는 존재하지 않습니다.

                ### 응답 데이터 (OrderResponse)
                - `orderId` (Long): 주문 ID
                - `cafeteriaName` (String): 주문한 식당 이름
                - `orderItems` (List<OrderItemResponse>)
                    - `menuName` (String): 메뉴 이름
                    - `price` (int): 메뉴 단가
                    - `quantity` (int): 주문 수량
                    - `subtotal` (int): 해당 메뉴 총 금액 (price * quantity)
                    - `status` (OrderStatus): 주문 항목 상태
                    - `categoryId` (Long): 카테고리 ID
                - `totalPrice` (int): 주문 전체 금액
                - `status` (OrderStatus): 주문 상태
                - `orderTime` (LocalDateTime): 주문 생성 시간

                ### 사용 방법
                1. 사용자는 장바구니에 메뉴를 담습니다.
                2. POST /api/orders 호출 시, 서버는 현재 로그인 사용자 기준으로 주문을 생성합니다.
                3. 장바구니에 담긴 각 메뉴의 재고가 차감됩니다.
                4. 주문 생성 후 장바구니는 비워집니다.
                5. 트랜잭션 커밋 이후 Redis ZSet에 메뉴 판매 수량이 누적됩니다.

                ### 유의 사항
                - 로그인된 사용자만 주문 생성이 가능합니다.
                - 장바구니가 비어 있으면 주문이 생성되지 않습니다.
                - 메뉴 재고는 주문 생성 시 차감됩니다.

                ### 예외 처리
                - USER_NOT_FOUND (404 NOT_FOUND): 유저를 찾을 수 없습니다.
                - CART_NOT_FOUND (404 NOT_FOUND): 장바구니를 찾을 수 없습니다.
                - CART_EMPTY (400 BAD_REQUEST): 장바구니가 비어 있어 주문을 진행할 수 없습니다.
                - MENU_NOT_FOUND (404 NOT_FOUND): 해당 메뉴를 찾을 수 없습니다.
                - CAFETERIA_NOT_FOUND (404 NOT_FOUND): 요청한 식당 정보를 찾을 수 없습니다.
                """
  )
  ResponseEntity<OrderResponse> createOrder();



  // ==========================================================
  // 2. 내 주문 목록 조회
  // ==========================================================

  @Operation(
      summary = "내 주문 목록 조회",
      description = """
                ### 요청 파라미터 (Query Parameter - Pageable)
                - `page` (int, optional, 기본값 0): 페이지 번호
                - `size` (int, optional, 기본값 10): 페이지 당 데이터 수
                - `sort` (String, optional, 기본값 id,DESC): 정렬 조건
                
                기본 설정:
                - size = 10
                - sort = id DESC

                ※ 로그인된 사용자 기준으로 주문이 조회됩니다.

                ### 응답 데이터 (Page<OrderResponse>)
                - `content` (List<OrderResponse>): 주문 목록
                - `totalElements` (long): 전체 주문 수
                - `totalPages` (int): 전체 페이지 수
                - `size` (int): 페이지 크기
                - `number` (int): 현재 페이지 번호
                - `first` (boolean): 첫 페이지 여부
                - `last` (boolean): 마지막 페이지 여부

                각 OrderResponse는 아래 필드를 포함합니다:
                - `orderId`
                - `cafeteriaName`
                - `orderItems`
                - `totalPrice`
                - `status`
                - `orderTime`

                ### 사용 방법
                - GET /api/orders?page=0&size=10 호출
                - 로그인한 사용자의 주문만 조회됩니다.
                - 최신 주문(id DESC) 기준으로 정렬됩니다.

                ### 유의 사항
                - 다른 사용자의 주문은 조회할 수 없습니다.
                - 정렬 기준을 변경하려면 sort 파라미터를 사용해야 합니다.

                ### 예외 처리
                (Service 내부에서 CustomException을 발생시키지 않음)
                """
  )
  ResponseEntity<Page<OrderResponse>> getMyOrders(Pageable pageable);



  // ==========================================================
  // 3. 관리자 - 카테고리 READY 처리
  // ==========================================================

  @Operation(
      summary = "관리자 - 카테고리 READY 처리",
      description = """
                ### 요청 파라미터 (Path Variable)
                - `order-id` (Long, required): 주문 ID
                - `category-id` (Long, required): 카테고리 ID

                ### 사용 방법
                1. ADMIN 권한이 필요합니다.
                2. 특정 주문 내에서 해당 카테고리에 속한 모든 주문 항목을 READY 상태로 변경합니다.
                3. 해당 주문의 모든 카테고리 항목이 READY 상태가 되면 주문 전체 상태도 READY로 변경됩니다.

                ### 유의 사항
                - 주문은 OPTIMISTIC_FORCE_INCREMENT 락을 사용하여 동시성 제어를 수행합니다.
                - 해당 주문에 요청한 카테고리의 항목이 없으면 예외가 발생합니다.

                ### 예외 처리
                - ORDER_NOT_FOUND (404 NOT_FOUND): 해당 주문을 찾을 수 없습니다.
                - CATEGORY_NOT_FOUND (404 NOT_FOUND): 해당 카테고리를 찾을 수 없습니다.
                - ORDER_ITEM_NOT_FOUND (404 NOT_FOUND): 해당 주문 내에 요청하신 카테고리의 메뉴가 존재하지 않습니다.
                """
  )
  ResponseEntity<Void> markCategoryAsReady(
      @PathVariable("order-id") Long orderId,
      @PathVariable("category-id") Long categoryId
  );



  // ==========================================================
  // 4. 관리자 - 카테고리 COMPLETED 처리
  // ==========================================================

  @Operation(
      summary = "관리자 - 카테고리 COMPLETED 처리",
      description = """
                ### 요청 파라미터 (Path Variable)
                - `order-id` (Long, required): 주문 ID
                - `category-id` (Long, required): 카테고리 ID

                ### 사용 방법
                1. ADMIN 권한이 필요합니다.
                2. 특정 주문 내에서 해당 카테고리에 속한 모든 주문 항목을 COMPLETED 상태로 변경합니다.
                3. 해당 주문의 모든 카테고리 항목이 COMPLETED 상태가 되면 주문 전체 상태도 COMPLETED로 변경됩니다.

                ### 유의 사항
                - 주문은 OPTIMISTIC_FORCE_INCREMENT 락을 사용하여 동시성 제어를 수행합니다.
                - 해당 주문에 요청한 카테고리의 항목이 없으면 예외가 발생합니다.

                ### 예외 처리
                - ORDER_NOT_FOUND (404 NOT_FOUND): 해당 주문을 찾을 수 없습니다.
                - CATEGORY_NOT_FOUND (404 NOT_FOUND): 해당 카테고리를 찾을 수 없습니다.
                - ORDER_ITEM_NOT_FOUND (404 NOT_FOUND): 해당 주문 내에 요청하신 카테고리의 메뉴가 존재하지 않습니다.
                """
  )
  ResponseEntity<Void> markCategoryAsCompleted(
      @PathVariable("order-id") Long orderId,
      @PathVariable("category-id") Long categoryId
  );
}
