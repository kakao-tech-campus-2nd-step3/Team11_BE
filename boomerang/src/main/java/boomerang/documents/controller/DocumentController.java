package boomerang.documents.controller;

import boomerang.documents.dto.DocumentRequestDto;
import boomerang.documents.dto.DocumentResponseDto;
import boomerang.documents.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<ByteArrayResource> generateDocument(
        @RequestBody DocumentRequestDto requestDto) {
        try {
            DocumentResponseDto responseDto = documentService.generateDocument(requestDto);

            ByteArrayResource resource = new ByteArrayResource(responseDto.getContent());

            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + responseDto.getFileName() + "\"")
                .body(resource);

        } catch (Exception e) {
            log.error("Failed to generate document", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}