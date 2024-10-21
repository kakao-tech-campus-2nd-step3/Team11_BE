package boomerang.board.dto;

import boomerang.board.domain.AnonymousStatus;
import boomerang.board.domain.Board;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardListResponseDto {
    private int totalPage;
    private int currentPage;
    private List<Item> itemList;

    // Board 객체 리스트를 받아서 각 요소를 Item으로 변환하여 List에 담는다
    public BoardListResponseDto(Page<Board> boardPage, int contentLength) {
        this.totalPage = boardPage.getTotalPages();
        this.currentPage = boardPage.getNumber() + 1;

        itemList = boardPage.stream()
                .map(board -> Item.of(board, contentLength))  // contentLength를 전달
                .toList();
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Item {
        private String title;
        private String content;
        private AnonymousStatus anonymousStatus;
        private String writerEmail;
        private String writerName;
        private Long likeCount;
        private Long commentCount;

        // Board 객체를 받아 필요한 필드만 추출하고, contentLength만큼 글자를 자른다
        public static Item of(Board board, int contentLength) {
            // content 길이를 제한하고, contentLength보다 길 경우 '...'을 추가
            String content = board.getContent();
            if (content.length() > contentLength) {
                content = content.substring(0, contentLength) + "...";
            }

            return Item.builder()
                    .title(board.getTitle())
                    .content(content)  // 자른 content 사용
                    .anonymousStatus(board.getAnonymousStatus())
                    .writerEmail(board.getWriterEmail())
                    .writerName(board.getMember().getNickname())
                    .likeCount(board.getLikeCount())
                    .commentCount(board.getCommentCount())
                    .build();
        }
    }
}
