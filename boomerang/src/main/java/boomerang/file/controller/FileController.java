package boomerang.file.controller;

import boomerang.file.dto.FileResponseDto;
import boomerang.file.service.FileService;
import boomerang.global.oauth.dto.PrincipalDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;


@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping( value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<FileResponseDto> upload(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("file") MultipartFile multipartFile) {
        URL fileUrl = fileService.upload(principalDetails.getMemberEmail(), multipartFile);
        return ResponseEntity.status(HttpStatus.OK)
                .body(FileResponseDto.builder()
                        .fileUrl(fileUrl)
                        .build());
    }
}
