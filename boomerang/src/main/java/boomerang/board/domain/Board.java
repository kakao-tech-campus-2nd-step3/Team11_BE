package boomerang.board.domain;

import boomerang.board.dto.BoardRequestDto;
import boomerang.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String subtitle;

    private String content;

    @Embedded
    private BoardType boardType;

    @Embedded
    private Location location;

    @Embedded
    private AnonymousStatus anonymousStatus;

    // Board 데이터를 들고 올 때마다 작성자가 누구인지 가져오는 것은 필수이다
    // 그렇다면, Member 를 Lazy 로 하고 작성자를 따로 저장하는 것이 좋을까
    // 아니면, 매번 EAGER 로 가져오는 것이 좋을까?
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Board() {
    }

    public Board(Long id, String title, String subtitle, String content, BoardType boardType, Location location, AnonymousStatus anonymousStatus, Member member) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.boardType = boardType;
        this.location = location;
        this.anonymousStatus = anonymousStatus;
        this.member = member;
    }

    // DTO를 사용하는 생성자
    public Board(BoardRequestDto boardRequestDto, Member member) {
        this.title = boardRequestDto.getTitle();
        this.subtitle = boardRequestDto.getSubtitle();
        this.content = boardRequestDto.getContent();
        this.boardType = boardRequestDto.getBoardType();
        this.location = boardRequestDto.getLocation();
        this.anonymousStatus = boardRequestDto.getAnonymousStatus();
        this.member = member;
    }

    // ID가 있는 경우의 생성자
    public Board(Long id, BoardRequestDto boardRequestDto, Member member) {
        this.id = id;
        this.title = boardRequestDto.getTitle();
        this.subtitle = boardRequestDto.getSubtitle();
        this.content = boardRequestDto.getContent();
        this.boardType = boardRequestDto.getBoardType();
        this.location = boardRequestDto.getLocation();
        this.anonymousStatus = boardRequestDto.getAnonymousStatus();
        this.member = member;
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
