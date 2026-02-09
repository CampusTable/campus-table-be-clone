package shop.campustable.campustablebeclone.domain.menu.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.campustable.campustablebeclone.domain.category.entity.Category;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

  Optional<Menu> findByCategoryAndMenuName(Category category,String menuName);

  List<Menu> findByCategory(Category category);
}
