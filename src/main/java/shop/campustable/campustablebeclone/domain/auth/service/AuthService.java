package shop.campustable.campustablebeclone.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.campustable.campustablebeclone.domain.auth.dto.SejongMemberInfo;
import shop.campustable.campustablebeclone.domain.auth.dto.TokenResponse;
import shop.campustable.campustablebeclone.domain.auth.entity.RefreshToken;
import shop.campustable.campustablebeclone.domain.auth.provider.JwtTokenProvider;
import shop.campustable.campustablebeclone.domain.auth.repository.RefreshTokenRepository;
import shop.campustable.campustablebeclone.domain.user.dto.UserRequest;
import shop.campustable.campustablebeclone.domain.user.entity.User;
import shop.campustable.campustablebeclone.domain.user.entity.UserRole;
import shop.campustable.campustablebeclone.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

  private final UserRepository userRepository;
  private final SejongPortalLoginService sejongPortalLoginService;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  public TokenResponse login(UserRequest request) {

    SejongMemberInfo sejongMemberInfo =sejongPortalLoginService.getMemberAuthInfos(
        String.valueOf(request.getStudentId()),
        request.getPassword()
    );

    User user = userRepository.findByStudentId(request.getStudentId())
        .orElseGet(() -> userRepository.save(User.builder()
            .studentId(request.getStudentId())
            .role(UserRole.USER)
            .name(sejongMemberInfo.getName())
            .build()));

    String accessToken = jwtTokenProvider.createAccessToken(request.getStudentId(), user.getRole().name());
    String refreshToken = jwtTokenProvider.createRefreshToken(request.getStudentId());

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
        .studentId(user.getStudentId())
        .studentName(user.getName())
        .build();

  }

  public TokenResponse reissue(String refreshToken) {
    if (!jwtTokenProvider.validateToken(refreshToken)) {
      throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
    }

    Long studentId = jwtTokenProvider.getStudentId(refreshToken);

    RefreshToken storedToken = refreshTokenRepository.findById(studentId)
        .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

    if (!storedToken.getToken().equals(refreshToken)) {
      throw new RuntimeException("토큰 정보가 일치하지 않습니다.");
    }

    User user = userRepository.findByStudentId(studentId)
        .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

    String newAccessToken = jwtTokenProvider.createAccessToken(user.getStudentId(), user.getRole().name());
    String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getStudentId());

    storedToken.updateToken(newRefreshToken);

    return TokenResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .studentName(user.getName())
        .studentId(studentId)
        .build();
  }

}
