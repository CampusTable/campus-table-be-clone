package shop.campustable.campustablebeclone.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.campustable.campustablebeclone.domain.user.entity.User;

@Getter
@Setter
public class UserResponse {

  private Long userId;
  private String role;

  public UserResponse(User user){
    this.userId=user.getUserId();
    this.role=user.getRole();
  }

}
