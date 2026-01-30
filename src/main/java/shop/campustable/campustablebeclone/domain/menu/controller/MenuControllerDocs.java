package shop.campustable.campustablebeclone.domain.menu.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuRequest;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuResponse;

@Tag(name = "Menu API", description = "캠퍼스 테이블 메뉴 관리 API")
public interface MenuControllerDocs {


  @Operation(
      summary = "메뉴 생성 (Admin)",
      description = """
            관리자 권한으로 새로운 메뉴를 시스템에 등록합니다.
            
            ### 유의 사항
            - **중복 체크**: 동일한 `menuName`이 이미 존재할 경우 `409 Conflict`를 반환합니다.
            - **재고 설정**: 초기 재고가 0인 경우 `available` 상태는 자동으로 `false`로 설정될 수 있습니다.
            
            ### 주요 오류 코드
            - **MENU_ALREADY_EXISTS (409)**: 이미 등록된 메뉴 이름입니다.
            - **INVALID_INPUT (400)**: 가격이 0 이하이거나 필수 값이 누락되었습니다.
            """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "메뉴 생성 성공"),
      @ApiResponse(responseCode = "400", description = "요청 값 오류"),
      @ApiResponse(responseCode = "409", description = "메뉴 이름 중복")
  })
  ResponseEntity<MenuResponse> createMenu(@RequestBody MenuRequest request);

  @Operation(
      summary = "전체 메뉴 조회",
      description = """
            현재 시스템에 등록된 모든 메뉴 리스트를 조회합니다.
            
            ### 반환 정보
            - 메뉴 ID, 이름, 가격, 이미지 URL, 판매 가능 여부, 재고 수량을 포함한 리스트를 반환합니다.
            - 별도의 페이징 처리가 없는 전체 조회 API입니다.
            """
  )
  ResponseEntity<List<MenuResponse>> getAllMenu();

  @Operation(
      summary = "단일 메뉴 상세 조회",
      description = """
            메뉴 식별자(ID)를 통해 특정 메뉴의 상세 정보를 조회합니다.
            
            ### Path Variable
            - `menu-id` (Long): 조회하고자 하는 메뉴의 고유 PK
            
            ### 주요 오류 코드
            - **MENU_NOT_FOUND (404)**: 해당 ID의 메뉴가 존재하지 않습니다.
            """
  )
  @Parameter(name = "menu-id", description = "메뉴 고유 식별자", example = "1")
  ResponseEntity<MenuResponse> getMenu(@PathVariable("menu-id") Long menuId);

  @Operation(
      summary = "메뉴 정보 수정 (Admin)",
      description = """
            관리자 권한으로 기존 메뉴의 정보를 수정합니다. **부분 업데이트(Partial Update)**를 지원합니다.
            
            ### 수정 로직 상세
            - **메뉴 이름**: 전달된 경우에만 수정하며, 본인을 제외한 다른 메뉴와 이름이 중복되는지 체크합니다.
            - **가격**: 0보다 큰 값일 때만 반영됩니다.
            - **재고/상태 연동**: `stockQuantity`를 0으로 수정할 경우, `available` 상태는 자동으로 `false`(품절)로 변경됩니다.
            
            ### 유의 사항
            - 수정하지 않을 필드는 `null` 또는 빈 값으로 전달하면 기존 값이 유지됩니다.
            - `menu-id`는 필수 Path Variable입니다.
            
            ### 요청 예시 (JSON)
            ```json
            {
              "price": 18000,
              "stockQuantity": 0
            }
            ```
            """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수정 성공"),
      @ApiResponse(responseCode = "404", description = "메뉴를 찾을 수 없음"),
      @ApiResponse(responseCode = "409", description = "변경하려는 이름이 이미 사용 중임")
  })
  ResponseEntity<MenuResponse> updateMenu(
      @PathVariable("menu-id") Long menuId,
      @RequestBody MenuRequest request
  );
}
