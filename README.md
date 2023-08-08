스프링 부트와 JPA, Thymeleaf를 사용하여 CRUD 웹 애플리케이션을 설계하고 개발해보고, <br>
REST 규칙을 생각하며 API 개발도 해보았다. <br><br>
> 공부했던 스프링에 대해 내용 정리해보기 위해 간단한 프로젝트를 만들어보았다. <br>
> 요구사항부터 설계, 구현, 만났던 오류, 참고자료 등등 자세하게 <br>
> 프로젝트를 만들면서 Spring Boot에서 "무슨 역할을 하기 위해 코드를 넣었는지?" <br>
> "무엇을 해주고 있는 코드인지?", "가독성을 높이기 위해 어떻게 작성해야 할지?" 등등 생각했던 내용을 기록했다.

# 📌목차
+ 프로젝트 생성
+ 도메인 계층 구현
  + 요구사항 분석
  + 도메인 모델 분석
  +  테이블 설계
  +  엔티티 설계와 매핑
+ 리포지토리 계층 구현
+ 서비스 계층 구현
+ 웹 계층 개발
  + 컨트롤러
  + 뷰
+  실행 화면
+  API 설계
+  테스트 코드
+  만났던 오류
+ 회고

## 1.  프로젝트 생성
스프링 개발시 dependencies 추가를 할 때, Maven이나 Gradle을 사용하여 버전에 맞게 필요한 라이브러리를 추가해야 한다. <br>
스프링 스타터를 통해 프로젝트를 만드는데, 다음과 같이 버전 및 플러그인을 구성한다. <br>
Gradle Project, Spring Boot version 2.7.11, Jar, Java 11으로 선택하고 <br> 
JPA, Thymeleaf, Spring Web, Lombok, H2 라이브러리를 추가했다.

## 2. 도메인 계층 구현
JPA를 사용하는데 가장 중요한 일은 엔티티와 테이블을 정확히 매핑하는 것이다. <br>
객체와 테이블 매핑, 기본 키 매핑, 필드와 컬럼 매핑에 대해 알아보고 연관관계 매핑을 한다. <br><br>

### 요구사항 분석
핵심 요구사항은 다음과 같다. <br>
+ 게시물을 작성할 수 있다.
+ 게시물 등록 시 게시글 분류(자유 게시판, 필독/공지)를 선택할 수 있다.
+ 게시물 등록/조회/수정/삭제할 수 있다.

> (회원 엔티티를 설계했지만, 관련 기능들은 나중에 추가하려고 한다.)

이후에 추가하고 싶은 요구사항은 다음과 같다.
+ 모든 게시물은 등록일과 수정일이 있어야 한다.
+ 로그인한 회원만 게시물을 작성할 수 있다.
+ 본인 작성 글에 대한 권한 관리를 갖는다. (다른 회원이 수정할 수 없다.)

### 도메인 모델 분석
요구사항을 분석해보니 회원, 게시물 엔티티가 도출되었다.
+ 회원과 게시물 관계  <br> 회원은 여러 개의 게시물을 작성할 수 있으므로 게시물과 회원은 다대일 관계다.
> Board -> User 방향으로 참조하는 board.user 필드만 사용해서 다대일 단방향 관계로 설정했다. <br>
> (반대로 user에서는 board를 참조하는 필드가 없어서, user에서는 board를 조회할 수도 없다.) <br>

> 요구사항 : `user 계정당 게시물 관리권한 구현`이 필요할 때 <br>
> 게시물과 회원을 다대일 양방향 관계로 설계하여, <br>
> (주인이 아닌 쪽도 조회 가능하도록) 주인이 아닌 user.board는 조회를 위한 JPQL이나 객체 그래프를 탐색할 때 사용한다. 

<br>

