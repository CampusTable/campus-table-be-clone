package shop.campustable.campustablebeclone.domain.cafeteria.controller;

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
import shop.campustable.campustablebeclone.domain.cafeteria.dto.OperatingHoursRequest;

import java.util.List;

@Tag(name = "Operating Hours API", description = "식당 운영시간 관리 API (관리자 전용)")
public interface OperatingHoursControllerDocs {

  @Operation(
      summary = "식당 운영시간 등록/수정",
      description = """
                ### 📌 기능 설명
                - 특정 식당의 운영시간을 등록하거나 기존 운영시간을 덮어씁니다.
                - 기존 운영시간은 모두 삭제 후 새로 저장됩니다.
                - 반드시 1개 이상의 운영시간 데이터를 전달해야 합니다.

                ---
                ### 📥 Path Variable
                - `cafeteria-id` (Long, required): 운영시간을 등록할 식당 ID

                ---
                ### 📥 Request Body (List<OperatingHoursRequest>)
                각 요소는 다음 필드를 포함합니다:

                - `dayOfWeek` (Enum, required)
                  - 허용 값: MON, TUE, WED, THU, FRI, SAT, SUN

                - `openTime` (LocalTime, required)
                  - 형식: HH:mm (예: 09:00)

                - `closeTime` (LocalTime, required)
                  - 형식: HH:mm (예: 18:00)

                - `breaksStartTime` (LocalTime, required)
                  - 형식: HH:mm (예: 14:00)

                - `breaksCloseTime` (LocalTime, required)
                  - 형식: HH:mm (예: 15:00)

                ---
                ### 📤 응답
                - 200 OK
                - Response Body 없음

                ---
                ### ❗ 예외 처리

                - 400 BAD_REQUEST
                  - Validation 실패 (@NotNull 위반)
                  - INVALID_OPERATING_HOURS (리스트가 null 또는 empty)

                - 404 NOT_FOUND
                  - CAFETERIA_NOT_FOUND (존재하지 않는 cafeteriaId)
                """
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "운영시간 저장 성공 (Response Body 없음)"
      ),
      @ApiResponse(
          responseCode = "400",
          description = """
                            BAD_REQUEST
                            - 요청 데이터 검증 실패
                            - INVALID_OPERATING_HOURS
                            """
      ),
      @ApiResponse(
          responseCode = "404",
          description = "CAFETERIA_NOT_FOUND - 존재하지 않는 식당"
      )
  })
  ResponseEntity<Void> saveOperatingHours(

      @Parameter(
          name = "cafeteria-id",
          description = "식당 ID",
          required = true,
          in = ParameterIn.PATH
      )
      @PathVariable("cafeteria-id") Long cafeteriaId,

      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "운영시간 리스트 (최소 1개 이상 필수)",
          required = true,
          content = @Content(
              array = @ArraySchema(
                  schema = @Schema(implementation = OperatingHoursRequest.class)
              )
          )
      )
      @RequestBody List<OperatingHoursRequest> requests
  );
}
