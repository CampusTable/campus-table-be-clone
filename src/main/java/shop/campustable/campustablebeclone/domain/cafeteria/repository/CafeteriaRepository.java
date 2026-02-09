package shop.campustable.campustablebeclone.domain.cafeteria.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;

public interface CafeteriaRepository extends JpaRepository<Cafeteria, Long> {

  Optional<Cafeteria> findByName(String name);
}
