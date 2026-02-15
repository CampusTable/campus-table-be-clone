package shop.campustable.campustablebeclone.domain.auth.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {

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
            .role(UserRole.USER)
            .name(sejongMemberInfo.getName())
            .build()));

    String jti = UUID.randomUUID().toString();
    String accessToken = jwtTokenProvider.createAccessToken(request.getStudentId(), user.getRole().name(), jti);
    String refreshToken = jwtTokenProvider.createRefreshToken(request.getStudentId(), jti);

    refreshTokenRepository.save(RefreshToken.builder()
        .jti(jti)
        .studentId(user.getStudentId())
        .token(refreshToken)
        .expiration(jwtTokenProvider.getRefreshInMs() / 1000) // TTL
        .build());

    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .studentId(user.getStudentId())
        .studentName(user.getName())
        .build();

  }

  public TokenResponse reissue(String refreshToken) {

    try {
      jwtTokenProvider.validateToken(refreshToken);
    } catch (Exception e) {
      log.error("reissue: 유효하지 않은 Refresh Token입니다.");
      throw new CustomException(ErrorCode.JWT_INVALID);
    }

    String jti = jwtTokenProvider.getJti(refreshToken);

    RefreshToken storedToken = refreshTokenRepository.findById(jti)
        .orElseThrow(() -> {
          log.error("reissue: 로그아웃 된 사용자 입니다.");
          return new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        });

    if (!storedToken.getToken().equals(refreshToken)) {
      log.error("reissue: DB의 토큰 정보와 일치하지 않는 토큰입니다.");
      throw new CustomException(ErrorCode.JWT_INVALID);
    }

    User user = userRepository.findByStudentId(storedToken.getStudentId())
        .orElseThrow(() -> {
          log.error("reissue: 유효하지 않은 User {}", storedToken.getStudentId());
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    refreshTokenRepository.delete(storedToken);

    String newJti = UUID.randomUUID().toString();
    String newAccessToken = jwtTokenProvider.createAccessToken(user.getStudentId(), user.getRole().name(), newJti);
    String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getStudentId(), newJti);

    refreshTokenRepository.save(RefreshToken.builder()
        .jti(newJti)
        .studentId(user.getStudentId())
        .token(newRefreshToken)
        .expiration(jwtTokenProvider.getRefreshInMs() / 1000)
        .build());

    return TokenResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .studentName(user.getName())
        .studentId(user.getStudentId())
        .build();
  }

}
