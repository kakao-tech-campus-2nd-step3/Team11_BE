package boomerang.domain.member.controller;

import boomerang.domain.member.domain.MemberDomain;
import boomerang.domain.member.dto.MemberCreateRequestDto;
import boomerang.domain.member.service.MemberService;
import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.response.ResultCode;
import boomerang.global.response.SimpleResultResponseDto;
import boomerang.global.utils.ResponseHelper;
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
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

//    @GetMapping("")
//    public ResponseEntity<MemberCreateResponseDto> getAllMembers() {
//        memberService.getAllMemberDomains();
//        return ;
//    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDomain> getMemberById(@PathVariable(name = "id") Long id) {
        return ResponseHelper.createResponse(memberService.getMemberDomainById(id));
    }

    @PostMapping("")
    public ResponseEntity<SimpleResultResponseDto> createMember(@RequestBody MemberCreateRequestDto memberCreateRequestDTO) {
        memberService.createMemberDomain(memberCreateRequestDTO.toMemberCreateServiceDto());
        return ResponseHelper.createSimpleResponse(ResultCode.CREATE_MEMBER_SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> updateMember(@PathVariable(name = "id") Long id, @RequestBody MemberCreateRequestDto memberCreateRequestDTO) {
        memberService.updateMemberDomain(memberCreateRequestDTO.toMemberCreateServiceDto(id));
        return ResponseHelper.createSimpleResponse(ResultCode.UPDATE_MEMBER_SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> deleteMember(@PathVariable(name = "id") Long id) {
        memberService.deleteMemberDomain(id);
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