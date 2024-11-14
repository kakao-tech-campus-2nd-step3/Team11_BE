package boomerang.prevention.domain;

import boomerang.member.domain.Member;
import boomerang.prevention.dto.PreventionRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "prevention")
public class Prevention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 집 주소
    @Column(nullable = false)
    private String address;

    // 집 가격
    @Column(nullable = false)
    private Long housePrice;

    // 보증금 가격
    @Column(nullable = false)
    private Long depositAmount;

    // 채권 총액
    @Column(nullable = false)
    private Long totalMortgageAmount = 0L;

    // 위험 여부
    @Column(nullable = false)
    private Boolean isDangerous;

    // 채권
    @OneToMany(mappedBy = "prevention", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Mortgage> mortgages = new ArrayList<>();

    public Prevention(Member member, PreventionRequestDto requestDto) {
        this.member = member;
        this.address = requestDto.getAddress();
        this.housePrice = requestDto.getHousePrice();
        this.depositAmount = requestDto.getDepositAmount();
        this.isDangerous = false;
    }

    public void addMortgage(Long amount, String creditor, LocalDate registrationDate) {
        Mortgage mortgage = new Mortgage(amount, creditor, registrationDate, this);
        this.mortgages.add(mortgage);
        this.totalMortgageAmount += amount;
    }

    public void calculateDanger() {
        // (house_price * 0.8) - total_mortgage_amount - deposit_amount > 0
        this.isDangerous = (this.housePrice * 0.8) - this.totalMortgageAmount - this.depositAmount <= 0;
    }
}