### 테이블 설계
요구사항을 기반으로 설계한 테이블 ERD다. <br>
![oo](https://github.com/Kim-Gyuri/spring-board-crud-paging/assets/57389368/4bdfcf86-a718-49ea-ac7c-ea9a669fc1e0)
> ERD는 [diagrams - 온라인 편집기](https://app.diagrams.net/)로 그렸다.

+ 회원(USER) : 이름(NAME)과 로그인 정보(이메일, 비밀번호)를 가진다.
+ 게시물(BOARD) : 게시물 제목(TITLE), 내용(CONTENT), 게시글 분류(BOARD TYPE), 작성한 날짜(UPDATED DATE)를 가진다. <br> 게시물 수정 시 제목,내용을 바꿀 수 있다. 게시물을 수정하면 게시물 작성시간도 업데이트된다.

### 엔티티 설계와 매핑
설계한 테이블을 기반으로 실제 엔티티를 설계하자. <br>
+ [Board 엔티티 코드](https://github.com/Kim-Gyuri/spring-board-crud-paging/blob/master/src/main/java/test/lomboktest/entities/Board.java) 
+ [User 엔티티 코드](https://github.com/Kim-Gyuri/spring-board-crud-paging/blob/master/src/main/java/test/lomboktest/entities/User.java) 

<br>

+ 식별자는 @Id와 @GeneratedValue를 사용해서 DB에서 자동 생성되도록 했다. 
> @GeneratedValue의 기본 생성전략은 AUTO이므로 선택한 DB 방언에 따라 IDENTITY, SEQUANCE, TABLE 중 하나가 선택된다.  <br>
> 나는 H2 DB를 사용하는데, 이 DB는 SEQUANCE를 사용한다. <br>
> 다른 entity들에 대해서는 같은 키 생성전략을 사용한다.

<br>

+ 게시물(BOARD)에 필요한 등록날짜는 LocalDateTime을 사용했다.
> LocalDateTime 클래스는 타임존 개념이 필요없는 날짜와 시간 정보 모두를 나태나기 위해서 사용된다. <br>
> 즉, 간단하게 LocalDate와 LocalTime을 합쳐놨다고 보시면 된다고 한다. <br>
> Java 8 이전에는 Date를 사용했다. <br>

> @Temporal을 생략하면 @Temporal(TemporalType.TIMESTAMP)와 같으므로 예제에서는 생략해도 좋다. <br>
> @Temporal(TemporalType.TIMESTAMP) 경우, 2023-08-03 20:21 처럼 매핑된다.

<br>

+ 게시글 분류(BOARDTYPE)은 열거형을 사용하므로 @Enumerated로 매핑했다. <br> `EnumType.String 속성`을 지정해서 열거형의 이름이 그대로 저장되도록 했다. <br>
그리고 열거형을 사용하므로  notice("공지사항"),  free("자유게시판")을 표현할 수 있다.

<br>

+ 게시물(BOARD)는 회원의 외래 키 값을 가진다. (게시물과 회원은 다대일 관계이므로)

<br>

+ setter를 만들기 보다는 의미있는 변경 메서드 이름을 사용한다. <br> 
게시물을 수정해야 하니까 update()라는 이름의 메스드를 만든다.  board.update()를 호출하여 필드 값을 변경한다.
```java
    public void update(String title, String content, LocalDateTime updatedDate) {
        this.title = title;
        this.content = content;
        this.updatedDate = updatedDate;
    }
```

## 3. 리포지토리 계층 구현
![사용자 정의 리포지토리 구현](https://github.com/Kim-Gyuri/spring-board-crud-paging/assets/57389368/5676156b-0a5f-484c-ac0d-38ba5796bb0c) <br>

해당 Board, User 클래스로  DB를 접근하게 해줄 JpaRepository를 생성한다. <br> 
인터페이스를 생성한 후, JpaRepository<Entity 클래스, PK타입>을 상속하면 기본적인 CRUD 메소드가 자동으로 생성된다. <br>
그리고 사용자 정의 인터페이스(BoardRepositoryCustom)를 만들어 따로 필요한 쿼리를 작성했다.<br><br>
boardRepository.sortByBoardType(type)을 만들 때, 쿼리 작성을 QueryDsl로 간단하게 만들었다. <br>
```java
    @Override
    public List<MainPostDto> sortByBoardType(String type) {
        List<MainPostDto> content = queryFactory
                .select(new QMainPostDto(
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
```
#### BooleanExpression을 사용하여 리팩토링
> BooleanExpression을 사용하여 boardTypeContains()메서드를 만들었다. (좀 더 직관적으로 볼 수 있도록 리팩토링할 수 있음) <br>
> BooleanExpression을 리턴하는데, 메소드에서 조건이 맞지 않으면 null을 리턴한다. (where에서는 상황에 따라 조건문을 생성하게 된다.)


## 4. 서비스 계층 구현
컨트롤러에서 서비스를 호출하여 데이터를 처리하려고 한다. 컨트롤러나 타임리프에 필요한 데이터를 DTO로 변환하였다. <br>
엔티티를 직접 사용하지 않고 필요한 필드만 가져올 수 있게 DTO로 반환하도록 코드를 작성했다.<br> <br>
구현한 로직은 아래와 같다. [BoardService 코드](https://github.com/Kim-Gyuri/spring-board-crud-paging/blob/master/src/main/java/test/lomboktest/service/BoardService.java)
+ Long save(BoardForm boardForm) : 엔티티를 등록한다.  MVC 확인용
+ Long save(CreatePostRequest request) : 엔티티를 등록한다. API 확인용
+ void delete(Long id) : 엔티티를 삭제한다.
+ Long update(Long id, BoardForm boardForm) : 엔티티 부분 수정, MVC 확인용
+ Long update(Long id, UpdatePostRequest request) : 엔티티 부분 수정, API 확인용
+ Board findById(Long id) : 엔티티 단건 조회
+ Page<Board> findBoardList(Pageable pageable) : 페이징
+ List<MainPostDto> findBoardList() : 엔티티 전체조회, API 확인용
+ List<MainPostDto> findByBoardType(String type) : 엔티티 타입별(자유게시판, 공지) 정렬조회, API 확인용

 <br> 
 
> 코드의 가독성을 위해 stream, orElseThrow를 사용하였다. 
### stream을 사용한 코드작성
코드 가독성을 높이기 위해  stream을 사용하여 작성하려고 했다. <br> boardRepository.findAll() 경우에 List<Board> 엔티티 리스트로 반환된다.  <br>
여기서 stream()을 사용해서 Board를 DTO로 변환하고 컬렉션으로 반환했다. <br>
```java
    @Transactional(readOnly = true)
    public List<MainPostDto> findBoardList() {
        List<MainPostDto> collect = boardRepository.findAll().stream()
                .map(o -> new MainPostDto(o.getId(), o.getTitle(), o.getContent(), o.getBoardType()))
                .collect(Collectors.toList());
        return collect;
    }
```

### orElseThrow를 사용한 코드작성
코드 가독성을 높이기 위해 사용했다. <br> 해당 Board 엔티티가 없다면, 예외처리를 하고 있다면 해당 객체를 반환하도록 했다.
```java
    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        boardRepository.delete(board);
    }
```

<br><br>

> 페이징 처리는 다음과 같다.

pageable 인터페이스를 사용해서 쉽게 페이징 데이터를 만들고 뷰로 넘겨주고, 타임리프에서 넘겨진 데이터를 페이징 처리하도록 설계했다. <br>
BoardService에서 Pageable 객체를 사용하여 원하는 페이징 처리를 간단한 연산식 몇 개를 추가해서 끝냈다. <br>
```java
    @Transactional(readOnly = true)
    public Page<Board> findBoardList(Pageable pageable) {
        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber()-1,
                pageable.getPageSize());
        return boardRepository.findAll(pageable);
    }
```

페이징 객체를 사용해서 뷰 쪽에 구현할 기능은 다음과 같다. <br>
![페이징 버튼](https://github.com/Kim-Gyuri/spring-board-crud-paging/assets/57389368/c6cedd4c-8fb7-4677-9f41-6816cd5710fa) <br>
+ 맨 처음으로 이동 버튼
+ 이전 페이지로 이동 버튼 
+ 10 페이지 단위로 이동 버튼
+ 다음 페이지로 이동 버튼
+ 맨 마지막 페이지로 이동 버튼


## 5. 웹 계층 개발
### 컨트롤러
게시물 등록/수정/조회/삭제 매핑을 다음과 같이 설계했다. [BoardController 코드](https://github.com/Kim-Gyuri/spring-board-crud-paging/blob/master/src/main/java/test/lomboktest/controller/BoardController.java)
```
등록
등록폼 이동 :Get    /board/add
등록폼 전송 :Post   /board/add


수정
수정폼 이동 :Get    /board/edit/{id}
수정폼 전송 :Post   /board/edit/{id}


조회
단건 조회                    : Get    /board/{id}
전체 조회(메인페이지,페이징)  : Get    /board/home

삭제                         : Get    /board/delete/{id}
```

### 뷰
> 페이징에 대한 타임리프는 다음과 같이 작성했다.

+  th:with 구문을 사용하여 ul 태그 안에서 사용할 변수를 정의한다. <br> startNumber와 endNumber 변수로 페이지의 처음과 끝을 동적으로 계산하여 초기화한다. <br>
변수 계산 로직은 기본 10 페이지 단위로 처리한다. <br>
```html
<nav aria-label="Page navigation" style="text-align:center;">
    <ul class="pagination"
        th:with="startNumber=${T(Math).floor(boardList.number/10)}*10+1,
                endNumber=(${boardList.totalPages} > ${startNumber}+9) ? ${startNumber}+9 : ${boardList.totalPages}">
```



+ pageable 객체에는 편리하게도 해당 페이지가 처음인지(isFirst) 마지막인지(isLast)에 대한 정보(boolean 형)를 제공한다. <br> 
    이를 사용하여 이전/다음 페이지의 노출 여부를 결정한다.

```html
    <li th:style="${boardList.first} ? 'display:none'">
```
	


+ 각 페이지 버튼은 th:each를 사용하여 startNumber부터 endNumber까지 출력시킨다. <br>
pageable은 현재 페이지를 알려주는 number 객체가 0부터 시작한다. <br>
그래서 ${boardList.number}+1로 비교하여 현재 페이지 번호일 경우, class에 현재 페이지임을 보여주는 'active' 프로퍼티를 추가한다. <br>
```html
        <li th:each="page :${#numbers.sequence(startNumber, endNumber)}" th:class="(${page} == ${boardList.number}+1) ? 'active'">
```

<br>

> `참고` [처음 배우는 스프링 부트 2: 커뮤니티 게시판을 구현하며 배우는 입문부터 보안까지 - 책 자료](https://books.google.co.kr/books?id=jQhzDwAAQBAJ&pg=PA109&lpg=PA109&dq=pageable.getPageNumber()+%3C%3D+0+?&source=bl&ots=apXnG9-xh3&sig=ACfU3U0-GKbYrW0NKp1RkTDjM9WB6Xwp1A&hl=ko&sa=X&ved=2ahUKEwjiuoi2zMiAAxWS1GEKHZUmCOAQ6AF6BAgWEAM#v=onepage&q&f=false)

## 6. 실행 화면
`메인 페이지` <br> 
> `http://localhost:8080/board/home` url로 확인했을 때 다음과 같다. <br>
> 7를 누르면 `http://localhost:8080/board/home?page=7`로 url 쿼리문이 생긴다. <br>

![커뮤니티-메인페이지](https://github.com/Kim-Gyuri/spring-board-crud-paging/assets/57389368/55ef7c9e-2e34-44a8-acf2-50825b8e7785)

`게시물 등록 폼 이동했을 때` <br> <br>
![등록폼](https://github.com/Kim-Gyuri/spring-board-crud-paging/assets/57389368/954f0e1c-d7fd-4cae-99b5-3469eef53f75) 

`단건 조회` <br> <br>
![단건조회](https://github.com/Kim-Gyuri/spring-board-crud-paging/assets/57389368/7c9fa57d-eac2-4e5f-8d8d-c6a03557e24a)

`게시물 수정 폼 이동했을 때` <br><br>
![수정폼](https://github.com/Kim-Gyuri/spring-board-crud-paging/assets/57389368/254f3331-851b-47e1-b35f-873ac61b9a08)

`게시물 수정 완료했을 때` <br>
> 제목을 수정했을 때, 다음과 같이 변경된다.

![수정완료](https://github.com/Kim-Gyuri/spring-board-crud-paging/assets/57389368/28efa094-79a6-416b-9711-d815cbec58a3)


## 7. API 설계
REST 스타일에 맞추기 위해 아래처럼 사용했다. [BoardApiController 코드](https://github.com/Kim-Gyuri/spring-board-crud-paging/blob/master/src/main/java/test/lomboktest/web/BoardApiController.java) <br>
```
등록                    : Post         /api/post
수정                    : Patch        /api/post/{id}
단건 조회               : Get          /api/post/{id}
전체 조회               : Get          /api/post
분류타입별 조회          :Get          /api/post/category?type=FREE
삭제                    : Delete      /api/post/{id}
```

<br>

#### 등록
요청값을  DTO로 받는데, DTO를 RequestBody와 매핑했다. (엔티티와 API 스펙을 명확한게 분리할 수 있다.) <br>
```java
    @PostMapping
    public CreatePostResponse save(@RequestBody @Validated CreatePostRequest board) {
        Long id = boardService.save(board);
        return new CreatePostResponse(id);
    }
```
> postman으로 확인했을 때, 다음과 같다. <br>

![save](https://github.com/Kim-Gyuri/spring-board-crud-paging/assets/57389368/612d540b-09c8-452e-a7d6-344176960175) <br><br>


#### 조회
응답값으로 DTO을 반환한다. (DTO로 변환해서 반환하면, 엔티티가 변해도 API 스펙이 변경되지 않는다.) <br>
> `전체조회 API 경우` <br> Result 클래스로 컬렉션을 감싸서 반환했다. (향후 필요한 필드(예: 전체 크기)를 추가할 수 있는 구조다.) <br>
> { "data": [...], "count": 207} 이렇게 조회된다.
```java
    @GetMapping
    public Result findAll() {
        List<MainPostDto> collect = boardService.findBoardList();
        return new Result(collect, collect.size());
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
        private int count;
    }
```
> postman으로 확인했을 때, 다음과 같다. <br>

![findAll](https://github.com/Kim-Gyuri/spring-board-crud-paging/assets/57389368/69133fae-8242-45fa-a2e2-ad01e24d0ab3) <br><br><br><br>


#### 삭제
삭제했다는 확인메시지 DTO를 반환했다.
```java
    @DeleteMapping("/{id}")
    public DeletePostResponse delete(@PathVariable("id") Long id) {
        boardService.delete(id);
        return new DeletePostResponse(Boolean.TRUE, "success!");
    }
```
> postman으로 확인했을 때, 다음과 같다. <br>

![delete](https://github.com/Kim-Gyuri/spring-board-crud-paging/assets/57389368/700fcfb4-018f-4c1a-8345-8195cd5b71aa)

## 8. 테스트 코드
리포지토리, 서비스 테스트 코드를 통합 테스트로 작성했다. <br> 실제 product code라면 생성자 주입을 사용해야겠지만, 테스트 코드이므로 @Autowired를 사용해 필드 주입하였다. <br>
등록, 수정, 조회 기능을 테스트 했다.  <br>
+ [BoardRepositoryTest 코드](https://github.com/Kim-Gyuri/spring-board-crud-paging/blob/master/src/test/java/test/lomboktest/repository/BoardRepositoryTest.java)
+ [BoardServiceTest 코드](https://github.com/Kim-Gyuri/spring-board-crud-paging/blob/master/src/test/java/test/lomboktest/service/BoardServiceTest.java)

API Controller는 함수 호출이 아닌 API 호출을 통해 요청을 받고 응답을 처리해야 한다. <br> 그래서 메시지 컨버팅 등 작업이 필요하다. 그러므로 MockMvc 클래스를 이용하여 테스트 코드를 작성하였다. <br> MockMvc에 대한 @BeforeEach 세팅을 해주어야 하는데 @AutoConfigureMockMvc를 추가하면 된다. 
+ [BoardApiControllerTest 코드](https://github.com/Kim-Gyuri/spring-board-crud-paging/blob/master/src/test/java/test/lomboktest/web/BoardApiControllerTest.java)

## 9. 만났던 오류
### Querydsl No sources given 에러
코드를 작성 중에 No sources given 에러가 발생했었다. <br>
에러가 나는 소스를 보니 jpaQueryFactory 코드에 from() 코드가 없어 발생한 오류였다. <br>
from 절을 추가해서 문제를 해결했다. (성공한 쿼리문은 아래와 같다.) <br>
```
Hibernate: 
    select
        board0_.board_id as col_0_0_,
        board0_.title as col_1_0_,
        board0_.content as col_2_0_,
        board0_.board_type as col_3_0_ 
    from
        board board0_ 
    where
        board0_.board_type=?
```
> `참고` [Querydsl No sources given 에러 해결방법 - 블로그 글](https://wakestand.tistory.com/928)



### InvalidDefinitionException 에러 
InvalidDefinitionException 오류가 터졌는데, 에러 로그는 다음과 같다.
```
(no Creators, like default constructor, exist) :
 cannot deserialize from Object value (no delegate- or property-based Creator)
```

기본 생성자가 없어서 발생하는 에러였다.
포인트는 "JPA에서 관리되는 엔티티를 위해서는 기본 생성자가 필요하다."를 아는지 대한 오류같다.
DTO에 @NoArgsConstructor 어노테이션을 추가해서 해결했다.
(@NoArgsConstructor:  파라미터가 없는 기본 생성자를 생성해준다.)
> `참고` <br> [InvalidDefinitionException 에러 - 블로그 글](https://dhbang.tistory.com/57) <br>
> [Cannot construct instance of (no Creators, like default constructor, exist): cannot deserialize from Object value (no delegate- or property-based Creator) 해결방안 - 블로그 글](https://suyeoniii.tistory.com/99)

### BeanCreationException 에러
에러 로그는 다음과 같다.
```
org.springframework.beans.factory.BeanCreationException: 
Error creating bean with name 'test.lomboktest.web.BoardApiControllerTest':
Injection of autowired dependencies failed; nested exception is java.lang.IllegalArgumentException:
Could not resolve placeholder 'local.server.port' in value "${local.server.port}"

Caused by: java.lang.IllegalArgumentException: Could not resolve placeholder 'local.server.port' in value "${local.server.port}"
```

임의의 포트를 지정해주지 않아 생긴 문제다. <br> webEnvironment=RANDOM_PORT를 추가해주면 해결된다.
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BoardApiControllerTest {
```


### IllegalStateException: springSecurityFilterChain cannot be null. 에러
ApiControllerTest 코드 작성할 때, MockMvc와 Spring Security 작성하는 부분에서 발생한 오류다.
오류가 발생한 위치는 아래와 같다.
```java
    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
```

@AutoConfigureMockMvc를 추가하여 해결했다. <br> Mock 테스트 시 필요한 의존성을 제공해주기 때문에 @BeforeEach 코드는 지우면 된다.

> `참고` <br>
> [Incomplete Documentation for Setting Up MockMvc and Spring Security - 해결책](https://github.com/spring-projects/spring-security/issues/7688) <br>
> [Controller 단위 테스트를 위한  MockMvc 설정 - 블로그 글](https://goodteacher.tistory.com/492) <br>
> [@SpringBootTest, @AutoConfogureMockMvc, 그리고 @WebMvcTest - 블로그 글](https://astrid-dm.tistory.com/536) <br>


### HttpMessageConversionException -> InvalidDefinitionException 에러
에러로그는 다음과 같다.
```
org.springframework.web.util.NestedServletException: Request processing failed; 
nested exception is org.springframework.http.converter.HttpMessageConversionException: 
Type definition error: [simple type, class test.lomboktest.web.dto.CreatePostRequest];
nested exception is com.fasterxml.jackson.databind.exc.InvalidDefinitionException:
Cannot construct instance of `test.lomboktest.web.dto.CreatePostRequest` (no Creators, like default constructor, exist):
cannot deserialize from Object value (no delegate- or property-based Creator)
at [Source: (org.springframework.util.StreamUtils$NonClosingInputStream);line: 1, column: 2]
```

클라이언트에게 받은 CreatePostRequest DTO에 기본 생성자가 없어 발생한 오류다. <br> DTO에 @NoArgsConstructor 어노테이션을 추가해서 해결했다. <br>
> `참고`  [HttpMessageConversionException 오류에 대한 글 - 블로그 글](https://januaryman.tistory.com/450) <br>

## 10. 회고
### 끝나고 느낀점
공부했던 스프링, 네트워크를 되새길 수 있어서 좋았고, 생각보다 테스트 코드를 작성할 때 어려웠었다. <br>
(TDD로 작성하고 싶었는데, 결과적으로 고민하다보니 통합 테스트를 작성하게 되었다.) <br><br>
지금 프로젝트는 1년 전에 만든 것인데,  개선하려다보니 버전 업그레이드로 인한 오류를 만나게 되었다. <br>
(따로 스프링 버전관련 노트정리를 해야 겠다.) <br><br>
이번에는 간단한 API 설계만 했는데, 다음에 연관관계를 추가하고 컬렉션 조회에 대한 최적화 API 구현을 연습해보자.

<br>

### 필요한 공부
다음 API 구현 프로젝트를 만들 때는 , TDD를 생각하며 테스트 코드에 집중해야 겠다고 느꼈다. <br>
단위 테스트, 실패 테스트 등등 테스트 코드를 좀 더 연습해야 할 것 같다. <br>
참고자료로  [TDD 작성 - 블로그 글](https://mangkyu.tistory.com/184), [Spring Boot 공식 문서 - Spring Boot의 테스트 기능에 대해 - 블로그 글](https://meetup.nhncloud.com/posts/124)을 찾았는데 읽어보려고 한다. <br><br>
그리고 코드 리팩토링을 위해 자바 8(책: effective java)을 좀 더 공부할 예정이다.
