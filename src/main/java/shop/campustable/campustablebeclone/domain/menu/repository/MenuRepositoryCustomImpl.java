package shop.campustable.campustablebeclone.domain.menu.repository;

import static shop.campustable.campustablebeclone.domain.menu.entity.QMenu.menu;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;

@RequiredArgsConstructor
public class MenuRepositoryCustomImpl implements MenuRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Menu> findByIdWithCategory(Long id) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(menu)
            .join(menu.category).fetchJoin()
            .where(menu.id.eq(id))
            .fetchOne()
    );
  }

}
