package shop.campustable.campustablebeclone.domain.menu.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.campustable.campustablebeclone.domain.category.entity.Category;
import shop.campustable.campustablebeclone.domain.category.repository.CategoryRepository;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuRequest;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuResponse;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;
import shop.campustable.campustablebeclone.domain.menu.repository.MenuRepository;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;
import shop.campustable.campustablebeclone.global.s3.service.S3Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MenuService {

  private final MenuRepository menuRepository;
  private final CategoryRepository categoryRepository;
  private final S3Service s3Service;

  @Value("${spring.cloud.aws.s3.domain}")
  private String s3Domain;

  private String getFullUrl(String menuUrl) {
    if (menuUrl == null || menuUrl.isBlank()) {
      return null;
    }
    String baseUrl = s3Domain.endsWith("/") ? s3Domain : s3Domain + "/";
    String path = menuUrl.startsWith("/") ? menuUrl.substring(1) : menuUrl;
    return baseUrl + path;
  }

  public MenuResponse createMenu(Long categoryId, MenuRequest request, MultipartFile image) {

    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> {
          log.error("createMenu: 유효하지 않은 categoryId {}", categoryId);
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });

    menuRepository.findByCategoryAndMenuName(category, request.getMenuName())
        .ifPresent(menu -> {
          log.error("createMenu: menu가 이미 존재합니다. menuName: {}", menu.getMenuName());
          throw new CustomException(ErrorCode.MENU_ALREADY_EXISTS);
        });

    String menuUrl = null;
    if (image != null && !image.isEmpty()) {
      menuUrl = uploadMenuImage(image, category.getCafeteria().getName());
    }

    Menu menu = request.toEntity(category, menuUrl);

    menuRepository.save(menu);
    return MenuResponse.from(menu, getFullUrl(menu.getMenuUrl()));
  }

  public List<MenuResponse> getAllMenus() {

    List<Menu> menus = menuRepository.findAll();
    List<MenuResponse> responses = menus.stream()
        .map(menu -> MenuResponse.from(menu, getFullUrl(menu.getMenuUrl())))
        .toList();
    return responses;

  }

  public List<MenuResponse> getMenusByCategory(Long categoryId) {

    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> {
          log.error("getMenusByCategory: 유효하지 않은 categoryId {}", categoryId);
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });

    List<Menu> menus = menuRepository.findByCategory(category);

    return menus.stream()
        .map(menu-> MenuResponse.from(menu, getFullUrl(menu.getMenuUrl())))
        .toList();
  }

  public MenuResponse getMenuById(Long menuId) {

    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> {
          log.error("getMenuById: 해당 메뉴는 존재하지 않습니다. menuId: {}", menuId);
          return new CustomException(ErrorCode.MENU_NOT_FOUND);
        });

    return MenuResponse.from(menu, getFullUrl(menu.getMenuUrl()));
  }

  public MenuResponse updateMenu(Long menuId, MenuRequest request, MultipartFile image) {

    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> {
          log.error("updateMenu: 해당 메뉴는 존재 하지 않습니다. menuId: {}", menuId);
          return new CustomException(ErrorCode.MENU_NOT_FOUND);
        });

    if (request.getMenuName() != null && !request.getMenuName().isBlank()) {
      menuRepository.findByCategoryAndMenuName(menu.getCategory(), request.getMenuName())
          .ifPresent(existedMenu -> {
            if (!existedMenu.getId().equals(menu.getId())) {
              log.error("updateMenu: 이미 카테고리에 존재하는 메뉴 입니다. menuName: {}", existedMenu.getMenuName());
              throw new CustomException(ErrorCode.MENU_ALREADY_EXISTS);
            }
          });
    }

    if (image != null && !image.isEmpty()) {
      String newUrl = uploadMenuImage(image, menu.getCategory().getCafeteria().getName());

      String oldUrl = menu.getMenuUrl();
      menu.setMenuUrl(newUrl);

      if(oldUrl != null && !oldUrl.isBlank()) {
        try{
          s3Service.deleteFile(oldUrl);
        }catch(Exception e) {
          log.warn("updateMenu: 기존 이미지 삭제 실패 {}", oldUrl);
        }
      }

    }

    menu.update(request);

    return MenuResponse.from(menu,getFullUrl(menu.getMenuUrl()));

  }

  private String uploadMenuImage(MultipartFile image, String cafeteriaName) {
    String dirName = "menu/" + cafeteriaName;
    return s3Service.uploadFile(image, dirName);
  }

}
