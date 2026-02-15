package shop.campustable.campustablebeclone.domain.cafeteria.controller;

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
import shop.campustable.campustablebeclone.domain.cafeteria.dto.CafeteriaRequest;
import shop.campustable.campustablebeclone.domain.cafeteria.dto.CafeteriaResponse;

import java.util.List;

@Tag(name = "Cafeteria API", description = "식당 생성, 조회, 수정 API")
public interface CafeteriaControllerDocs {

  // ==========================================================
  // 1. 식당 생성 (관리자)
  // ==========================================================

  @Operation(
      summary = "식당 생성",
      description = """
                ### 📌 기능 설명
                새로운 식당을 등록합니다.
                
                ⚠ 운영시간은 이 API에서 등록되지 않습니다.
                운영시간은 별도의 운영시간 API를 통해 등록해야 합니다.

                ---
                ### 📥 Request Body
                - `name` (String)
                - `description` (String)
                - `address` (String)

                ---
                ### 📤 Response
                - `id`
                - `name`
                - `description`
                - `address`
                - `operatingHours`
                  - 운영시간 목록
                  - 초기 생성 시 빈 리스트로 반환됩니다.

                ---
                ### ❗ 예외 처리
                - 409 CONFLICT
                  - CAFETERIA_ALREADY_EXISTS
                """
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "201",
          description = "식당 생성 성공",
          content = @Content(schema = @Schema(implementation = CafeteriaResponse.class))
      ),
      @ApiResponse(
          responseCode = "409",
          description = "CAFETERIA_ALREADY_EXISTS - 이미 존재하는 식당"
      )
  })
  ResponseEntity<CafeteriaResponse> createCafeteria(
      @RequestBody CafeteriaRequest request
  );


  // ==========================================================
  // 2. 전체 식당 조회
  // ==========================================================

  @Operation(
      summary = "전체 식당 조회",
      description = """
                ### 📌 기능 설명
                등록된 모든 식당 정보를 조회합니다.

                ---
                ### 📤 Response
                List<CafeteriaResponse>

                각 식당 정보:
                - `id`
                - `name`
                - `description`
                - `address`
                - `operatingHours`
                  - 운영시간 목록
                  - 운영시간이 등록되지 않은 경우 빈 리스트 반환

                ---
                ### ❗ 예외
                별도의 CustomException 발생 없음
                """
  )
  @ApiResponse(responseCode = "200", description = "조회 성공")
  ResponseEntity<List<CafeteriaResponse>> getAllCafeterias();


  // ==========================================================
  // 3. 식당 단건 조회
  // ==========================================================

  @Operation(
      summary = "식당 단건 조회",
      description = """
                ### 📌 기능 설명
                특정 식당의 상세 정보를 조회합니다.

                ---
                ### 📥 Path Variable
                - `cafeteria-id` (Long)

                ---
                ### 📤 Response
                - `id`
                - `name`
                - `description`
                - `address`
                - `operatingHours`
                  - 운영시간 목록
                  - 별도 운영시간 API에서 관리됨
                  - 등록되지 않은 경우 빈 리스트 반환

                ---
                ### ❗ 예외 처리
                - 404 NOT_FOUND
                  - CAFETERIA_NOT_FOUND
                """
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CafeteriaResponse.class))
      ),
      @ApiResponse(
          responseCode = "404",
          description = "CAFETERIA_NOT_FOUND - 존재하지 않는 식당"
      )
  })
  ResponseEntity<CafeteriaResponse> getCafeteriaById(
      @Parameter(
          name = "cafeteria-id",
          description = "식당 ID",
          required = true,
          in = ParameterIn.PATH
      )
      @PathVariable("cafeteria-id") Long cafeteriaId
  );


  // ==========================================================
  // 4. 식당 수정 (관리자)
  // ==========================================================

  @Operation(
      summary = "식당 정보 수정",
      description = """
                ### 📌 기능 설명
                기존 식당 정보를 수정합니다.

                ⚠ 운영시간은 이 API에서 수정되지 않습니다.
                운영시간 변경은 별도 운영시간 API를 사용해야 합니다.

                ---
                ### 📥 Path Variable
                - `cafeteria-id` (Long)

                ### 📥 Request Body
                - `name`
                - `description`
                - `address`

                ---
                ### 📤 Response
                수정된 CafeteriaResponse 반환
                (operatingHours 포함)

                ---
                ### ❗ 예외 처리
                - 404 NOT_FOUND
                  - CAFETERIA_NOT_FOUND
                """
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "수정 성공",
          content = @Content(schema = @Schema(implementation = CafeteriaResponse.class))
      ),
      @ApiResponse(
          responseCode = "404",
          description = "CAFETERIA_NOT_FOUND - 존재하지 않는 식당"
      )
  })
  ResponseEntity<CafeteriaResponse> updateCafeteria(
      @RequestBody CafeteriaRequest request,
      @PathVariable("cafeteria-id") Long cafeteriaId
  );
}
