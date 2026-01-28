package shop.campustable.campustablebeclone.domain.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  /**
   * Handles access-denied events by sending a JSON error response with ErrorCode.ACCESS_DENIED.
   *
   * @param request the HTTP servlet request that resulted in access denial
   * @param response the HTTP servlet response used to write the JSON error payload
   * @param accessDeniedException the exception describing the access denial
   * @throws IOException if an I/O error occurs while writing the response
   */
  @Override
  public void handle(HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)throws IOException{

    log.warn("권한 없는 접근 감지. Handler 실행: {}", accessDeniedException.getMessage());

    writeErrorResponse(response, ErrorCode.ACCESS_DENIED);
  }

  /**
   * Writes a JSON-formatted error payload to the given HTTP response using values from the provided ErrorCode.
   *
   * @param response  the HTTP response to populate (status, content type, encoding, and body)
   * @param errorCode the error code whose HTTP status, name, and message will be used in the response body
   * @throws IOException if an I/O error occurs while serializing the payload or writing to the response
   */
  private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    response.setStatus(errorCode.getStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    ErrorResponse errorResponse = new ErrorResponse(errorCode.name(), errorCode.getMessage());
    String json = objectMapper.writeValueAsString(errorResponse);
    response.getWriter().write(json);
  }

  private record ErrorResponse(String errorCode, String errorMessage) {}

}