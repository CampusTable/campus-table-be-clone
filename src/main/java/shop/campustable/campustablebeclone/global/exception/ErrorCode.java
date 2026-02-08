package shop.campustable.campustablebeclone.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // GLOBAL

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),

  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),

  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력 값이 유효하지 않거나 형식이 잘못되었습니다."),

  // AUTH

  JWT_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다. 다시 로그인해 주세요."),

  ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,"액세스 토큰이 만료되었습니다. 재발급이 필요합니다."),

  REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "리프레시토큰이 만료되었습니다. 다시 로그인해 주세요."),

  // USER
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),

  // CAFETERIA

  CAFETERIA_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 식당 정보를 찾을 수 없습니다."),

  CAFETERIA_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 식당입니다."),

  INVALID_OPERATING_HOURS(HttpStatus.BAD_REQUEST, "운영시간 목록은 비어있을 수 없습니다."),
  
  // Menu
  MENU_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 메뉴 이름입니다"),

  MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String message;

}
