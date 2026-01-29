package shop.campustable.campustablebeclone.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import shop.campustable.campustablebeclone.domain.user.dto.UserResponse;
import shop.campustable.campustablebeclone.global.exception.ErrorResponse;

@Tag(name = "User", description = "유저 정보 관리 및 관리자 전용 API")
public interface UserControllerDocs {

  @Operation(
      summary = "내 정보 조회",
  description = """
      ### 요청 파라미터
      -없음
      (인증된 사용자의 Access Token에서 userId를 자동으로 조회)
      
      ### 응답 데이터 (UserResponse)
      - `userName` (String): 사용자 이름
      - `userId` (Long): 사용자 고유 ID
      - `role` (Enum): 사용자 역할
      - `studentId` (Long): 사용자 학번
      
      ### 사용 방법
        - 로그인된 사용자가 자신의 프로필 정보를 조회할 때 사용합니다.
        - 서버는 Access Token 내부의 userId 값을 기반으로 사용자 정보를 조회합니다.

        ### 유의 사항
        - Access Token이 반드시 필요합니다.
        - 응답 데이터는 현재 DB에 저장된 최신 사용자 정보를 반영합니다.

        ### 예외 처리
        - **USER_NOT_FOUND (404)**:  
          토큰은 유효하지만 해당 userId가 DB에 존재하지 않는 경우  
          ("유저를 찾을 수 없습니다.")
      """)
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<UserResponse> getMyInfo();

  @Operation(
      summary = "내 계정 삭제",
      description = """
        ### 요청 파라미터
        - 없음  
          (인증된 Access Token의 userId 기준으로 본인 계정 삭제)

        ### 응답 데이터
        - 없음 (204 No Content)

        ### 사용 방법
        - 사용자가 자신의 계정을 삭제할 때 사용합니다.
        - 삭제 후 서버는 해당 유저 정보와 연관된 Refresh Token을 모두 삭제합니다.

        ### 유의 사항
        - 계정 삭제 시 복구가 불가능합니다.
        - 반드시 Access Token이 포함된 요청이어야 합니다.

        ### 예외 처리
        - **USER_NOT_FOUND (404)**:  
          유저가 DB에 존재하지 않는 경우  
          ("유저를 찾을 수 없습니다.")
        """
  )
  @ApiResponse(responseCode = "204", description = "탈퇴 성공")
  ResponseEntity<Void> deleteMe();

  @Operation(
      summary = "전체 사용자 조회 (관리자)",
      description = """
        ### 요청 파라미터
        - 없음
        
        ### 응답 데이터
        - 사용자 목록(List<UserResponse>)
          - `userName` (String): 사용자 이름
          - `userId` (Long): 사용자 고유 ID
          - `role` (Enum): 사용자 역할 (예: ADMIN, USER)
          - `studentId` (Long): 사용자 학번
      

        ### 사용 방법
        - 관리자가 시스템 내 모든 사용자를 조회할 때 사용합니다.
        - 인증된 관리자 계정의 Access Token이 필요합니다.
        - 응답은 사용자 정보를 배열 형태로 전달합니다.

        ### 유의 사항
        - 본 API는 관리자만 호출할 수 있습니다.
        - 인증 토큰이 없거나 권한이 부족한 경우 요청이 거부될 수 있습니다.

        ### 예외 처리
        - **ACCESS_DENIED (403)**: 접근 권한이 없는 경우
        """
  )
  ResponseEntity<List<UserResponse>> getAllUsers();

  @Operation(
      summary = "특정 사용자 조회 (관리자)",
      description = """
        ### 요청 파라미터
        - `userId` (Long, Path Variable): 조회할 사용자 ID

        ### 응답 데이터 (UserResponse)
      - `userName` (String): 사용자 이름
      - `userId` (Long): 사용자 고유 ID
      - `role` (Enum): 사용자 역할
      - `studentId` (Long): 사용자 학번

        ### 사용 방법
        - 관리자가 특정 사용자를 조회 할 때 사용합니다.

        ### 유의 사항
        - 관리자 권한이 필요합니다.
        - 해당 userId가 존재하는지 확인 후 요청해야 합니다.

        ### 예외 처리
        - **USER_NOT_FOUND (404)**:  
          조회하려는 userId가 DB에 존재하지 않을 경우  
          ("유저를 찾을 수 없습니다.")
        """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<UserResponse> getUser(@PathVariable Long userId);

  @Operation(
      summary = "특정 사용자 삭제 (관리자)",
      description = """
        ### 요청 파라미터
        - `userId` (Long, Path Variable): 삭제할 사용자 ID

        ### 응답 데이터
        - 없음 (204 No Content)

        ### 사용 방법
        - 관리자가 특정 사용자를 강제로 삭제할 때 사용합니다.
        - DB의 사용자 정보 및 해당 사용자의 Refresh Token이 모두 삭제됩니다.

        ### 유의 사항
        - 관리자 권한이 필요합니다.
        - 해당 userId가 존재하는지 확인 후 요청해야 합니다.

        ### 예외 처리
        - **USER_NOT_FOUND (404)**:  
          삭제하려는 userId가 DB에 존재하지 않을 경우  
          ("유저를 찾을 수 없습니다.")
        """
  )
  @ApiResponse(responseCode = "204", description = "삭제 성공")
  ResponseEntity<Void> deleteUser(@PathVariable Long userId);



}
