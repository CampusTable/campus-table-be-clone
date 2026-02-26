package shop.campustable.campustablebeclone.domain.menu.repository;

import java.util.Optional;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;

public interface MenuRepositoryCustom {

  Optional<Menu> findByIdWithCategory(Long id);

}
