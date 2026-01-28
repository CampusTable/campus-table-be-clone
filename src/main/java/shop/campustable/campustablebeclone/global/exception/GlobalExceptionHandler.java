package shop.campustable.campustablebeclone.global.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  /**
   * Handles CustomException by converting it into an HTTP error response.
   *
   * Builds an ErrorResponse containing the exception's ErrorCode and its message,
   * and returns a ResponseEntity with the HTTP status derived from that ErrorCode.
   *
   * @param e the thrown CustomException containing an ErrorCode
   * @return a ResponseEntity whose body is an ErrorResponse (with errorCode and message)
   *         and whose HTTP status is taken from the exception's ErrorCode
   */
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

}