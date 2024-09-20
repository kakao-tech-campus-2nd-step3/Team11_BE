package boomerang.template.controller;

import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.response.ResultCode;
import boomerang.global.response.SimpleResultResponseDto;
import boomerang.global.utils.ResponseHelper;
import boomerang.template.domain.TemplateDomain;
import boomerang.template.dto.TemplateCreateRequestDto;
import boomerang.template.dto.TemplateCreateResponseDto;
import boomerang.template.service.TemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/template")
public class TemplateController {
    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

//    @GetMapping("")
//    public ResponseEntity<TemplateCreateResponseDto> getAllMembers() {
//        templateService.getAllTemplateDomains();
//        return ;
//    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateDomain> getTemplateById(@PathVariable(name = "id") Long id) {
        return ResponseHelper.createResponse(templateService.getTemplateDomainById(id));
    }

    @PostMapping("")
    public ResponseEntity<SimpleResultResponseDto> createTemplate(@RequestBody TemplateCreateRequestDto templateCreateRequestDTO) {
        templateService.createTemplateDomain(templateCreateRequestDTO.toTemplateCreateServiceDto());
        return ResponseHelper.createSimpleResponse(ResultCode.CREATE_MEMBER_SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> updateTemplate(@PathVariable(name = "id") Long id, @RequestBody TemplateCreateRequestDto templateCreateRequestDTO) {
        templateService.updateTemplateDomain(templateCreateRequestDTO.toTemplateCreateServiceDto(id));
        return ResponseHelper.createSimpleResponse(ResultCode.UPDATE_MEMBER_SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> deleteTemplate(@PathVariable(name = "id") Long id) {
        templateService.deleteTemplateDomain(id);
        return ResponseHelper.createSimpleResponse(ResultCode.DELETE_PRODUCT_SUCCESS);
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
