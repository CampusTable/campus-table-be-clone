package shop.campustable.campustablebeclone.domain.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

  @Id
  private Long studentId;

  @Column(name = "token", nullable = false)
  private String token;

  @Column(name = "expiration")
  private Long expiration;

  public void updateToken(String newtoken, Long newexpiration) {
    this.token = newtoken;
    this.expiration = newexpiration;
  }

}
