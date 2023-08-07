Spring Boot와 Thymeleaf를 사용하여 CRUD 웹애플리케이션, API를 만들어봤다. <br>
[baeldung 블로그 글](https://www.baeldung.com/spring-boot-crud-thymeleaf)를 읽고나서 간단한 CRUD 기능 구현하려고 했는데, <br>
[인프런 김영한 강의](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-API%EA%B0%9C%EB%B0%9C-%EC%84%B1%EB%8A%A5%EC%B5%9C%EC%A0%81%ED%99%94)에서 들었던 내용을 떠올리면서 API도 만들어 보니 내용 정리도 되고 좋았다. <br>

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
+  API 설계
+  테스트 코드
+  만났던 오류
+ 회고

## 1.  프로젝트 생성
스프링 개발시 dependencies 추가를 할 때, Maven이나 Gradle을 사용하여 버전에 맞게 필요한 라이브러리를 추가해야 한다. <br>
스프링 스타터를 통해 프로젝트를 만드는데, 다음과 같이 버전 및 플러그인을 구성한다. <br>
Gradle Project, Spring Boot version 2.6.3, Jar, Java 11으로 선택하고 <br> 
JPA, Thymeleaf, Spring Web, Lombok, H2 라이브러리를 추가했다.

> `Project` <br>
> 스프링 스타터를 보면 "Project: Maven 또는 Gradle"을 고를 수 있는데, <br>
> 필요한 라이브러리를 가져와 주고, build 라이프 사이클을 관리해주는 tool이다. <br>
>[김영한 강의중] 과거에는 주로 Maven을 선택했지만, 요즘에는 거의 Gradle을 쓴다고 한다.

> `Spring Boot` <br>
> 스프링부트 버전은 그때 기준으로 최신버전을 선택했다. <br>
> (SNAPSHOT)은 아직 만드는 중인 버전이라는 뜻

> `Project Metadata` <br>
> Group : 그룹에는 보통 기업 도메인명을 적는다. (나는 공부용이므로 "test"라고 적었다.)
> Artifact : 빌드되어 나온 결과물을 말한다. 프로젝트명과 비슷하다. (나는 "lomboktest"라고 적었다.)

> `Dependencies` <br>
> 스프링 부트 기반으로 스프링 프로젝트를 시작할 때, 어떤 라이브러리를 가져와 쓸 것인지 설정하는 부분이다. <br>
> Spring Web : 웹 프로젝트를 만들 때 필수다. <br>
> Thymeleaf : HTML 템플릿을 만들어주는 라이브러리 <br>
>  H2 : 가벼운 RDB <br> (인메모리 관계형 데이터베이스다. <br> 메모리에서 실행되어 애플리케이션을 재시작하면 초기화되는 특성으로 테스트에 많이 사용된다. <br> 
> 별도의 설치 없이 프로젝트 의존성만으로 관리할 수 있다. ) <br><br>
> JPA :스프링 부트용 Spring Data JPA 추상화 라이브러리다. 스프링 부트 버전에 맞춰 자동으로 JPA관련 라이브러리들의 버전을 관리해준다. <br>
> Lombok : 생성자 주입 방식은 생성자도 만들어야하고 주입 받은 값을 대입하는 코드도 만들어야하는데, 필드 주입처럼 편하게 사용하기 위해 롬복 라이브러리를 사용한다. <br>


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
>  주인이 아닌 user.board는 조회를 위한 JPQL이나 객체 그래프를 탐색할 때 사용한다. (주인이 아닌 쪽도 조회 가능하도록)

### 테이블 설계
요구사항을 기반으로 설계한 테이블 ERD다. <br>
![oo](https://github.com/Kim-Gyuri/spring-board-crud-paging/assets/57389368/4bdfcf86-a718-49ea-ac7c-ea9a669fc1e0)


+ 회원(USER) : 이름(NAME)과 로그인 정보(이메일, 비밀번호)를 가진다.
+ 게시물(BOARD) : 게시물 제목(TITLE), 내용(CONTENT), 게시글 분류(BOARD TYPE), 작성한 날짜(UPDATED DATE)를 가진다. <br> 게시물 수정 시 제목,내용을 바꾸 수 있다. 게시물을 수정하면 게시물 작성시간도 업데이트된다.

### 엔티티 설계와 매핑
설계한 테이블을 기반으로 실제 엔티티를 설계하자. <br>
+ [Board 엔티티 코드](https://github.com/Kim-Gyuri/spring-board-crud-paging/blob/master/src/main/java/test/lomboktest/entities/Board.java) 
+ [User 엔티티 코드](https://github.com/Kim-Gyuri/spring-board-crud-paging/blob/master/src/main/java/test/lomboktest/entities/User.java) 

<br>

+ 식별자는 @Id와 @GeneratedValue를 사용해서 DB에서 자동 생성되도록 했다. 
> @GeneratedValue의 기본 생성전략은 AUTO이므로 선택한 DB 방언에 따라 IDENTITY, SEQUANCE, TABLE 중 하나가 선택된다.
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

+ 게시글 분류(BOARDTYPE)은 열거형을 사용하므로 @Enumerated로 매핑했다. <br> EnumType.String 속성을 지정해서 열거형의 이름이 그대로 저장되도록 했다. <br>
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
> JPA에선 Repository라고 부르며, 인터페이스로 생성한다. <br>

<br>

+ 인터페이스를 생성한 후, JpaRepository<Entity 클래스, PK타입>을 상속하면 기본적인 CRUD 메소드가 자동으로 생성된다.
> @Repository도 추가할 필요도 없다.

+ boardRepository.sortByBoardType(type)을 만들 때, 쿼리 작성을 QueryDsl로 간단하게 만들었다.
> [BoardRepositoryCustomImpl 코드](https://github.com/Kim-Gyuri/spring-board-crud-paging/blob/master/src/main/java/test/lomboktest/repository/BoardRepositoryCustomImpl.java) <br>
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
> BooleanExpression을 사용하여 boardTypeContains()메서드를 만들었다. (좀 더 직관적으로 볼 수 있도록 리팩토링할 수 있음) <br>
> BooleanExpression을 리턴하는데, 메소드에서 조건이 맞지 않으면 null을 리턴한다. (where에서는 상황에 따라 조건문을 생성하게 된다.)


## 4. 서비스 계층 구현
구현한 로직은 아래와 같다. [BoardService 코드](https://github.com/Kim-Gyuri/spring-board-crud-paging/blob/master/src/main/java/test/lomboktest/service/BoardService.java)
+ Long save(BoardForm boardForm) : 엔티티를 등록한다.  MVC 확인용
+ Long save(CreatePostRequest request) : 엔티티를 등록한다. API 확인용
+ void delete(Long id) : 엔티티를 삭제한다.
+ Long update(Long id, BoardForm boardForm) : 엔티티 부분 수정 
+ Board findById(Long id) : 엔티티 단건 조회
+ Page<Board> findBoardList(Pageable pageable) : 페이징
+ List<MainPostDto> findBoardList() : 엔티티 전체조회, API 확인용
+ List<MainPostDto> findByBoardType(String type) : 엔티티 타입별(자유게시판, 공지) 정렬조회, API 확인용

### @Transactional(readOnly = true) 
조회 로직에는 읽기 전용으로 엔티티를 조회했다.
> 성능 향상이 된다. <br>
> 강제로 플러시를 호출하지 않는 한 플러시가 일어나지 않는다. <br>
> 따라서 트랜잭션을 커밋하더라도 영속성 컨텍스트가 플러시 되지 않아서 `엔티티의 등록/수정/삭제에는 동작하지 않고` <br>
> `또한 읽기 전용으로, 영속성 컨텍스트는 변경 감지를 위한 스냅샷을 보관하지 않으므로 성능이 향상된다.` <br>

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
## 5. 웹 계층 개발
`페이징 위주로 작성할 예정` <br>
### 컨트롤러
[BoardController 코드](https://github.com/Kim-Gyuri/spring-board-crud-paging/blob/master/src/main/java/test/lomboktest/controller/BoardController.java)
### 뷰


## 6. API 설계
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

### 등록
요청값을  DTO로 받는데, DTO를 RequestBody와 매핑했다.
> 엔티티와 API 스펙을 명확한게 분리할 수 있다.

### 조회
응답값으로 DTO을 반환한다.
> DTO로 변환해서 반환하면, 엔티티가 변해도 API 스펙이 변경되지 않는다. <br>
> 전체조회 API 경우, Result 클래스로 컬렉션을 감싸서 반환했다. (향후 필요한 필드(예: 전체 크기)를 추가할 수 있는 구조다.) 

### 삭제
삭제했다는 확인메시지 DTO를 반환했다.
### # 화면캡처/ 쿼리캡처 / postman??
