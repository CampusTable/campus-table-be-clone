package shop.campustable.campustablebeclone.domain.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)throws IOException{

    writeErrorResponse(response, ErrorCode.ACCESS_DENIED);
  }

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
