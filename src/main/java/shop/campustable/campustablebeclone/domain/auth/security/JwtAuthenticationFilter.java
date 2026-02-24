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
import shop.campustable.campustablebeclone.global.exception.ErrorResponse;

@Slf4j
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

    // 1. [토큰 추출]
    // resolveToken(request) 메서드를 호출하여 HTTP 요청 헤더(Authorization)에 숨겨진
    // "Bearer {토큰}" 문자열에서 순수 토큰 값만 쏙 뽑아냅니다.
    String token = resolveToken(request);

    // 토큰 존재 여부 확인
    if(token != null) {
      try {
        // 유효성 검사
        jwtTokenProvider.validateToken(token);

        // 인증 객체 생성
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        // SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (ExpiredJwtException e) {
        // 토큰이 만료된 경우, 클라이언트에게 명확한 에러 메시지와 함께 401 Unauthorized 상태 코드를 반환합니다.
        writeErrorResponse(response, ErrorCode.ACCESS_TOKEN_EXPIRED);
        return;
      }catch(JwtException | CustomException e) {
        // 토큰이 유효하지 않은 경우, 클라이언트에게 명확한 에러 메시지와 함께 401 Unauthorized 상태 코드를 반환합니다.
        writeErrorResponse(response,ErrorCode.JWT_INVALID);
        return;
      }
    }

    // 2. [필터 체인 계속 진행]
    filterChain.doFilter(request, response);

  }

  private String resolveToken(HttpServletRequest request) {
    // HTTP 요청 헤더에서 "Authorization" 값을 가져옵니다.
    String bearerToken = request.getHeader("Authorization");
    // 값이 비어있는지 확인 및 "Bearer "로 시작하는지 확인
    if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
      // "Bearer " 접두어를 제거하여 순수 토큰 값만 반환합니다.
      return bearerToken.substring(7);
    }
    return null;
  }

  private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    // HTTP 응답 상태 코드를 에러 코드에 해당하는 값으로 설정합니다.
    response.setStatus(errorCode.getStatus().value());
    // 응답의 Content-Type을 JSON으로 설정하고, 문자 인코딩을 UTF-8로 지정합니다.
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    // ErrorResponse 객체를 생성하여 에러 코드와 메시지를 담습니다.
    ErrorResponse errorJson = ErrorResponse.builder()
        .errorCode(errorCode)
        .errormessage(errorCode.getMessage())
        .build();

    // ObjectMapper를 사용하여 ErrorResponse 객체를 JSON 문자열로 변환한 후, HTTP 응답 본문에 작성합니다.
    response.getWriter().write(objectMapper.writeValueAsString(errorJson));
    response.getWriter().flush();

  }

}
