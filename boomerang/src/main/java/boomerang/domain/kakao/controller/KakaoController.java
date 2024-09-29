package boomerang.domain.kakao.controller;

import boomerang.domain.kakao.domain.KakaoDomain;
//import boomerang.domain.kakao.dto.KakaoCreateRequestDto;
import boomerang.domain.kakao.domain.KakaoMember;
import boomerang.domain.kakao.dto.KakaoTokenResponseDto;
import boomerang.domain.kakao.service.KakaoService;
import boomerang.domain.member.service.MemberService;
import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.response.ResultCode;
import boomerang.global.response.SimpleResultResponseDto;
import boomerang.global.utils.CookieUtil;
import boomerang.global.utils.JwtUtil;
import boomerang.global.utils.ResponseHelper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class KakaoController {
    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public KakaoController(KakaoService kakaoService, MemberService memberService,
        JwtUtil jwtUtil) {
        this.kakaoService = kakaoService;
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

//    @GetMapping("")
//    public ResponseEntity<KakaoCreateResponseDto> getAllMembers() {
//        kakaoService.getAllKakaoDomains();
//        return ;
//    }

    @GetMapping("/login")
    public void authorize(HttpServletResponse response) throws IOException {
        response.sendRedirect(
            "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=5c13fd3c6832cef54c183d9295eecacb&redirect_uri=http://localhost:8080/api/auth/login/callback");
    }

    @GetMapping("/login/callback")
    @ResponseBody
    public ResponseEntity<?> token(@RequestParam("code") String code, HttpServletResponse response) {
        Map<String, Object> responseBody = new HashMap<>();
        KakaoTokenResponseDto kakaoTokenResponseDto = kakaoService.getAccessTokenFromKakao(code);
        KakaoMember kakaoMember = kakaoService.getKakaoProfile(kakaoTokenResponseDto);
        String token = memberService.loginKakaoMember(kakaoMember);

        response.addCookie(CookieUtil.createCookies(token));
        System.out.println("token = " + token);
//        Long memberId = Long.parseLong(claims.getSubject());
//        Member member = memberService.getMemberById(memberId);
//        member.setAccessToken(token);// jwt 토큰 멤버 DB에Claims claims = jwtUtil.extractClaims(token.replace("Bearer ", "")); 저장
//        member.setKakaoToken(kakaoTokenResponseDto.getAccessToken());// 카카오에서 발급받은 엑세스 토큰도 멤버 DB에 저장
//        memberService.updateMember(member);
        return ResponseEntity.status(HttpStatus.OK)
//            .header(HttpHeaders.AUTHORIZATION, token)
            .build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<KakaoDomain> getKakaoById(@PathVariable(name = "id") Long id) {
        return ResponseHelper.createResponse(kakaoService.getKakaoDomainById(id));
    }

//    @PostMapping("")
//    public ResponseEntity<SimpleResultResponseDto> createKakao(@RequestBody KakaoCreateRequestDto kakaoCreateRequestDTO) {
//        kakaoService.createKakaoDomain(kakaoCreateRequestDTO.toKakaoCreateServiceDto());
//        return ResponseHelper.createSimpleResponse(ResultCode.CREATE_MEMBER_SUCCESS);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<SimpleResultResponseDto> updateKakao(@PathVariable(name = "id") Long id, @RequestBody KakaoCreateRequestDto kakaoCreateRequestDTO) {
//        kakaoService.updateKakaoDomain(kakaoCreateRequestDTO.toKakaoCreateServiceDto(id));
//        return ResponseHelper.createSimpleResponse(ResultCode.UPDATE_MEMBER_SUCCESS);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> deleteKakao(@PathVariable(name = "id") Long id) {
        kakaoService.deleteKakaoDomain(id);
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
