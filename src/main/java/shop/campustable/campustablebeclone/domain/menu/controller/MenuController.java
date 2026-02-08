package shop.campustable.campustablebeclone.domain.menu.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuRequest;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuResponse;
import shop.campustable.campustablebeclone.domain.menu.service.MenuService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController implements MenuControllerDocs {

  private final MenuService menuService;

  @Override
  @PostMapping("/admin/menus")
  public ResponseEntity<MenuResponse> createMenu(@RequestBody MenuRequest request) {

    MenuResponse response = menuService.createMenu(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);

  }

  @Override
  @GetMapping("/menus")
  public ResponseEntity<List<MenuResponse>> getAllMenu() {

    List<MenuResponse> responses = menuService.getAllMenus();
    return ResponseEntity.ok(responses);

  }

  @Override
  @GetMapping("/menus/{menu-id}")
  public ResponseEntity<MenuResponse> getMenu(@PathVariable("menu-id") Long menuId) {

    MenuResponse response = menuService.getMenuById(menuId);
    return ResponseEntity.ok(response);

  }

  @Override
  @PatchMapping("/admin/menus/{menu-id}")
  public ResponseEntity<MenuResponse> updateMenu(@PathVariable("menu-id") Long menuId, @RequestBody MenuRequest request) {
    MenuResponse response = menuService.updateMenu(menuId, request);
    return ResponseEntity.ok(response);
  }

}
