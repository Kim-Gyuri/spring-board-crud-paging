package test.lomboktest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.lomboktest.entities.Board;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

}
