package shop.campustable.campustablebeclone.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.campustable.campustablebeclone.domain.user.entity.User;
import shop.campustable.campustablebeclone.domain.user.entity.UserRole;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {


  private String userName;
  private Long userId;
  private UserRole role;
  private Long studentId;

  public static UserResponse from(User user) {
    return UserResponse.builder()
        .userName(user.getName())
        .userId(user.getId())
        .role(user.getRole())
        .studentId(user.getStudentId())
        .build();
  }

}
