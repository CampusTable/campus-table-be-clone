package shop.campustable.campustablebeclone.domain.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;
import shop.campustable.campustablebeclone.global.exception.ErrorResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException{
    log.warn("미인증 요청 감지 (토큰 없음). EntryPoint 실행: {}", authException.getMessage());

    writeErrorResponse(response, ErrorCode.JWT_INVALID);
  }

  private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    response.setStatus(errorCode.getStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    ErrorResponse errorJson = ErrorResponse.builder()
        .errorCode(errorCode)
        .errormessage(errorCode.getMessage())
        .build();

    response.getWriter().write(objectMapper.writeValueAsString(errorJson));
    response.getWriter().flush();

  }

}
