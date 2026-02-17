package shop.campustable.campustablebeclone.domain.auth.service;

import java.util.Collections;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
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
@Transactional
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final SejongPortalLoginService sejongPortalLoginService;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;
  private final StringRedisTemplate stringRedisTemplate;

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

    String accessJti = UUID.randomUUID().toString();
    String refreshJti = UUID.randomUUID().toString();
    String accessToken = jwtTokenProvider.createAccessToken(request.getStudentId(), user.getRole().name(), accessJti);
    String refreshToken = jwtTokenProvider.createRefreshToken(request.getStudentId(), refreshJti);

    refreshTokenRepository.save(RefreshToken.builder()
        .jti(refreshJti)
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
      log.error("reissue: 유효하지 않은 Refresh Token입니다. {}", e.getMessage());
      throw new CustomException(ErrorCode.JWT_INVALID);
    }

    String jti = jwtTokenProvider.getJti(refreshToken);
    String redisKey = "refreshToken:" + jti;

    // KEYS[1]: 메인데이터 키 "refreshToken:{jti}"
    // ARGV[1]: 비교할 토큰 값
    // ARGV[2]: 삭제할 JTI 값 (인덱스에서 지우기 위해 필요)
    // 1: 성공 (삭제됨)
    // -1: 키가 없음 (이미 지워짐/로그아웃/만료)
    // -2: 키는 있는데 토큰 값이 다름 (재사용 탐지/해킹 위험)
    String script =
        "local val = redis.call('HGET', KEYS[1], 'token') " +
        "if not val then return -1 end " + // 1. 아예 없으면 -1 (만료/로그아웃)
        "if val == ARGV[1] then " +
        "    local studentId = redis.call('HGET', KEYS[1], 'studentId') " +
        "    if studentId then " +
        "        redis.call('SREM', 'refreshToken:studentId:' .. studentId, ARGV[2]) " + // 2. 보조 인덱스 정리
        "    end " +
        "    return redis.call('DEL', KEYS[1]) " + // 3. 메인 데이터 삭제
        "else " +
        "    return -2 " + // 4. 값은 있는데 다르면 -2 (재사용 탐지)
        "end";

    Long result = stringRedisTemplate.execute(
        new DefaultRedisScript<>(script, Long.class),
        Collections.singletonList(redisKey),
        refreshToken,
        jti
    );

    if (result == null || result.equals(-1L)) {
      log.error("reissue: 이미 로그아웃 되었거나 만료된 세션입니다. jti={}", jti);
      throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }

    if (result.equals(-2L)) {
      log.warn("reissue: 토큰 재사용 탐지. studentId={} 의 모든 세션을 무효화합니다.", jwtTokenProvider.getStudentId(refreshToken));
      refreshTokenRepository.deleteByStudentId(jwtTokenProvider.getStudentId(refreshToken));
      throw new CustomException(ErrorCode.JWT_INVALID);
    }

    Long studentId = jwtTokenProvider.getStudentId(refreshToken);

    User user = userRepository.findByStudentId(studentId)
        .orElseThrow(() -> {
          log.error("reissue: 유효하지 않은 User {}", studentId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    String newAccessJti = UUID.randomUUID().toString();
    String newRefreshJti = UUID.randomUUID().toString();
    String newAccessToken = jwtTokenProvider.createAccessToken(user.getStudentId(), user.getRole().name(), newAccessJti);
    String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getStudentId(), newRefreshJti);

    refreshTokenRepository.save(RefreshToken.builder()
        .jti(newRefreshJti)
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
