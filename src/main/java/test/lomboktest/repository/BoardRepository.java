package test.lomboktest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.lomboktest.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
