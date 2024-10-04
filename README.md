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
>        }
> 
>```

