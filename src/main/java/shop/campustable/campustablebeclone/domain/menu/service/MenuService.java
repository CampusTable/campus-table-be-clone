package shop.campustable.campustablebeclone.domain.menu.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuRequest;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuResponse;
import shop.campustable.campustablebeclone.domain.menu.repository.MenuRepository;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MenuService {

  private final MenuRepository menuRepository;

  public MenuResponse createMenu(MenuRequest request) {

    menuRepository.findByMenuName(request.getMenuName())
        .ifPresent(menu->{
          log.error("createMenu: menu가 이미 존재합니다. menuName: {}", menu.getMenuName());
          throw new CustomException(ErrorCode.MENU_ALREADY_EXISTS);
        });
    return null;
  }

}
