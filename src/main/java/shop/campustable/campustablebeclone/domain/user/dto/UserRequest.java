package shop.campustable.campustablebeclone.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import shop.campustable.campustablebeclone.domain.user.entity.User;
import shop.campustable.campustablebeclone.domain.user.entity.UserRole;

@Getter
@Setter
public class UserRequest {

  private Long studentId;
  private String password;
  private UserRole role;

  public  User toEntity(UserRequest request) {
      User user = User.builder()
          .studentId(studentId)
          .password(request.getPassword())
          .role(request.getRole())
          .build();
      return user;
  }

}
