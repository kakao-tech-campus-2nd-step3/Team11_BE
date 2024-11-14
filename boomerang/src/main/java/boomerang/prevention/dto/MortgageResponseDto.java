package boomerang.prevention.dto;

import boomerang.prevention.domain.Mortgage;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MortgageResponseDto {

    private Long id;
    private Long amount;
    private String creditor;
    private LocalDate registrationDate;

    public MortgageResponseDto(Mortgage mortgage) {
        this.id = mortgage.getId();
        this.amount = mortgage.getAmount();
        this.creditor = mortgage.getCreditor();
        this.registrationDate = mortgage.getRegistrationDate();
    }
}
