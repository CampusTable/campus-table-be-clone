package shop.campustable.campustablebeclone.domain.menu.repository;

import java.lang.invoke.CallSite;
import java.util.List;
import java.util.Optional;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuSearchRequest;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;

public interface MenuRepositoryCustom {

  Optional<Menu> findByIdWithCategory(Long id);

  List<Menu> searchMenus (MenuSearchRequest request);

}
