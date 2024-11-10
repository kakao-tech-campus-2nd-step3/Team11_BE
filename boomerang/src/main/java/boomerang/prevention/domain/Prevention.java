package boomerang.prevention.domain;

import boomerang.member.domain.Member;
import boomerang.prevention.enums.ContractType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    // 계약 유형 (전세, 월세)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractType contractType;

    // 계약 날짜
    @Column(nullable = false)
    private LocalDate contractDate;

    // 경매 날짜
    @Column
    private LocalDate auctionStartDate;

    // 임차권 날짜
    @Column
    private LocalDate leaseRegistrationDate;

    // 임차권 금액
    @Column
    private Long leaseRegistrationAmount;

    // 채권
    @OneToMany(mappedBy = "prevention", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Mortgage> mortgages = new ArrayList<>();

    // 경매 x, 임차권 x
    public Prevention(Member member, String address, Long housePrice, Long depositAmount,
        ContractType contractType, LocalDate contractDate) {
        this.member = member;
        this.address = address;
        this.housePrice = housePrice;
        this.depositAmount = depositAmount;
        this.contractType = contractType;
        this.contractDate = contractDate;
    }

    // 경매 o, 임차권 x
    public Prevention(Member member, String address, Long housePrice, Long depositAmount,
        ContractType contractType, LocalDate contractDate, LocalDate auctionStartDate) {
        this.member = member;
        this.address = address;
        this.housePrice = housePrice;
        this.depositAmount = depositAmount;
        this.contractType = contractType;
        this.contractDate = contractDate;
        this.auctionStartDate = auctionStartDate;
    }

    // 경매 x, 임차권 o
    public Prevention(Member member, String address, Long housePrice, Long depositAmount,
        ContractType contractType, LocalDate contractDate,
        LocalDate leaseRegistrationDate, Long leaseRegistrationAmount) {
        this.member = member;
        this.address = address;
        this.housePrice = housePrice;
        this.depositAmount = depositAmount;
        this.contractType = contractType;
        this.contractDate = contractDate;
        this.leaseRegistrationDate = leaseRegistrationDate;
        this.leaseRegistrationAmount = leaseRegistrationAmount;
    }

    // 경매 o, 임차권 o
    public Prevention(Member member, String address, Long housePrice, Long depositAmount,
        ContractType contractType, LocalDate contractDate, LocalDate auctionStartDate,
        LocalDate leaseRegistrationDate, Long leaseRegistrationAmount) {
        this.member = member;
        this.address = address;
        this.housePrice = housePrice;
        this.depositAmount = depositAmount;
        this.contractType = contractType;
        this.contractDate = contractDate;
        this.auctionStartDate = auctionStartDate;
        this.leaseRegistrationDate = leaseRegistrationDate;
        this.leaseRegistrationAmount = leaseRegistrationAmount;
    }

    public void addMortgage(Long amount, String creditor, LocalDate registrationDate) {
        Mortgage mortgage = new Mortgage(amount, creditor, registrationDate, this);
        this.mortgages.add(mortgage);
    }
}
