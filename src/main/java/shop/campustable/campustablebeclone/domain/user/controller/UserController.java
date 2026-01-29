package shop.campustable.campustablebeclone.domain.user.controller;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.campustable.campustablebeclone.domain.user.dto.UserResponse;
import shop.campustable.campustablebeclone.domain.user.service.UserService;
import shop.campustable.campustablebeclone.global.common.SecurityUtil;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs{

  private final UserService userService;

  @Override
  @GetMapping("/me")
  public ResponseEntity<UserResponse> getMyInfo(){
    return ResponseEntity.ok(userService.getMyInfo());
  }

  @Override
  @DeleteMapping("/me")
  public ResponseEntity<Void> deleteMe(){

    userService.deleteMe();
    return ResponseEntity.noContent().build();
  }

  @Override
  @GetMapping("/admin/users")
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    List<UserResponse> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @Override
  @GetMapping("/admin/users/{id}")
  public ResponseEntity<UserResponse> getUser(@PathVariable Long id){
    UserResponse user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }


  @Override
  @DeleteMapping("/admin/users/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

}
