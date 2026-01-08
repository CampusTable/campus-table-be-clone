package shop.campustable.campustablebeclone.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.campustable.campustablebeclone.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
