package boomerang.prevention.dto;

import boomerang.prevention.domain.Prevention;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
    private Long totalMortgageAmount;
    private Boolean isDangerous;
    private List<MortgageResponseDto> mortgages;

    public PreventionResponseDto(Prevention prevention) {
        this.id = prevention.getId();
        this.address = prevention.getAddress();
        this.housePrice = prevention.getHousePrice();
        this.depositAmount = prevention.getDepositAmount();
        this.totalMortgageAmount = prevention.getTotalMortgageAmount();
        this.isDangerous = prevention.getIsDangerous();
        this.mortgages = prevention.getMortgages().stream()
            .map(MortgageResponseDto::new)
            .toList();
    }
}
