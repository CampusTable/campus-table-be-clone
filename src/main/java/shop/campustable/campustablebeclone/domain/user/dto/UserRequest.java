package shop.campustable.campustablebeclone.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import shop.campustable.campustablebeclone.domain.user.entity.User;

@Getter
@Setter
public class UserRequest {


  private String password;
  private String role;

  public  User toEntity(UserRequest request) {
      User user = User.builder()
          .password(request.getPassword())
          .role(request.getRole())
          .build();
      return user;
  }

}
