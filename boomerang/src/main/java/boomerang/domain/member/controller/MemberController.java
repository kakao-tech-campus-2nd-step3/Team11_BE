package boomerang.domain.member.controller;

import boomerang.domain.member.domain.Member;
import boomerang.domain.member.dto.MemberCreateRequestDto;
import boomerang.domain.member.service.MemberService;
import boomerang.global.exception.DomainValidationException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable(name = "id") Long id) {
        Member member = memberService.getMemberDomainById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(member);
    }

    // 시큐리티 필터 테스트 컨트롤러
    @GetMapping("")
    public ResponseEntity<Member> getMember(
        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = memberService.getMemberDomainByEmail(principalDetails.getMemberEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body(member);
    }

    @PostMapping("")
    public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequestDto memberCreateRequestDTO) {
        memberService.createMemberDomain(memberCreateRequestDTO.toMemberCreateServiceDto());
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMember(@PathVariable(name = "id") Long id, @RequestBody MemberCreateRequestDto memberCreateRequestDTO) {
        memberService.updateMemberDomain(memberCreateRequestDTO.toMemberCreateServiceDto(id));
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable(name = "id") Long id) {
        memberService.deleteMemberDomain(id);
        return ResponseEntity.status(HttpStatus.CREATED)
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
