package shop.campustable.campustablebeclone.domain.cart.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {

  @NotNull(message = "메뉴 ID는 필수 입니다.")
  private Long menuId;

  @NotNull
  @Min(value = 0, message = "수량은 최소 0개 이상이어야 합니다.")
  @Max(value = 9, message = "수량은 최대 9개까지만 담을 수 있습니다.")
  private Integer quantity;

}
