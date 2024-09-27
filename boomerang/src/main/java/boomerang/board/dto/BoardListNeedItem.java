package boomerang.board.dto;

import boomerang.board.domain.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
public class BoardListNeedItem {
    private String val; // 페이지와 같은 추가 정보를 담는 필드
    private List<Item> itemList; // 아이템 리스트

    // 생성자
    public BoardListNeedItem(String val, List<Item> itemList) {
        this.val = val;
        this.itemList = itemList;
    }

    // Board 객체 리스트를 받아서 각 요소를 Item으로 변환하여 List에 담는다
    public static BoardListNeedItem of(List<Board> boardList) {
        List<Item> items = boardList.stream()
                .map(Item::of)
                .toList();
        return BoardListNeedItem.builder()
                .val("some_value") // 필요에 따라 값 설정
                .itemList(items)
                .build();
    }

    @Getter
    @Builder
    public static class Item {
        private Title title;
        private Subtitle subtitle;
        private Content content;
        private BoardType boardType;
        private Location location;
        private AnonymousStatus anonymousStatus;

        // 생성자
        public Item(Title title, Subtitle subtitle, Content content, BoardType boardType, Location location, AnonymousStatus anonymousStatus) {
            this.title = title;
            this.subtitle = subtitle;
            this.content = content;
            this.boardType = boardType;
            this.location = location;
            this.anonymousStatus = anonymousStatus;
        }

        // Board 객체를 받아 필요한 필드만 추출
        public static Item of(Board board) {
            return Item.builder()
                    .title(board.getTitle())
                    .subtitle(board.getSubtitle())
                    .content(board.getContent())
                    .boardType(board.getBoardType())
                    .location(board.getLocation())
                    .anonymousStatus(board.getAnonymousStatus())
                    .build();
        }
    }
}
