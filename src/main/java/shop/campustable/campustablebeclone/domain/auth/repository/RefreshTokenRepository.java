package shop.campustable.campustablebeclone.domain.auth.repository;


import org.springframework.data.repository.CrudRepository;
import shop.campustable.campustablebeclone.domain.auth.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

  void deleteByStudentId(Long studentId);

}
