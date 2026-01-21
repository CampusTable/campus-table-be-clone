package shop.campustable.campustablebeclone.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shop.campustable.campustablebeclone.domain.auth.security.CustomUserDetails;
import shop.campustable.campustablebeclone.domain.user.entity.User;
import shop.campustable.campustablebeclone.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String studentIdString) throws UsernameNotFoundException{

    Long studentId;
    try{
      studentId = Long.parseLong(studentIdString);
    } catch (NumberFormatException e){
      throw new UsernameNotFoundException("잘못된 학번 형식입니다: "+ studentIdString);
    }

    return userRepository.findByStudentId(studentId)
        .map(CustomUserDetails::new)
        .orElseThrow(()->new UsernameNotFoundException("해당학번을 찾을수 없습니다.: "+studentIdString));

  }


}
