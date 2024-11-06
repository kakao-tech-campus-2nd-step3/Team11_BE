package boomerang.consultation.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConsultationResponseListDto {
    private int totalPage;
    private int currentPage;
    private List<ConsultationResponseDto> itemList;

    public ConsultationResponseListDto(Page<ConsultationResponseDto> consultationResponsePage) {
        this.totalPage = consultationResponsePage.getTotalPages();
        this.currentPage = consultationResponsePage.getNumber();
        this.itemList = consultationResponsePage.getContent();
    }
}
