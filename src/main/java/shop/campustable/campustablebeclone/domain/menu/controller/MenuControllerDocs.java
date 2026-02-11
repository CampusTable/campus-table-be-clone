package shop.campustable.campustablebeclone.domain.menu.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuRequest;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuResponse;

@Tag(name = "Menu API", description = "캠퍼스 테이블 메뉴 관리 API")
public interface MenuControllerDocs {


  @Operation(
      summary = "메뉴 생성",
      description = """
          ### 요청 파라미터
          - `category-id` (Long, required, PathVariable): 메뉴를 생성할 카테고리 ID
          
          ### 요청 바디
          - `menuName` (String): 메뉴 이름
          - `price` (Integer): 메뉴 가격
          - `available` (Boolean): 메뉴 판매 가능 여부
          - `stockQuantity` (Integer): 메뉴 재고 수량
          
          ### 응답 데이터
          - `menuId` (Long): 생성된 메뉴 ID
          - `categoryId` (Long): 카테고리 ID
          - `menuName` (String): 메뉴 이름
          - `price` (Integer): 메뉴 가격
          - `menuUrl` (String): 메뉴 이미지 URL (현재 null)
          - `available` (Boolean): 판매 가능 여부  
          - `stockQuantity` (Integer): 재고 수량
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시
          
          ### 사용 방법
          1. 관리자 권한으로 카테고리 ID를 PathVariable로 전달하여 요청합니다.
          2. 요청 바디에 메뉴 이름, 가격, 판매 여부, 재고 수량을 포함하여 전송합니다.
          3. 메뉴가 정상적으로 생성되면 생성된 메뉴 정보를 반환합니다.
          
          ### 유의 사항
          - 동일한 카테고리 내에 동일한 `menuName`이 존재할 경우 메뉴 생성이 불가능합니다.
          - `available` 값이 true이더라도 `stockQuantity`가 0 이하이면 판매 불가 상태로 저장됩니다.
          
          ### 예외 처리
          - `CATEGORY_NOT_FOUND` (404 BAD_REQUEST): 카테고리를 찾을 수 없습니다.
          - `MENU_ALREADY_EXISTS` (409 BAD_REQUEST): 이미 존재하는 메뉴입니다.
          """,
      requestBody = @RequestBody(
          content = @Content(
              mediaType = "multipart/form-data",
              schema = @Schema(implementation = MenuRequest.class)
          )
      )
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "메뉴 생성 성공"),
      @ApiResponse(responseCode = "404", description = "CATEGORY_NOT_FOUND - 해당 카테고리를 찾을 수 없습니다."),
      @ApiResponse(responseCode = "409", description = "MENU_ALREADY_EXISTS - 이미 등록된 메뉴 이름입니다.")
  })
  @Parameter(name = "category-id", description = "카테고리 고유 식별자", example = "1")
  ResponseEntity<MenuResponse> createMenu(
      @PathVariable(name = "category-id") Long categoryId,
      MenuRequest request);

  @Operation(
      summary = "전체 메뉴 조회",
      description = """
          ### 요청 파라미터
          - 없음
          
          ### 응답 데이터
          - 메뉴 목록(List)
            - `menuId` (Long)
            - `categoryId` (Long)
            - `menuName` (String)
            - `price` (Integer)
            - `menuUrl` (String)
            - `available` (Boolean)
            - `stockQuantity` (Integer)
            - `createdAt` (LocalDateTime)
            - `updatedAt` (LocalDateTime)
          
          ### 사용 방법
          1. 별도의 파라미터 없이 요청합니다.
          2. 등록된 모든 메뉴 목록을 반환합니다.
          
          ### 유의 사항
          - 카테고리 구분 없이 모든 메뉴가 조회됩니다.
          
          ### 예외 처리
          - 없음
          """
  )
  ResponseEntity<List<MenuResponse>> getAllMenu();

  @Operation(
      summary = "카테고리별 메뉴 조회",
      description = """
          ### 요청 파라미터
          - `category-id` (Long, required, PathVariable): 조회할 카테고리 ID
          
          ### 응답 데이터
          - 메뉴 목록(List)
            - `menuId` (Long)
            - `categoryId` (Long)
            - `menuName` (String)
            - `price` (Integer)
            - `menuUrl` (String)
            - `available` (Boolean)
            - `stockQuantity` (Integer)
            - `createdAt` (LocalDateTime)
            - `updatedAt` (LocalDateTime)
          
          ### 사용 방법
          1. 카테고리 ID를 PathVariable로 전달하여 요청합니다.
          2. 해당 카테고리에 속한 메뉴 목록을 반환합니다.
          
          ### 유의 사항
          - 존재하지 않는 카테고리 ID로 요청 시 오류가 발생합니다.
          
          ### 예외 처리
          - `CATEGORY_NOT_FOUND` (404 NOT_FOUND): 카테고리를 찾을 수 없습니다.
          """
  )
  @Parameter(name = "category-id", description = "카테고리 고유 식별자", example = "1")
  ResponseEntity<List<MenuResponse>> getMenusByCategory(@PathVariable("category-id") Long categoryId);

  @Operation(
      summary = "메뉴 단건 조회",
      description = """
          ### 요청 파라미터
          - `menu-id` (Long, required, PathVariable): 조회할 메뉴 ID
          
          ### 응답 데이터
          - `menuId` (Long)
          - `categoryId` (Long)
          - `menuName` (String)
          - `price` (Integer)
          - `menuUrl` (String)
          - `available` (Boolean)
          - `stockQuantity` (Integer)
          - `createdAt` (LocalDateTime)
          - `updatedAt` (LocalDateTime)
          
          ### 사용 방법
          1. 메뉴 ID를 PathVariable로 전달하여 요청합니다.
          2. 해당 메뉴의 상세 정보를 반환합니다.
          
          ### 유의 사항
          - 존재하지 않는 메뉴 ID로 요청 시 오류가 발생합니다.
          
          ### 예외 처리
          - `MENU_NOT_FOUND` (404 NOT_FOUND): 해당 메뉴를 찾을 수 없습니다.
          """
  )

  @Parameter(name = "menu-id", description = "메뉴 고유 식별자", example = "1")
  ResponseEntity<MenuResponse> getMenu(@PathVariable("menu-id") Long menuId);

  @Operation(
      summary = "메뉴 수정",
      description = """
          ### 요청 파라미터
          - `menu-id` (Long, required, PathVariable): 수정할 메뉴 ID
          
          ### 요청 바디
          - `menuName` (String): 수정할 메뉴 이름
          - `price` (Integer): 수정할 가격
          - `available` (Boolean): 판매 가능 여부
          - `stockQuantity` (Integer): 재고 수량
          
          ### 응답 데이터
          - `menuId` (Long)
          - `categoryId` (Long)
          - `menuName` (String)
          - `price` (Integer)
          - `menuUrl` (String)
          - `available` (Boolean)
          - `stockQuantity` (Integer)
          - `createdAt` (LocalDateTime)
          - `updatedAt` (LocalDateTime)
          
          ### 사용 방법
          1. 수정할 메뉴 ID를 PathVariable로 전달합니다.
          2. 변경이 필요한 필드만 요청 바디에 포함하여 전송합니다.
          3. 수정된 메뉴 정보를 반환합니다.
          
          ### 유의 사항
          - 동일 카테고리 내에 이미 존재하는 메뉴 이름으로 변경할 수 없습니다.
          - `menuName`이 null 또는 공백일 경우 중복 검사를 수행하지 않습니다.
          
          ### 예외 처리
          - `MENU_NOT_FOUND` (404 NOT_FOUND): 메뉴를 찾을 수 없습니다.
          - `MENU_ALREADY_EXISTS` (409 CONFLICT): 이미 존재하는 메뉴입니다.
          """,
      requestBody = @RequestBody(
          content = @Content(
              mediaType = "multipart/form-data",
              schema = @Schema(implementation = MenuRequest.class)
          )
      )
  )

  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수정 성공"),
      @ApiResponse(responseCode = "404", description = "MENU_NOT_FOUND - 해당 메뉴를 찾을 수 없습니다."),
      @ApiResponse(responseCode = "409", description = "MENU_ALREADY_EXISTS - 이미 등록된 메뉴 이름입니다.")
  })
  ResponseEntity<MenuResponse> updateMenu(
      @PathVariable("menu-id") Long menuId,
      MenuRequest request
  );
}
