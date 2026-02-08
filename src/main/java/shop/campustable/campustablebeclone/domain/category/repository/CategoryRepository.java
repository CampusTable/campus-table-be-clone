package shop.campustable.campustablebeclone.domain.category.repository;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;
import shop.campustable.campustablebeclone.domain.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findByCafeteriaAndName(Cafeteria cafeteria, String name);

  List<Category> findByCafeteria(Cafeteria cafeteria);
}
