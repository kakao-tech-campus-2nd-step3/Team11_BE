package boomerang.prevention.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PreventionRequestDto {

    @NotBlank(message = "주소는 필수 입력값입니다")
    @Size(max = 255, message = "주소는 255자를 초과할 수 없습니다")
    private String address;

    @NotNull(message = "집 가격은 필수 입력값입니다")
    @Positive(message = "집 가격은 0보다 커야 합니다")
    private Long housePrice;

    @NotNull(message = "보증금은 필수 입력값입니다")
    @Positive(message = "보증금은 0보다 커야 합니다")
    private Long depositAmount;

    @Valid
    private List<MortgageRequestDto> mortgages;
}