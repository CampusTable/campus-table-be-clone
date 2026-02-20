package shop.campustable.campustablebeclone.domain.menu.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.campustable.campustablebeclone.domain.category.entity.Category;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuResponse;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

  Optional<Menu> findByCategoryAndMenuName(Category category, String menuName);

  List<Menu> findByCategory(Category category);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select m from Menu m where m.id =:id")
  Optional<Menu> findByIdForUpdate(@Param("id") Long id);

  @Query("SELECT new shop.campustable.campustablebeclone.domain.menu.dto.MenuResponse("+
         "m.id,"+
         "m.category.id,"+
         "m.menuName,"+
         "m.price,"+
         "m.menuUrl,"+
         "m.available,"+
         "m.stockQuantity,"+
         "m.createdAt,"+
         "m.updatedAt)"+
        "FROM Menu m")
  List<MenuResponse> findAllMenuResponses();
}
