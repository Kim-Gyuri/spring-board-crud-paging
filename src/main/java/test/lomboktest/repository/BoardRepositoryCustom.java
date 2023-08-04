package test.lomboktest.repository;

import test.lomboktest.web.dto.MainPostDto;

import java.util.List;

public interface BoardRepositoryCustom {
    List<MainPostDto> sortByBoardType(String type);
}
