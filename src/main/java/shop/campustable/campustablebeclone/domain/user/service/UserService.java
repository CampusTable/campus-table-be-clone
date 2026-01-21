package shop.campustable.campustablebeclone.domain.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.campustable.campustablebeclone.domain.auth.dto.SejongMemberInfo;
import shop.campustable.campustablebeclone.domain.auth.dto.TokenResponse;
import shop.campustable.campustablebeclone.domain.auth.entity.RefreshToken;
import shop.campustable.campustablebeclone.domain.auth.provider.JwtTokenProvider;
import shop.campustable.campustablebeclone.domain.auth.repository.RefreshTokenRepository;
import shop.campustable.campustablebeclone.domain.auth.service.SejongPortalLoginService;
import shop.campustable.campustablebeclone.domain.user.dto.UserRequest;
import shop.campustable.campustablebeclone.domain.user.dto.UserResponse;
import shop.campustable.campustablebeclone.domain.user.dto.UserUpdateRequest;
import shop.campustable.campustablebeclone.domain.user.entity.User;
import shop.campustable.campustablebeclone.domain.user.entity.UserRole;
import shop.campustable.campustablebeclone.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final SejongPortalLoginService sejongPortalLoginService;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;



  public List<UserResponse> getAllUsers() {
    List<User> users = userRepository.findAll();

    return users.stream().map(UserResponse::new).toList();

  }

  public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    UserResponse userResponse = new UserResponse(user);
    return userResponse;
  }

  public UserResponse updateUser(UserUpdateRequest request, Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    if (request.getRole() != null) {
      user.setRole(request.getRole());
    }
    UserResponse userResponse = new UserResponse(user);
    return userResponse;
  }

  public void deleteUser(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    userRepository.delete(user);
  }


}
