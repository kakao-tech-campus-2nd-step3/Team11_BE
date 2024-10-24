package boomerang.comment.domain;

import boomerang.IsDeleted;
import boomerang.board.domain.Board;
import boomerang.comment.dto.CommentRequestDto;
import boomerang.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private IsDeleted isDeleted = IsDeleted.NOT_DELETED;

    public String getAuthorName() {
        return author.getNickname();
    }

    public String getAuthorEmail() {
        return author.getEmail();
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
        this.isDeleted = IsDeleted.NOT_DELETED;
    }

    public boolean isMemberCommentAuthor(Member loginUser) {
        return loginUser.equals(this.author);
    }

}
