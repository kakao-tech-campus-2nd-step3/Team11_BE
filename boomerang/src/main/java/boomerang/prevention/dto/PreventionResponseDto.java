package boomerang.prevention.dto;

import boomerang.prevention.enums.ContractType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PreventionResponseDto {

    private Long id;
    private String address;
    private Long housePrice;
    private Long depositAmount;
    private ContractType contractType;
    private LocalDate contractDate;
    private LocalDate auctionStartDate;
    private LocalDate leaseRegistrationDate;
    private Long leaseRegistrationAmount;
    private List<MortgageResponseDto> mortgages;
}
