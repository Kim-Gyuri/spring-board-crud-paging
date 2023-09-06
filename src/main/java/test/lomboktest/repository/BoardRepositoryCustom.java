package test.lomboktest.repository;

import test.lomboktest.controller.dto.getPostResponse;

import java.util.List;

public interface BoardRepositoryCustom {
    List<getPostResponse> sortByBoardType(String type);
}
