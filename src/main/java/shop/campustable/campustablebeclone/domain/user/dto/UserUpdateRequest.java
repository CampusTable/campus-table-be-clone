package shop.campustable.campustablebeclone.domain.user.dto;


import lombok.Getter;
import lombok.Setter;
import shop.campustable.campustablebeclone.domain.user.entity.UserRole;

@Getter
@Setter
public class UserUpdateRequest {

  private UserRole role;

}
