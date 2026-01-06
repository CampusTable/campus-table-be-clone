package shop.campustable.campustablebeclone.domain.user.repository;

import org.springframework.data.repository.CrudRepository;
import shop.campustable.campustablebeclone.domain.user.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
