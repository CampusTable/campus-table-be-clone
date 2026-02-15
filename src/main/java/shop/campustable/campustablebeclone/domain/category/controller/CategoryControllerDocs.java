package shop.campustable.campustablebeclone.domain.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shop.campustable.campustablebeclone.domain.category.dto.CategoryRequest;
import shop.campustable.campustablebeclone.domain.category.dto.CategoryResponse;

import java.util.List;

@Tag(name = "Category API", description = "카테고리 관리 API")
public interface CategoryControllerDocs {

  // ==========================================================
  // 1. 카테고리 생성 (관리자)
  // ==========================================================

  @Operation(
      summary = "카테고리 생성",
      description = """
                ### 📌 기능 설명
                특정 식당에 카테고리를 생성합니다.
                동일 식당 내에서 카테고리 이름은 중복될 수 없습니다.

                ---
                ### 📥 Path Variable
                - `cafeteria-id` (Long)

                ---
                ### 📥 Request Body
                - `name` (String, required)
                  - 공백 불가

                ---
                ### 📤 Response
                생성된 CategoryResponse 반환

                ---
                ### ❗ 예외 처리
                - 400 BAD_REQUEST
                  - Validation 실패 (@NotBlank)
                - 404 NOT_FOUND
                  - CAFETERIA_NOT_FOUND
                - 409 CONFLICT
                  - CATEGORY_ALREADY_EXISTS
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "카테고리 생성 성공",
          content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
      @ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패"),
      @ApiResponse(responseCode = "404", description = "CAFETERIA_NOT_FOUND"),
      @ApiResponse(responseCode = "409", description = "CATEGORY_ALREADY_EXISTS")
  })
  ResponseEntity<CategoryResponse> createCategory(

      @Parameter(
          name = "cafeteria-id",
          description = "식당 ID",
          required = true,
          in = ParameterIn.PATH
      )
      @PathVariable("cafeteria-id") Long cafeteriaId,

      @RequestBody CategoryRequest request
  );


  // ==========================================================
  // 2. 전체 카테고리 조회
  // ==========================================================

  @Operation(
      summary = "전체 카테고리 조회",
      description = """
                ### 📌 기능 설명
                모든 카테고리를 조회합니다.

                ---
                ### 📤 Response
                List<CategoryResponse>

                ---
                ### ❗ 예외
                별도 CustomException 발생 없음
                """
  )
  @ApiResponse(responseCode = "200", description = "조회 성공")
  ResponseEntity<List<CategoryResponse>> getAllCategories();


  // ==========================================================
  // 3. 식당별 카테고리 조회
  // ==========================================================

  @Operation(
      summary = "식당별 카테고리 조회",
      description = """
                ### 📌 기능 설명
                특정 식당에 속한 카테고리 목록을 조회합니다.

                ---
                ### 📥 Path Variable
                - `cafeteria-id` (Long)

                ---
                ### 📤 Response
                List<CategoryResponse>

                ---
                ### ❗ 예외 처리
                - 404 NOT_FOUND
                  - CAFETERIA_NOT_FOUND
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "CAFETERIA_NOT_FOUND")
  })
  ResponseEntity<List<CategoryResponse>> getCategoriesByCafeteriaId(

      @Parameter(
          name = "cafeteria-id",
          description = "식당 ID",
          required = true,
          in = ParameterIn.PATH
      )
      @PathVariable("cafeteria-id") Long cafeteriaId
  );


  // ==========================================================
  // 4. 카테고리 수정 (관리자)
  // ==========================================================

  @Operation(
      summary = "카테고리 수정",
      description = """
                ### 📌 기능 설명
                카테고리 이름을 수정합니다.
                동일 식당 내에서 동일한 이름으로 수정할 수 없습니다.

                ---
                ### 📥 Path Variable
                - `category-id` (Long)

                ---
                ### 📥 Request Body
                - `name` (String, required)
                  - 공백 불가

                ---
                ### 📤 Response
                수정된 CategoryResponse 반환

                ---
                ### ❗ 예외 처리
                - 400 BAD_REQUEST
                  - Validation 실패
                - 404 NOT_FOUND
                  - CATEGORY_NOT_FOUND
                - 409 CONFLICT
                  - CATEGORY_ALREADY_EXISTS
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수정 성공",
          content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
      @ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패"),
      @ApiResponse(responseCode = "404", description = "CATEGORY_NOT_FOUND"),
      @ApiResponse(responseCode = "409", description = "CATEGORY_ALREADY_EXISTS")
  })
  ResponseEntity<CategoryResponse> updateCategory(

      @Parameter(
          name = "category-id",
          description = "카테고리 ID",
          required = true,
          in = ParameterIn.PATH
      )
      @PathVariable("category-id") Long categoryId,

      @RequestBody CategoryRequest request
  );
}
