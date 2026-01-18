package shop.campustable.campustablebeclone.domain.auth.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  private final SecretKey key;
  private final long expirationInMs;

  public JwtTokenProvider(@Value("${jwt.secret}") String secret){

    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationInMs = 1000 * 60 * 60;

  }

  public String createToken(Long studentId, String role){
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

  public Long getStudentId(String token){
    String id = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();

    return Long.valueOf(id);
  }

  public boolean validateToken(String token){
      try{
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseClaimsJws(token);
        return true;
      }
      catch(ExpiredJwtException e){
        log.info("유효하지 않은 토큰입니다: {}", e.getMessage());
      }
      return false;
  }

}
