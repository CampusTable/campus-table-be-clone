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

  ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다. 재발급이 필요합니다."),

  REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "리프레시토큰이 만료되었습니다. 다시 로그인해 주세요."),

  // USER
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),

  // CAFETERIA

  CAFETERIA_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 식당 정보를 찾을 수 없습니다."),

  CAFETERIA_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 식당입니다."),

  INVALID_OPERATING_HOURS(HttpStatus.BAD_REQUEST, "운영시간 목록은 비어있을 수 없습니다."),

  // Menu
  MENU_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 메뉴 이름입니다"),

  MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다."),

  MENU_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "메뉴의 재고가 부족합니다."),

  //Category

  CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),

  CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다."),

  //S3
  INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "이미지 파일(.jpg, .jpeg, .png, .webp)만 업로드 가능합니다."),

  INVALID_FILE_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 파일 요청입니다."),

  S3_UPLOAD_AMAZON_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 클라이언트 에러로 인해 파일 업로드에 실패했습니다."),

  S3_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일 업로드 중 오류 발생"),

  S3_DELETE_AMAZON_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 서비스 에러로 인해 파일 삭제에 실패했습니다."),

  S3_DELETE_AMAZON_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 클라이언트 에러로 인해 파일 삭제에 실패했습니다."),

  S3_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일 삭제 중 오류 발생"),

  //CART
  CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니를 찾을 수 없습니다."),

  CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니 항목을 찾을 수 없습니다."),

  CART_MIXED_CAFETERIA(HttpStatus.BAD_REQUEST, "같은 식당의 메뉴만 장바구니에 담을 수 있습니다."),

  CART_ITEM_QUANTITY_LIMIT(HttpStatus.BAD_REQUEST, "한 메뉴당 최대 9개까지만 담을 수 있습니다."),

  //ORDER
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."),

  CANNOT_CANCEL_ORDER(HttpStatus.CONFLICT, "이미 조리가 시작되어 주문을 취소할수 없습니다."),

  INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "올바르지 않은 주문 상태 변경입니다."),

  ORDER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문 내에 요청하신 카테고리의 메뉴가 존재하지 않습니다.");

  private final HttpStatus status;
  private final String message;

}
