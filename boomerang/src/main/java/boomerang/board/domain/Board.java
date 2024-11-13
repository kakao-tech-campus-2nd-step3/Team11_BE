package boomerang.board.domain;

import boomerang.IsDeleted;
import boomerang.board.dto.BoardRequestDto;
import boomerang.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jsoup.Jsoup;

@Getter
@Entity
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 10000)
    private String content;

    private String summary;

    private String writerNickname;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Embedded
    private Location location;

    private Long score;

    private Long likeCount = 0L;

    private Long commentCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

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
        this.writerNickname = member.getNickname();
        this.boardType = boardRequestDto.getBoard_type();
        this.location = boardRequestDto.getLocation();
        this.member = member;

        this.summary = summaryContent(content);
    }

    // ID가 있는 경우의 생성자
    public Board(Long id, BoardRequestDto boardRequestDto, Member member) {
        this.id = id;
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.writerNickname = member.getNickname();
        this.boardType = boardRequestDto.getBoard_type();
        this.location = boardRequestDto.getLocation();
        this.member = member;

        this.summary = summaryContent(content);
    }

    private String summaryContent(String content) {
        int contentLength = 20;

        // html 태그 제거
        String summary = Jsoup.parse(content).text();

        // 줄바꿈 문자(\n, \r)들을 스페이스로 변환
        summary = summary.replaceAll("\\r?\\n", " ");

        // contentLength 를 넘으면 이후를 "..."으로 요약
        if (summary.length() > contentLength) {
            return summary.substring(0, contentLength - 1) + "...";
        }

        return summary;
    }

    public void calculateScore(int validDays, Long likeWeight, Long commentWeight) {
        LocalDateTime expiryDate = createdAt.plusDays(validDays);
        if (LocalDateTime.now().isAfter(expiryDate)) {
            // 유효 기간이 지나면 score를 음수로 설정
            this.score = -1L;
        } else {
            this.score = this.likeCount * likeWeight + this.commentCount * commentWeight;
        }
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
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Board item = (Board) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
