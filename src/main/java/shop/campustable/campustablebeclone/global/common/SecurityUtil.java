package shop.campustable.campustablebeclone.global.common;



import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import shop.campustable.campustablebeclone.domain.auth.security.CustomUserDetails;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Slf4j
public class SecurityUtil {

  private SecurityUtil() {}

  public static Long getCurrentUserId(){
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if(authentication == null || authentication.getName() == null ||authentication.getName().isBlank()){
      log.error("Security Context에 인증 정보가 없습니다.");
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    Object principal = authentication.getPrincipal();

    if(principal instanceof CustomUserDetails customUserDetails){
      return customUserDetails.getUser().getId();
    }

    log.error("인증 정보 타입이 유효하지 않습니다.");
    throw new CustomException(ErrorCode.JWT_INVALID);
  }

}
