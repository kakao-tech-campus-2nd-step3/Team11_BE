package boomerang.board.domain;

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

    @Embedded
    private Title title;

    @Embedded
    private Subtitle subtitle;

    @Embedded
    private Content content;

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

    public Board(Long id, Title title, Subtitle subtitle, Content content, BoardType boardType, Location location, AnonymousStatus anonymousStatus, Member member) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.boardType = boardType;
        this.location = location;
        this.anonymousStatus = anonymousStatus;
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
