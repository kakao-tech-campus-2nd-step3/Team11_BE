package boomerang.comment.domain;

import boomerang.comment.dto.CommentRequestDto;
import boomerang.domain.board.domain.Board;
import boomerang.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)  //도메인에서 날짜필드가 자동 관리되도록 설정
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private String text;


    @Enumerated(EnumType.STRING)
    @Builder.Default //빌더 패턴시에도 "NOT_DELETED"로 만들어질 수 있도록 기본값 설정
    private IsDeleted isDeleted = IsDeleted.NOT_DELETED;

    public String getAuthorName() {
        return "가상닉네임";
    }

    @CreatedDate
    private LocalDateTime createdAt; //기본 업데이트 됨

    @LastModifiedDate
    private LocalDateTime updatedAt; //기본 업데이트 됨

    public void softDelete() {
        this.isDeleted = IsDeleted.DELETED;
    }

    public void updateCommentText(String text) {
        this.text = text;
    }

    public Comment(Member author, Board board, CommentRequestDto commentRequestDto) {
        this.author = author;
        this.board = board;
        this.text = commentRequestDto.getText();
    }
}
