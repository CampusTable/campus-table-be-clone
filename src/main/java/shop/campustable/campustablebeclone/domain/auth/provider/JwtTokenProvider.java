package shop.campustable.campustablebeclone.domain.auth.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
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
  @Getter private final long expirationInMs; // 로그아웃 시간 계산용
  @Getter private final long refreshInMs;    // Redis TTL 설정용
  private final UserDetailsService userDetailsService;

  public JwtTokenProvider(@Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration-time}") long expirationInMs,
      @Value("${jwt.refresh-expiration-time}") long refreshInMs,
      UserDetailsService userDetailsService) {

    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationInMs = expirationInMs;
    this.refreshInMs = refreshInMs;
    this.userDetailsService = userDetailsService;

  }

  public String createAccessToken(Long studentId, String role, String jti) {
    long now = (new Date()).getTime();
    Date validityDate = new Date(now + this.expirationInMs);

    return Jwts.builder()
        .subject(String.valueOf(studentId))
        .claim("role", role)
        .claim("jti", jti)
        .issuedAt(new Date())
        .expiration(validityDate)
        .signWith(key, SIG.HS256)
        .compact();
  }

  public String createRefreshToken(Long studentId, String jti) {
    long now = (new Date()).getTime();
    Date validityDate = new Date(now + this.refreshInMs);

    return Jwts.builder()
        .subject(String.valueOf(studentId))
        .claim("jti", jti)
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

  public String getJti(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("jti",String.class);
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

  public void validateToken(String token) {
    Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token);
  }

  public Long getExpiration(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getExpiration()
        .getTime();
  }

}
