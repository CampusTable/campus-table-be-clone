package shop.campustable.campustablebeclone.domain.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.campustable.campustablebeclone.domain.auth.repository.RefreshTokenRepository;
import shop.campustable.campustablebeclone.domain.user.dto.UserResponse;
import shop.campustable.campustablebeclone.domain.user.entity.User;
import shop.campustable.campustablebeclone.domain.user.repository.UserRepository;
import shop.campustable.campustablebeclone.global.common.SecurityUtil;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  @Transactional(readOnly = true)
  public UserResponse getMyInfo(){

    Long userId = SecurityUtil.getCurrentUserId();

    return UserResponse.from(findUserById(userId));
  }


  public void deleteMe(){

    Long userId = SecurityUtil.getCurrentUserId();
    User user = findUserById(userId);

    refreshTokenRepository.deleteByStudentId(user.getStudentId());

    userRepository.delete(user);
    log.info("회원 삭제 완료: 유저 ID {}", userId);
  }

  @Transactional(readOnly = true)
  public List<UserResponse> getAllUsers() {
    return userRepository.findAll()
        .stream()
        .map(UserResponse::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public UserResponse getUserById(Long userId) {
    return UserResponse.from(findUserById(userId));
  }


  public void deleteUser(Long userId) {

    User user = findUserById(userId);

    refreshTokenRepository.deleteByStudentId(user.getStudentId());

    userRepository.delete(user);
    log.info("관리자에 의한 회원 삭제 완료: 유저 ID {}", userId);
  }

  private User findUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(()->{
          log.error("유저를 찾을 수 없습니다. ID: {}", userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });
  }


}
