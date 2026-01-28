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

  /**
   * Authenticates the request using a JWT from the Authorization header, or forwards the request if no token is present.
   *
   * If a Bearer token is present, validates the token and, on success, sets the resulting Authentication into the SecurityContext.
   * If the token is expired, writes an ACCESS_TOKEN_EXPIRED JSON error response and stops filter processing.
   * If the token is invalid for any other JWT-related reason, writes a JWT_INVALID JSON error response and stops filter processing.
   *
   * @throws ServletException if an error occurs during the filtering process
   * @throws IOException if an I/O error occurs during the filtering process
   */
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

  /**
   * Extracts a bearer JWT from the HTTP Authorization header.
   *
   * @param request the HTTP servlet request to read the Authorization header from
   * @return the JWT string when the Authorization header starts with "Bearer ", `null` otherwise
   */
  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  /**
   * Write a JSON error response to the given HttpServletResponse using the provided ErrorCode.
   *
   * The response status and body are derived from the ErrorCode; the body is a JSON-serialized
   * ErrorResponse containing the error code and its message. The response content type and
   * character encoding are set to application/json and UTF-8.
   *
   * @param response  the HttpServletResponse to write the error response to
   * @param errorCode the ErrorCode that determines the HTTP status and error message
   * @throws IOException if an I/O error occurs while writing the response body
   */
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