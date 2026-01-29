package shop.campustable.campustablebeclone.domain.auth.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.campustable.campustablebeclone.domain.auth.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByToken(String token);

  void deleteByStudentId(Long studentId);

}
