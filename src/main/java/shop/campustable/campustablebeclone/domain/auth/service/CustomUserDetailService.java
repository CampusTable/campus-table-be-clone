package shop.campustable.campustablebeclone.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shop.campustable.campustablebeclone.domain.user.entity.User;
import shop.campustable.campustablebeclone.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String studentIdString) throws UsernameNotFoundException{

    Long studentId = Long.parseLong(studentIdString);

    User user = userRepository.findByStudentId(studentId)
        .orElseThrow(()->new UsernameNotFoundException("해당 학생을 찾을 수 없습니다."+ studentId));

    return org.springframework.security.core.userdetails.User.builder()
        .username(String.valueOf(user.getStudentId()))
        .password(user.getPassword())
        .roles(user.getRole().name())
        .build();

  }


}
