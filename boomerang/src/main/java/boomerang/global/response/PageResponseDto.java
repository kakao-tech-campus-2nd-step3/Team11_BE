package boomerang.global.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PageResponseDto {
    private int totalPage;
    private int currentPage;
    private List<?> content;


    public PageResponseDto(Page<?> page) {
        this.totalPage = page.getTotalPages();
        this.currentPage = page.getNumber();
        this.content = page.getContent();
    }
}
