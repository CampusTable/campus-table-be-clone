package shop.campustable.campustablebeclone.domain.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import shop.campustable.campustablebeclone.domain.auth.provider.JwtTokenProvider;
import shop.campustable.campustablebeclone.domain.auth.service.CustomUserDetailService;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    String token = resolveToken(request);

    if(token != null) {
      try {
        jwtTokenProvider.validateToken(token);

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (ExpiredJwtException e) {
        writeErrorResponse(response, ErrorCode.ACCESS_TOKEN_EXPIRED);
        return;
      }catch(JwtException | CustomException e) {
        writeErrorResponse(response,ErrorCode.JWT_INVALID);
        return;
      }
    }

    filterChain.doFilter(request, response);

  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    response.setStatus(errorCode.getStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    // 규격화된 에러 객체 생성 (기존 프로젝트의 구조 참고)
    ErrorResponse errorResponse = new ErrorResponse(errorCode.name(), errorCode.getMessage());

    // ObjectMapper를 이용해 객체를 JSON 문자열로 변환하여 전송
    String json = objectMapper.writeValueAsString(errorResponse);
    response.getWriter().write(json);
  }

  // 응답용 내부 레코드
  private record ErrorResponse(String errorCode, String errorMessage) {}

}
