package boomerang.documents.dto;

import lombok.Getter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class DocumentResponseDto {
    private final byte[] content;
    private final String fileName;

    public DocumentResponseDto(byte[] content, String subStepName) {
        this.content = content;
        this.fileName = generateFileName(subStepName);
    }

    private String generateFileName(String subStepName) {
        return subStepName + "_" +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
            ".pdf";
    }
}