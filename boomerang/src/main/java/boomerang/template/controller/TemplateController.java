package boomerang.template.controller;

import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import boomerang.template.domain.TemplateDomain;
import boomerang.template.dto.TemplateListResponseDto;
import boomerang.template.dto.TemplateRequestDto;
import boomerang.template.dto.TemplateResponseDto;
import boomerang.template.service.TemplateService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/template")
public class TemplateController {
    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping()
    public ResponseEntity<TemplateListResponseDto> getAllTemplates() {
        List<TemplateDomain> templateDomainList = templateService.getAllTemplateDomains();
        return ResponseEntity.status(HttpStatus.OK)
                .body(TemplateListResponseDto.of(templateDomainList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateResponseDto> getTemplateById(@PathVariable(name = "id") Long id) {
        TemplateDomain templateDomain = templateService.getTemplateDomainById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(TemplateResponseDto.of(templateDomain));
    }

    @PostMapping()
    public ResponseEntity<Void> createTemplate(@RequestBody TemplateRequestDto templateRequestDto) {
        templateService.createTemplateDomain(templateRequestDto.toTemplateDomain());
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTemplate(@PathVariable(name = "id") Long id, @RequestBody TemplateRequestDto templateRequestDto) {
        templateService.updateTemplateDomain(templateRequestDto.toTemplateDomain(id));
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable(name = "id") Long id) {
        templateService.deleteTemplateDomain(id);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    // GlobalException Handler 에서 처리할 경우,
    // RequestBody에서 발생한 에러가 HttpMessageNotReadableException 로 Wrapping 이 되는 문제가 발생한다
    // 때문에, 해당 에러로 Wrapping 되기 전 Controller 에서 Domain Error 를 처리해주었다
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(DomainValidationException e) {
        System.out.println(e);
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}
