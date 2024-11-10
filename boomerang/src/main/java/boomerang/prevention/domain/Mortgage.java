package boomerang.prevention.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "mortgage")
public class Mortgage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long amount;

    // 채권자
    @Column(nullable = false)
    private String creditor;

    // 날짜
    @Column(nullable = false)
    private LocalDate registrationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prevention_id")
    private Prevention prevention;

    public Mortgage(Long amount, String creditor, LocalDate registrationDate, Prevention prevention) {
        this.amount = amount;
        this.creditor = creditor;
        this.registrationDate = registrationDate;
        this.prevention = prevention;
    }
}
