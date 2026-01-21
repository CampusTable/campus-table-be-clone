package shop.campustable.campustablebeclone.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.campustable.campustablebeclone.domain.auth.dto.ReissueRequest;
import shop.campustable.campustablebeclone.domain.auth.dto.TokenResponse;
import shop.campustable.campustablebeclone.domain.auth.service.AuthService;
import shop.campustable.campustablebeclone.domain.user.dto.UserRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@RequestBody UserRequest request){
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/reissue")
  public ResponseEntity<TokenResponse> reisuue(@RequestBody ReissueRequest request){
    return ResponseEntity.ok(authService.reissue(request.getRefreshToken()));
  }

}
