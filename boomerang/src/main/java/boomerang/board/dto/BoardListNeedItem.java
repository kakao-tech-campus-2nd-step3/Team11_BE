package boomerang.board.dto;

import boomerang.board.domain.TemplateColumn1;
import boomerang.board.domain.TemplateColumn2;
import boomerang.board.domain.Board;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

    /*
        상황에 따라서는 List 로 담고 싶은 ResponseDto 와 기존의 ResponseDto의 형태가 조금 달라지는 경우가 있다

        ResponseDto
        { name, email, nickname }

        ResponseListDto
        {
            page:
            {
                { name, email, nickname, userStatus},
                { name, email, nickname, userStatus},
                { name, email, nickname, userStatus},
                ...
            }
        }

        이런 특수한 경우, 내부 클래스 Item 을 활용하여 List를 만들 수 있다

     */

@Getter
@Builder
@EqualsAndHashCode
public class BoardListNeedItem {
    // page와 같은 추가적인 정보를 담는 필드
    private String val;
    private List<Item> ItemList;

    // 생성자
    public BoardListNeedItem(String val, List<Item> ItemList) {
        this.val = val;
        this.ItemList = ItemList;
    }

    /*
        Domain 리스트를 받아서 각 요소를 ItemList로 변환하여 List에 담는다
    */
    public static BoardListNeedItem of(List<Board> boardList) {
        List<Item> templateResponseDtoList = boardList.stream()
                .map(Item::of)
                .toList();

        return BoardListNeedItem.builder()
                .val("some_value")  // 필요에 따라 값 설정
                .ItemList(templateResponseDtoList)
                .build();
    }


    @Getter
    @Builder
    public static class Item {
        private TemplateColumn1 templateColumn1;
        private TemplateColumn2 templateColumn2;

        // 생성자
        public Item(TemplateColumn1 templateColumn1, TemplateColumn2 templateColumn2) {
            this.templateColumn1 = templateColumn1;
            this.templateColumn2 = templateColumn2;
        }

        /*
            Domain 객체를 받아 필요한 필드만 추출
        */
        public static Item of(Board board) {
            return Item.builder()
                    .templateColumn1(board.getTemplateColumn1())
                    .templateColumn2(board.getTemplateColumn2())
                    .build();
        }
    }
}
