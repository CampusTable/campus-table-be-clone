package shop.campustable.campustablebeclone.domain.user.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.campustable.campustablebeclone.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByStudentId(Long studentId);
}
