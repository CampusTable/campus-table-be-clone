package shop.campustable.campustablebeclone.domain.user.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.campustable.campustablebeclone.domain.user.dto.UserRequest;
import shop.campustable.campustablebeclone.domain.user.entity.User;

@RestController("/api/user")
public class UserController {

  @PostMapping("/login")
  public String login(@RequestBody UserRequest request) {

  }

}
