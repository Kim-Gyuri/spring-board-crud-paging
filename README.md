# spring-board-crud-paging
웹 게시판 --> 기본적인 crud 기능과 페이징처리 만들기

*들어갈 내용 스케치*
1) 만났던 오류(주소가 길어서 메모장에 기록, 이건 요약)
-[Spring in Action] H2 database 테이블 생성 안되는 오류
- org.hibernate.MappingException
- AnnotationException: No identifier specified for entity
- 스프링 시큐리티, 왜 로그인 페이지 구현을 안해도 로그인 페이지로 이동되는지
- builder랑 @ModelAttribute를 같이 쓰면 null 예외가 터진다.
- @Builder를 올바르게 쓰는 법
- 500 에러란
- [SpringBoot + JPA] Lombok @Builder 빌더패턴 적용기
- Parameter 0 of constructor in required a bean of type 'java.lang.String' that could not be found
- 페이징 만들기, -->특히 타임리프 만들기
-  intstream을 올바르게 사용하는 법
- th:action 경로, -->서버설정 없을 땐 경로를 어떻게 설정해야 하는지


2) 추가할 것
- 등록시 필수입력조건을 추가한다.
- 검색기능 추가

3) 나중에 읽어보고 싶은 자료
- https://ivvve.github.io/2019/01/09/java/Spring/pagination_1/  ---> 스프링 부트, JPA, Thymeleaf를 이용한 페이징 처리 
- https://github.com/eugenp/tutorials/blob/master/spring-boot-modules/spring-boot-crud/src/main/java/com/baeldung/crud/controllers/UserController.java   -->스프링 부트 튜토리얼 살펴보기 딱 좋다.
- https://www.baeldung.com/intro-to-project-lombok     --->롬복 공부, 공식문서

5) 기타
- 다음에 만들고자 하는 프로젝트
  - 연관관계가 잘 보이도록
  -  SQL 연동
  -  security를 적용했는데 session을 적용해야 하나?
  -  공격자가 API를 변조하여 공격이 가능한지?
  -  디자인, TDD, 개발 스타일, 브랜치 전략, 이슈 관리, 배포 방식 등 시행착오?
  -  https://github.com/roadbook2/shop 쇼핑몰 만들기?
  -  aws
