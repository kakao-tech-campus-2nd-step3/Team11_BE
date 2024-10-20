package boomerang.board.domain;

import boomerang.IsDeleted;
import boomerang.board.dto.BoardRequestDto;
import boomerang.comment.domain.Comment;
import boomerang.like.domain.Like;
import boomerang.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String writerEmail;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Embedded
    private Location location;

    @Enumerated(EnumType.STRING)
    private AnonymousStatus anonymousStatus;

    private Long likeCount = 0L;

    private Long commentCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private IsDeleted isDeleted;

    protected Board() {
    }

    // DTO를 사용하는 생성자
    public Board(BoardRequestDto boardRequestDto, Member member) {
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.writerEmail = member.getEmail();
        this.boardType = boardRequestDto.getBoardType();
        this.location = boardRequestDto.getLocation();
        this.member = member;
    }

    // ID가 있는 경우의 생성자
    public Board(Long id, BoardRequestDto boardRequestDto, Member member) {
        this.id = id;
        this.title = boardRequestDto.getTitle();
        this.writerEmail = member.getEmail();
        this.content = boardRequestDto.getContent();
        this.boardType = boardRequestDto.getBoardType();
        this.location = boardRequestDto.getLocation();
        this.anonymousStatus = boardRequestDto.getAnonymousStatus();
        this.member = member;
    }

    public void increaseLikeCount() {
        likeCount += 1;
    }

    public void increaseCommentCount() {
        commentCount += 1;
    }

    public void decreaseLikeCount() {
        likeCount -= 1;
    }

    public void decreaseCommentCount() {
        commentCount -= 1;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Board item = (Board) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
