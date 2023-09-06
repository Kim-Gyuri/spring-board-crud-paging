package test.lomboktest.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.StringUtils;
import test.lomboktest.controller.dto.QgetPostResponse;
import test.lomboktest.entities.enums.BoardType;
import test.lomboktest.controller.dto.getPostResponse;

import javax.persistence.EntityManager;
import java.util.List;

import static test.lomboktest.entities.QBoard.board;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<getPostResponse> sortByBoardType(String type) {
        List<getPostResponse> content = queryFactory
                .select(new QgetPostResponse(
                        board.id.as("itemId"),
                        board.title,
                        board.content,
                        board.boardType))
                .from(board)
                .where(boardTypeContains(type))
                .fetch();
        return content;
    }

    private static BooleanExpression boardTypeContains(String type) {
        return StringUtils.hasText(type) ? board.boardType.eq(BoardType.enumOf(type)) : null;
    }
}
