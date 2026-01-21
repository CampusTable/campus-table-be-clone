package shop.campustable.campustablebeclone.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ReissueRequest {

  private String refreshToken;

}
