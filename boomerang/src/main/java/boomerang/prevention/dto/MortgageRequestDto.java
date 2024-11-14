package boomerang.prevention.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MortgageRequestDto {

    @NotNull(message = "채권 금액은 필수 입력값입니다")
    @Positive(message = "채권 금액은 0보다 커야 합니다")
    private Long amount;

    @NotBlank(message = "채권자는 필수 입력값입니다")
    @Size(max = 100, message = "채권자는 100자를 초과할 수 없습니다")
    private String creditor;

    @NotNull(message = "등록 날짜는 필수 입력값입니다")
    private LocalDate registrationDate;
}