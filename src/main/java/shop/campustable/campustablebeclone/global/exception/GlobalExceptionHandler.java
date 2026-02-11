package shop.campustable.campustablebeclone.global.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    log.error("CustomException 발생: {}", e.getMessage(), e);

    ErrorCode errorCode = e.getErrorCode();

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(errorCode)
        .errormessage(errorCode.getMessage())
        .build();

    return ResponseEntity.status(errorCode.getStatus()).body(response);
  }

  /**
   * @Valid 검증 실패 시 (DTO 수준의 검증)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("handleMethodArgumentNotValidException: {}", e.getMessage());

    // 에러 메시지 중 첫 번째 것을 가져옵니다 (예: "수량은 최대 9개까지만 담을 수 있습니다.")
    String errorMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

    // ErrorCode.INVALID_INPUT_VALUE(400)를 기본으로 사용하되, 메시지만 DTO에 설정한 것으로 교체
    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.INVALID_INPUT_VALUE)
        .errormessage(errorMessage)
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * 그 외 의도하지 않은 모든 예외 처리
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Unhandled Exception 발생: {}", e.getMessage(), e);

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR) // 500 에러 코드
        .errormessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

}
