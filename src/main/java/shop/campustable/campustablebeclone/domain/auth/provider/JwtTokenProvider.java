package shop.campustable.campustablebeclone.domain.auth.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  private final SecretKey key;
  private final long expirationInMs;
  private final long refreshInMs;
  private final UserDetailsService userDetailsService;

  public JwtTokenProvider(@Value("${jwt.secret}") String secret, UserDetailsService userDetailsService) {

    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationInMs = 1000 * 60 * 60;
    this.refreshInMs = 1000 * 60 * 60 * 24 * 14;
    this.userDetailsService = userDetailsService;

  }

  public String createAccessToken(Long studentId, String role) {
    long now = (new Date()).getTime();
    Date validityDate = new Date(now + this.expirationInMs);

    return Jwts.builder()
        .subject(String.valueOf(studentId))
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(validityDate)
        .signWith(key, SIG.HS256)
        .compact();
  }

  public String createRefreshToken(Long studentId) {
    long now = (new Date()).getTime();
    Date validityDate = new Date(now + this.refreshInMs);

    return Jwts.builder()
        .subject(String.valueOf(studentId))
        .issuedAt(new Date())
        .expiration(validityDate)
        .signWith(key, SIG.HS256)
        .compact();
  }

  public Authentication getAuthentication(String token) {

    Long studentId = getStudentId(token);

    UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(studentId));

    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

  }

  public Long getStudentId(String token) {
    String id = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();

    return Long.valueOf(id);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(key)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.warn("만료된 토큰입니다: {}", e.getMessage());
    } catch (SecurityException | MalformedJwtException e) {
      log.warn("잘못된 JWT 서명입니다: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.warn("지원되지 않는 JWT 토큰입니다: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.warn("JWT 토큰이 잘못되었습니다: {}", e.getMessage());
    }
    return false;
  }

}
