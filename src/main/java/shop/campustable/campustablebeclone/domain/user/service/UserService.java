package shop.campustable.campustablebeclone.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.campustable.campustablebeclone.domain.user.dto.UserRequest;
import shop.campustable.campustablebeclone.domain.user.dto.UserResponse;
import shop.campustable.campustablebeclone.domain.user.entity.User;
import shop.campustable.campustablebeclone.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserResponse createUser(UserRequest request) {
    User user = request.toEntity(request);
    userRepository.save(user);

    UserResponse userResponse = new UserResponse(user.getUserId(),user.getRole());
    return userResponse;

  }


}
