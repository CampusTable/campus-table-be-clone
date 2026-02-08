package shop.campustable.campustablebeclone.domain.menu.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

  Optional<Menu> findByMenuName(String menuName);

}
