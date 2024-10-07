## 5주차 리뷰 받고 싶은 부분


### 5주차에 작업 내용
- [x] 카카오 로그인
- [x] 커뮤니티의 보드 CRUD 작업
*** 
### 6주차에 작업할 내용
- 커뮤니티의 댓글 기능 합치기
- 커뮤니티의 좋아요 기능 합치기


- 보증금 돌려받기 가이드라인의 유저상황조사 기능 구현 (문성민)
- 보증금 돌려받기 가이드라인의 유저별 진행도 기능 구현 (진서현)
- 에러시 로그를 찍는 기능 (정재빈)

*** 
### 궁금한 점
#### 질문 1. 도메인객체는 어디 계층에서까지 다룰 수 있을지 궁금합니다. 

상황 1.`요청 객체 - 도메인 - 응답 객체` 만 사용하는 상황입니다.    
현재는 컨트롤러에서 도메인을 응답객체로 만들고 있는데,도메인 객체를 컨트롤러에서 다뤄도 될지 궁금합니다. 이런 상황에서는 보통 응답객체를 어디서 만드나요? 


>[컨트롤러 코드]
>```java
>    @GetMapping("/{board_id}")
>    public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable(name = "board_id") Long boardId) {
>        Board board = boardService.getBoardById(boardId);
>
>        return ResponseEntity.status(HttpStatus.OK)
>                .body(new BoardResponseDto(board));
>    }
>```
>
>
>[서비스 코드]
>```java
>    // ID로 게시물 가져오기
>    public Board getBoardById(Long id) {
>        return boardRepository.findById(id)
>        .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND_ERROR));
>    }
>```


### 질문 2. 연관관계에서 즉시로딩과 지연로딩 선택 기준
Board 테이블을 조회할때마다, 항상 작성자의 정보가 필요한 상황이라면,
어떻게 연관관계 조회 전략을 선택하는 것이 좋을지 궁금합니다. 

#### 방법 1.`FetchType.LAZY` 선택하고 `get메서드`로 조회   

#### 방법 2. `FetchType.EAGER`를 설정, 지연로딩을 선택하지 않음   

#### 방법 3. 작성자의 이메일을 필드로 저장 후
- 작성자의 이메일만 필요한 상황, 따라서 이메일을 필드로 저장 후, 유저의 이메일이 변경될 떄, 게시글의 작성자 이메일 필드를 바꿔주는 기능을 추가함.
- 이때 작성자의 이메일은 자주 변경되지 않을 것으로 가정했습니다. 


>[엔티티 코드]
> ```java
> 
>   @Table(name = "board")
>   public class Board {
>   
>       @Id
>       @GeneratedValue(strategy = GenerationType.IDENTITY)
>        private Long id;
>
>       @ManyToOne(fetch = FetchType.EAGER)
>        @JoinColumn(name = "member_id", nullable = false)
>       private Member member;
> }
>```



