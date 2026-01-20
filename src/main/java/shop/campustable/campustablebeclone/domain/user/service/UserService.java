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

  public TokenResponse login(UserRequest request) {

    SejongMemberInfo sejongMemberInfo = sejongPortalLoginService.getMemberAuthInfos(
        String.valueOf(request.getStudentId()),
        request.getPassword()
    );

    User user = userRepository.findByStudentId(request.getStudentId())
        .orElseGet(() -> userRepository.save(User.builder()
            .studentId(request.getStudentId())
            .password(request.getPassword())
            .role(UserRole.USER)
            .name(sejongMemberInfo.getName())
            .build()));

    String accessToken = jwtTokenProvider.createAccessToken(user.getStudentId(), user.getRole().name());
    String refreshToken = jwtTokenProvider.createRefreshToken(user.getStudentId());

    refreshTokenRepository.findById(user.getStudentId())
        .ifPresentOrElse(token -> token.updateToken(refreshToken),
            () -> refreshTokenRepository.save(RefreshToken.builder()
                .studentId(user.getStudentId())
                .token(refreshToken)
                .build())
        );

    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

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
