package shop.campustable.campustablebeclone.domain.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuRequest;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuResponse;
import shop.campustable.campustablebeclone.domain.menu.dto.TopMenuResponse;

import java.util.List;

@Tag(name = "Menu API", description = "메뉴 관리 및 조회 API")
public interface MenuControllerDocs {

  // ================================
  // 1. 메뉴 생성
  // ================================

  @Operation(
      summary = "메뉴 생성",
      description = """
          ### 요청 파라미터
          - `category-id` (Long, required, PathVariable): 메뉴가 속할 카테고리 ID
          - `menuName` (String, form-data)
          - `price` (Integer, form-data)
          - `available` (Boolean, form-data)
          - `stockQuantity` (Integer, form-data)
          - `image` (MultipartFile, form-data)
          
          ※ Content-Type: multipart/form-data
          
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
          1. 관리자 전용 API입니다.
          2. multipart/form-data 형식으로 메뉴 정보를 전송합니다.
          3. 이미지가 존재할 경우 S3에 업로드 후 URL이 저장됩니다.
          4. 동일 카테고리 내 동일한 메뉴 이름은 생성할 수 없습니다.
          
          ### 예외 처리
          - CATEGORY_NOT_FOUND (404 NOT_FOUND): 해당 카테고리를 찾을 수 없습니다.
          - MENU_ALREADY_EXISTS (409 CONFLICT): 이미 등록된 메뉴 이름입니다
          - INVALID_FILE_TYPE (400 BAD_REQUEST): 이미지 파일(.jpg, .jpeg, .png, .webp)만 업로드 가능합니다.
          - S3_UPLOAD_ERROR (500 INTERNAL_SERVER_ERROR): S3 파일 업로드 중 오류 발생
          - INTERNAL_SERVER_ERROR (500 INTERNAL_SERVER_ERROR): 서버에 문제가 발생했습니다.
          """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "메뉴 생성 성공",
          content = @Content(schema = @Schema(implementation = MenuResponse.class))),
      @ApiResponse(responseCode = "404", description = "CATEGORY_NOT_FOUND - 해당 카테고리를 찾을 수 없습니다."),
      @ApiResponse(responseCode = "409", description = "MENU_ALREADY_EXISTS - 이미 등록된 메뉴 이름입니다"),
      @ApiResponse(responseCode = "400", description = "INVALID_FILE_TYPE - 이미지 파일(.jpg, .jpeg, .png, .webp)만 업로드 가능합니다."),
      @ApiResponse(responseCode = "500", description = "S3_UPLOAD_ERROR / INTERNAL_SERVER_ERROR")
  })
  ResponseEntity<MenuResponse> createMenu(
      @Parameter(description = "카테고리 ID", required = true)
      @PathVariable("category-id") Long categoryId,
      @ModelAttribute MenuRequest request
  );

  // ================================
  // 2. 전체 메뉴 조회
  // ================================

  @Operation(
      summary = "전체 메뉴 조회",
      description = """
          ### 요청 파라미터
          없음
          
          ### 응답 데이터
          List<MenuResponse>
          
          ### 사용 방법
          - 등록된 모든 메뉴를 조회합니다.
          - 데이터가 없을 경우 빈 리스트를 반환합니다.
          
          ### 예외 처리
          - INTERNAL_SERVER_ERROR (500 INTERNAL_SERVER_ERROR): 서버에 문제가 발생했습니다.
          """
  )
  @ApiResponse(responseCode = "200", description = "조회 성공")
  ResponseEntity<List<MenuResponse>> getAllMenu();

  // ================================
  // 3. 카테고리별 메뉴 조회
  // ================================

  @Operation(
      summary = "카테고리별 메뉴 조회",
      description = """
          ### 요청 파라미터
          - `category-id` (Long, required)
          
          ### 응답 데이터
          List<MenuResponse>
          
          ### 예외 처리
          - CATEGORY_NOT_FOUND (404 NOT_FOUND): 해당 카테고리를 찾을 수 없습니다.
          """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "CATEGORY_NOT_FOUND - 해당 카테고리를 찾을 수 없습니다.")
  })
  ResponseEntity<List<MenuResponse>> getMenusByCategory(
      @PathVariable("category-id") Long categoryId
  );

  // ================================
  // 4. 단일 메뉴 조회
  // ================================

  @Operation(
      summary = "단일 메뉴 조회",
      description = """
          ### 요청 파라미터
          - `menu-id` (Long, required)
          
          ### 응답 데이터
          MenuResponse
          
          ### 예외 처리
          - MENU_NOT_FOUND (404 NOT_FOUND): 해당 메뉴를 찾을 수 없습니다.
          """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "MENU_NOT_FOUND - 해당 메뉴를 찾을 수 없습니다.")
  })
  ResponseEntity<MenuResponse> getMenu(
      @PathVariable("menu-id") Long menuId
  );

  // ================================
  // 5. 메뉴 수정
  // ================================

  @Operation(
      summary = "메뉴 수정",
      description = """
          ### 요청 파라미터
          - `menu-id` (Long, required)
          - `menuName` (String, form-data)
          - `price` (Integer, form-data)
          - `available` (Boolean, form-data)
          - `stockQuantity` (Integer, form-data)
          - `image` (MultipartFile, form-data)
          
          ※ Content-Type: multipart/form-data
          
          ### 응답 데이터
          MenuResponse
          
          ### 예외 처리
          - MENU_NOT_FOUND (404 NOT_FOUND): 해당 메뉴를 찾을 수 없습니다.
          - MENU_ALREADY_EXISTS (409 CONFLICT): 이미 등록된 메뉴 이름입니다
          - S3_DELETE_ERROR (500 INTERNAL_SERVER_ERROR): S3 파일 삭제 중 오류 발생
          """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수정 성공"),
      @ApiResponse(responseCode = "404", description = "MENU_NOT_FOUND - 해당 메뉴를 찾을 수 없습니다."),
      @ApiResponse(responseCode = "409", description = "MENU_ALREADY_EXISTS - 이미 등록된 메뉴 이름입니다"),
      @ApiResponse(responseCode = "500", description = "S3_DELETE_ERROR / INTERNAL_SERVER_ERROR")
  })
  ResponseEntity<MenuResponse> updateMenu(
      @PathVariable("menu-id") Long menuId,
      @ModelAttribute MenuRequest request
  );

  // ================================
  // 6. 식당별 TOP3 메뉴 조회
  // ================================

  @Operation(
      summary = "식당별 TOP3 메뉴 조회",
      description = """
          ### 요청 파라미터
          - `cafeteria-id` (Long, required)
          
          ### 응답 데이터
          List<TopMenuResponse>
          - `rank` (Long)
          - `menu` (MenuResponse)
          
          ### 동작 설명
          - Redis ZSet 기준 상위 3개 메뉴 조회
          - 데이터가 없을 경우 빈 리스트 반환
          
          ### 예외 처리
          - CAFETERIA_NOT_FOUND (404 NOT_FOUND): 요청한 식당 정보를 찾을 수 없습니다.
          """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "CAFETERIA_NOT_FOUND - 요청한 식당 정보를 찾을 수 없습니다.")
  })
  ResponseEntity<List<TopMenuResponse>> getTop3MenusByCafeteria(
      @PathVariable("cafeteria-id") Long cafeteriaId
  );
}
