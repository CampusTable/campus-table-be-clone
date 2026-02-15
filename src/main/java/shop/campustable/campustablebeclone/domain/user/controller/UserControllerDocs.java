package shop.campustable.campustablebeclone.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import shop.campustable.campustablebeclone.domain.user.dto.UserResponse;

import java.util.List;

@Tag(name = "User API", description = "회원 관리 API")
@SecurityRequirement(name = "Bearer Token")
public interface UserControllerDocs {

  // ==========================================================
  // 1. 내 정보 조회
  // ==========================================================

  @Operation(
      summary = "내 정보 조회",
      description = """
                ### 📌 기능 설명
                로그인한 사용자의 정보를 조회합니다.

                ---
                ### 🔐 인증 필요
                Bearer Token 필요

                ---
                ### 📤 Response
                UserResponse 반환

                ---
                ### ❗ 예외
                - 404 NOT_FOUND
                  - USER_NOT_FOUND
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "404", description = "USER_NOT_FOUND")
  })
  ResponseEntity<UserResponse> getMyInfo();



  // ==========================================================
  // 2. 회원 탈퇴
  // ==========================================================

  @Operation(
      summary = "회원 탈퇴",
      description = """
                ### 📌 기능 설명
                로그인한 사용자를 삭제합니다.
                해당 사용자의 RefreshToken도 함께 삭제됩니다.

                ---
                ### 🔐 인증 필요
                Bearer Token 필요

                ---
                ### 📤 Response
                204 NO_CONTENT

                ---
                ### ❗ 예외
                - 404 NOT_FOUND
                  - USER_NOT_FOUND
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "회원 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "USER_NOT_FOUND")
  })
  ResponseEntity<Void> deleteMe();



  // ==========================================================
  // 3. 전체 회원 조회 (관리자)
  // ==========================================================

  @Operation(
      summary = "전체 회원 조회 (관리자)",
      description = """
                ### 📌 기능 설명
                모든 회원 목록을 조회합니다.

                ---
                ### 🔐 관리자 권한 필요

                ---
                ### 📤 Response
                List<UserResponse>
                """
  )
  @ApiResponse(responseCode = "200", description = "조회 성공")
  ResponseEntity<List<UserResponse>> getAllUsers();



  // ==========================================================
  // 4. 특정 회원 조회 (관리자)
  // ==========================================================

  @Operation(
      summary = "특정 회원 조회 (관리자)",
      description = """
                ### 📌 기능 설명
                특정 회원 정보를 조회합니다.

                ---
                ### 📥 Path Variable
                - id (Long)

                ---
                ### 🔐 관리자 권한 필요

                ---
                ### 📤 Response
                UserResponse

                ---
                ### ❗ 예외
                - 404 NOT_FOUND
                  - USER_NOT_FOUND
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "404", description = "USER_NOT_FOUND")
  })
  ResponseEntity<UserResponse> getUser(

      @Parameter(
          name = "id",
          description = "회원 ID",
          required = true,
          in = ParameterIn.PATH
      )
      @PathVariable Long id
  );



  // ==========================================================
  // 5. 회원 삭제 (관리자)
  // ==========================================================

  @Operation(
      summary = "회원 삭제 (관리자)",
      description = """
                ### 📌 기능 설명
                관리자가 특정 회원을 삭제합니다.
                해당 사용자의 RefreshToken도 함께 삭제됩니다.

                ---
                ### 📥 Path Variable
                - userId (Long)

                ---
                ### 🔐 관리자 권한 필요

                ---
                ### 📤 Response
                204 NO_CONTENT

                ---
                ### ❗ 예외
                - 404 NOT_FOUND
                  - USER_NOT_FOUND
                """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "회원 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "USER_NOT_FOUND")
  })
  ResponseEntity<Void> deleteUser(

      @Parameter(
          name = "userId",
          description = "삭제할 회원 ID",
          required = true,
          in = ParameterIn.PATH
      )
      @PathVariable Long userId
  );
}
