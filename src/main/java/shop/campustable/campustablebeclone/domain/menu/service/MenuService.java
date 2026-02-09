package shop.campustable.campustablebeclone.domain.menu.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.webmvc.core.service.RequestService;
import org.springframework.stereotype.Service;
import shop.campustable.campustablebeclone.domain.category.entity.Category;
import shop.campustable.campustablebeclone.domain.category.repository.CategoryRepository;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuRequest;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuResponse;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;
import shop.campustable.campustablebeclone.domain.menu.repository.MenuRepository;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MenuService {

  private final MenuRepository menuRepository;
  private final CategoryRepository categoryRepository;

  public MenuResponse createMenu(Long categoryId,MenuRequest request) {

    Category category = categoryRepository.findById(categoryId)
            .orElseThrow(()->{
              log.error("createMenu: 유효하지 않은 categoryId {}", categoryId);
              return  new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
            });

    menuRepository.findByCategoryAndMenuName(category,request.getMenuName())
        .ifPresent(menu -> {
          log.error("createMenu: menu가 이미 존재합니다. menuName: {}", menu.getMenuName());
          throw new CustomException(ErrorCode.MENU_ALREADY_EXISTS);
        });

    Menu menu = request.toEntity(category);
    menuRepository.save(menu);

    return MenuResponse.from(menu);
  }

  public List<MenuResponse> getAllMenus() {

    List<Menu> menus = menuRepository.findAll();
    List<MenuResponse> responses = menus.stream()
        .map(MenuResponse::from)
        .toList();
    return responses;

  }

  public List<MenuResponse> getMenusByCategory(Long categoryId) {

    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(()->{
          log.error("getMenusByCategory: 유효하지 않은 categoryId {}", categoryId);
          return  new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });

    List<Menu> menus = menuRepository.findByCategory(category);

    return  menus.stream()
        .map(MenuResponse::from)
        .toList();
  }

  public MenuResponse getMenuById(Long menuId) {

    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> {
          log.error("해당 메뉴는 존재하지 않습니다. menuId: {}", menuId);
          return new CustomException(ErrorCode.MENU_NOT_FOUND);
        });

    return MenuResponse.from(menu);
  }

  public MenuResponse updateMenu(Long menuId, MenuRequest request) {

    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> {
          log.error("해당 메뉴는 존재 하지 않습니다. menuId: {}", menuId);
          return new CustomException(ErrorCode.MENU_NOT_FOUND);
        });
    if (request.getMenuName() != null && !request.getMenuName().isBlank()) {
      menuRepository.findByCategoryAndMenuName(menu.getCategory(),request.getMenuName())
          .ifPresent(existedMenu -> {
            if(!existedMenu.getId().equals(menu.getId())) {
              log.error("updateMenu: 이미 카테고리에 존재하는 메뉴 입니다. menuName: {}", existedMenu.getMenuName());
              throw new CustomException(ErrorCode.MENU_ALREADY_EXISTS);
            }
          });
    }

    menu.update(request);

    return MenuResponse.from(menu);

  }

}
