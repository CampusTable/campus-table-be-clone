package shop.campustable.campustablebeclone.domain.menu.repository;

import static shop.campustable.campustablebeclone.domain.menu.entity.QMenu.menu;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import shop.campustable.campustablebeclone.domain.menu.dto.MenuSearchRequest;
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

  @Override
  public List<Menu> searchMenus(MenuSearchRequest request) {
    return queryFactory.
        selectFrom(menu)
        .leftJoin(menu.category).fetchJoin()
        .where(
            menuNameContains(request.getMenuName()),
            categoryIdEq(request.getCategoryId())
        )
        .orderBy(menuSort(request.getSort()))
        .fetch();
  }

  private BooleanExpression menuNameContains(String menuName) {
    return StringUtils.hasText(menuName) ? menu.menuName.contains(menuName) : null;
  }

  private BooleanExpression categoryIdEq(Long categoryId) {
    return categoryId != null ? menu.category.id.eq(categoryId) : null;
  }

  private OrderSpecifier<?> menuSort(String sort) {
    if (sort == null) {
      return menu.id.desc();
    }
    return switch (sort) {
      case "price_asc" -> menu.price.asc();
      case "price_desc" -> menu.price.desc();
      case "newest" -> menu.createdAt.desc();
      default -> menu.id.desc();
    };
  }

}
