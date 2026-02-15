package shop.campustable.campustablebeclone.domain.auth.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@RedisHash(value = "refreshToken")
public class RefreshToken {

  @Id
  private String jti;

  @Indexed
  private Long studentId;

  private String token;

  @TimeToLive
  private Long expiration;

  @Builder
  public RefreshToken(String jti, Long studentId, String token, Long expiration) {
    this.jti = jti;
    this.studentId = studentId;
    this.token = token;
    this.expiration = expiration;
  }
}
