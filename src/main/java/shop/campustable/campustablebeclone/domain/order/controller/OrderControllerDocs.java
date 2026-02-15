package shop.campustable.campustablebeclone.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import shop.campustable.campustablebeclone.domain.order.dto.OrderResponse;

import java.util.List;

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

                ### 응답 데이터
                - `orderId` (Long): 주문 ID
                - `cafeteriaName` (String): 주문한 식당 이름
                - `orderItems` (List<OrderItemResponse>)
                    - `menuName` (String): 메뉴 이름
                    - `price` (int): 메뉴 단가
                    - `quantity` (int): 주문 수량
                    - `subtotal` (int): 해당 메뉴 총 금액
                    - `status` (OrderStatus): PREPARING / READY / COMPLETED / CANCELLED
                    - `categoryId` (Long): 카테고리 ID
                - `totalPrice` (int): 주문 전체 금액
                - `status` (OrderStatus): 주문 상태
                - `orderTime` (LocalDateTime): 주문 생성 시간

                ### 동작 설명
                1. 현재 로그인된 사용자의 장바구니를 기준으로 주문이 생성됩니다.
                2. 장바구니 항목을 기준으로 재고가 차감됩니다.
                3. 주문 생성 후 장바구니는 비워집니다.
                4. 트랜잭션 커밋 이후 Redis ZSet을 통해 메뉴 판매 랭킹이 갱신됩니다.

                ### 예외 처리
                - USER_NOT_FOUND (404 NOT_FOUND): 유저를 찾을 수 없습니다.
                - CART_NOT_FOUND (404 NOT_FOUND): 장바구니를 찾을 수 없습니다.
                - CART_EMPTY (400 BAD_REQUEST): 장바구니가 비어 있어 주문을 진행할 수 없습니다.
                - MENU_NOT_FOUND (404 NOT_FOUND): 해당 메뉴를 찾을 수 없습니다.
                - CAFETERIA_NOT_FOUND (404 NOT_FOUND): 요청한 식당 정보를 찾을 수 없습니다.
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "주문 생성 성공",
          content = @Content(schema = @Schema(implementation = OrderResponse.class))),
      @ApiResponse(responseCode = "400", description = "CART_EMPTY - 장바구니가 비어 있어 주문을 진행할 수 없습니다."),
      @ApiResponse(responseCode = "404", description = "USER_NOT_FOUND / CART_NOT_FOUND / MENU_NOT_FOUND / CAFETERIA_NOT_FOUND")
  })
  ResponseEntity<OrderResponse> createOrder();


  // ==========================================================
  // 2. 내 주문 목록 조회
  // ==========================================================

  @Operation(
      summary = "내 주문 목록 조회",
      description = """
                ### 요청 파라미터
                없음
                
                ※ 로그인된 사용자 기준으로 주문 목록이 조회됩니다.

                ### 응답 데이터
                List<OrderResponse>

                ### 동작 설명
                - 사용자의 모든 주문을 조회합니다.
                - 각 주문에는 주문 항목 및 식당 정보가 포함됩니다.

                ### 예외 처리
                - (별도의 CustomException 발생 없음)
                """
  )
  @ApiResponse(responseCode = "200", description = "조회 성공")
  ResponseEntity<List<OrderResponse>> getMyOrders();


  // ==========================================================
  // 3. 관리자 - 카테고리 READY 처리
  // ==========================================================

  @Operation(
      summary = "관리자 - 카테고리 READY 처리",
      description = """
                ### 요청 파라미터
                - `order-id` (Long, required): 주문 ID
                - `category-id` (Long, required): 카테고리 ID

                ### 동작 설명
                1. 해당 주문에서 특정 카테고리의 모든 주문 항목을 READY 상태로 변경합니다.
                2. 모든 카테고리 항목이 READY 상태가 되면 주문 전체 상태도 READY로 변경됩니다.
                3. ADMIN 권한이 필요합니다.

                ### 예외 처리
                - ORDER_NOT_FOUND (404 NOT_FOUND): 해당 주문을 찾을 수 없습니다.
                - CATEGORY_NOT_FOUND (404 NOT_FOUND): 해당 카테고리를 찾을 수 없습니다.
                - ORDER_ITEM_NOT_FOUND (404 NOT_FOUND): 해당 주문 내에 요청하신 카테고리의 메뉴가 존재하지 않습니다.
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
      @ApiResponse(responseCode = "404", description = "ORDER_NOT_FOUND / CATEGORY_NOT_FOUND / ORDER_ITEM_NOT_FOUND")
  })
  ResponseEntity<Void> markCategoryAsReady(
      @Parameter(description = "주문 ID", required = true)
      @PathVariable("order-id") Long orderId,
      @Parameter(description = "카테고리 ID", required = true)
      @PathVariable("category-id") Long categoryId
  );


  // ==========================================================
  // 4. 관리자 - 카테고리 COMPLETED 처리
  // ==========================================================

  @Operation(
      summary = "관리자 - 카테고리 COMPLETED 처리",
      description = """
                ### 요청 파라미터
                - `order-id` (Long, required): 주문 ID
                - `category-id` (Long, required): 카테고리 ID

                ### 동작 설명
                1. 해당 주문에서 특정 카테고리의 모든 주문 항목을 COMPLETED 상태로 변경합니다.
                2. 모든 카테고리 항목이 COMPLETED 상태가 되면 주문 전체 상태도 COMPLETED로 변경됩니다.
                3. ADMIN 권한이 필요합니다.

                ### 예외 처리
                - ORDER_NOT_FOUND (404 NOT_FOUND): 해당 주문을 찾을 수 없습니다.
                - CATEGORY_NOT_FOUND (404 NOT_FOUND): 해당 카테고리를 찾을 수 없습니다.
                - ORDER_ITEM_NOT_FOUND (404 NOT_FOUND): 해당 주문 내에 요청하신 카테고리의 메뉴가 존재하지 않습니다.
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
      @ApiResponse(responseCode = "404", description = "ORDER_NOT_FOUND / CATEGORY_NOT_FOUND / ORDER_ITEM_NOT_FOUND")
  })
  ResponseEntity<Void> markCategoryAsCompleted(
      @Parameter(description = "주문 ID", required = true)
      @PathVariable("order-id") Long orderId,
      @Parameter(description = "카테고리 ID", required = true)
      @PathVariable("category-id") Long categoryId
  );
}
