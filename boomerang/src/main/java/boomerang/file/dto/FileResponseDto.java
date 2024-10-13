package boomerang.file.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.net.URL;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FileResponseDto {
    private URL fileUrl;

    public FileResponseDto(URL fileUrl) {
        this.fileUrl = fileUrl;
    }
}
