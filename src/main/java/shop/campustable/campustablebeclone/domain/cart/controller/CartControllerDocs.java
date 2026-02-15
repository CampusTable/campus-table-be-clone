package shop.campustable.campustablebeclone.domain.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shop.campustable.campustablebeclone.domain.cart.dto.CartItemRequest;
import shop.campustable.campustablebeclone.domain.cart.dto.CartResponse;

@Tag(name = "Cart API", description = "장바구니 관리 API (로그인 사용자 전용)")
public interface CartControllerDocs {

  // ==========================================================
  // 1. 장바구니 아이템 추가/수정
  // ==========================================================

  @Operation(
      summary = "장바구니 아이템 추가 또는 수량 수정",
      description = """
                ### 📌 기능 설명
                - 메뉴를 장바구니에 추가합니다.
                - 이미 존재하는 메뉴인 경우 수량을 수정합니다.
                - 수량은 0~9 사이 값만 허용됩니다.

                ---
                ### 📥 Request Body
                - `menuId` (Long, required)
                - `quantity` (Integer, required)
                  - 최소 0
                  - 최대 9

                ---
                ### 📤 Response
                최신 CartResponse 반환

                ---
                ### ❗ 예외 처리
                - 400 BAD_REQUEST
                  - Validation 실패
                - 404 NOT_FOUND
                  - USER_NOT_FOUND
                  - MENU_NOT_FOUND
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "장바구니 추가/수정 성공",
          content = @Content(schema = @Schema(implementation = CartResponse.class))),
      @ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패"),
      @ApiResponse(responseCode = "404", description = "USER_NOT_FOUND 또는 MENU_NOT_FOUND")
  })
  ResponseEntity<CartResponse> addOrUpdateCart(
      @RequestBody CartItemRequest request
  );


  // ==========================================================
  // 2. 내 장바구니 조회
  // ==========================================================

  @Operation(
      summary = "내 장바구니 조회",
      description = """
                ### 📌 기능 설명
                로그인 사용자의 장바구니를 조회합니다.

                - 장바구니가 없거나 비어있을 경우:
                  - cartId: null
                  - cafeteriaId: null
                  - cartItems: []
                  - totalPrice: 0

                ---
                ### ❗ 예외 처리
                - 404 NOT_FOUND
                  - USER_NOT_FOUND
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CartResponse.class))),
      @ApiResponse(responseCode = "404", description = "USER_NOT_FOUND")
  })
  ResponseEntity<CartResponse> getMyCart();


  // ==========================================================
  // 3. 장바구니 전체 비우기
  // ==========================================================

  @Operation(
      summary = "장바구니 전체 비우기",
      description = """
                ### 📌 기능 설명
                로그인 사용자의 장바구니를 모두 삭제합니다.

                ---
                ### 📤 Response
                - 204 NO_CONTENT
                - Response Body 없음

                ---
                ### ❗ 예외 처리
                - 404 NOT_FOUND
                  - USER_NOT_FOUND
                  - CART_NOT_FOUND
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "장바구니 비우기 성공"),
      @ApiResponse(responseCode = "404", description = "USER_NOT_FOUND 또는 CART_NOT_FOUND")
  })
  ResponseEntity<Void> clearMyCart();


  // ==========================================================
  // 4. 장바구니 아이템 삭제
  // ==========================================================

  @Operation(
      summary = "장바구니 개별 아이템 삭제",
      description = """
                ### 📌 기능 설명
                특정 장바구니 아이템을 삭제합니다.
                삭제 후 최신 장바구니 정보를 반환합니다.

                ---
                ### 📥 Path Variable
                - `cart-item-id` (Long)

                ---
                ### ❗ 예외 처리
                - 404 NOT_FOUND
                  - USER_NOT_FOUND
                  - CART_NOT_FOUND
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "삭제 성공",
          content = @Content(schema = @Schema(implementation = CartResponse.class))),
      @ApiResponse(responseCode = "404", description = "USER_NOT_FOUND 또는 CART_NOT_FOUND")
  })
  ResponseEntity<CartResponse> deleteCartItem(

      @Parameter(
          name = "cart-item-id",
          description = "장바구니 아이템 ID",
          required = true,
          in = ParameterIn.PATH
      )
      @PathVariable("cart-item-id") Long cartItemId
  );
}
