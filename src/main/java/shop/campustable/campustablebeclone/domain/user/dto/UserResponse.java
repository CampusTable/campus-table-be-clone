package shop.campustable.campustablebeclone.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.campustable.campustablebeclone.domain.user.entity.User;
import shop.campustable.campustablebeclone.domain.user.entity.UserRole;

@Getter
@Setter
public class UserResponse {

  private Long userId;
  private UserRole role;
  private String name;

  public UserResponse(User user){
    this.userId=user.getUserId();
    this.role=user.getRole();
    this.name=user.getName();
  }

}
