package boomerang.file.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.net.URL;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FileResponseDto {

    private URL fileUrl;

    public FileResponseDto(URL fileUrl) {
        this.fileUrl = fileUrl;
    }
}
