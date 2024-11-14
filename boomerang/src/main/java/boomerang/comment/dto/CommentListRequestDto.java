package boomerang.comment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentListRequestDto {

    private int page = 0;
    private int size = 10;
    private Sort.Direction sortDirection = Sort.Direction.DESC;
    private String sortBy = "id";

    public CommentListRequestDto() {
    }

    public CommentListRequestDto(int page, int size, Sort.Direction sortDirection, String sortBy) {
        this.page = page;
        this.size = size;
        this.sortDirection = sortDirection;
        this.sortBy = sortBy;
    }
}

