package shop.campustable.campustablebeclone.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {

  private String accessToken;
  private String refreshToken;
  private Long studentId;
  private String studentName;

}
