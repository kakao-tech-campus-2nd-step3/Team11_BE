package boomerang.kakao.controller;

import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.CookieUtil;
import boomerang.global.utils.JwtUtil;
import boomerang.global.utils.ResponseHelper;
import boomerang.kakao.domain.KakaoMember;
import boomerang.kakao.dto.KakaoTokenResponseDto;
import boomerang.kakao.service.KakaoService;
import boomerang.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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


    @Value("${client_id}")
    private String clientId;

    @Value("${app.server.ip}")
    private String serverIp;

    @GetMapping("/login")
    public void authorize(HttpServletResponse response) throws IOException {
        String redirectUri = String.format("http://%s:8080/api/v1/auth/login/callback", serverIp);
        String authorizationUrl = String.format(
            "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s",
            clientId, redirectUri
        );
        response.sendRedirect(authorizationUrl);
    }

    @GetMapping("/login/callback")
    @ResponseBody
    public ResponseEntity<?> token(@RequestParam("code") String code, HttpServletResponse response) {
        KakaoTokenResponseDto kakaoTokenResponseDto = kakaoService.getAccessTokenFromKakao(code);
        KakaoMember kakaoMember = kakaoService.getKakaoProfile(kakaoTokenResponseDto);
        String token = memberService.loginKakaoMember(kakaoMember);
        response.addCookie(CookieUtil.createCookies(token));
        System.out.println("token = " + token);
        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }


    // GlobalException Handler 에서 처리할 경우,
    // RequestBody에서 발생한 에러가 HttpMessageNotReadableException 로 Wrapping 이 되는 문제가 발생한다
    // 때문에, 해당 에러로 Wrapping 되기 전 Controller 에서 Domain Error 를 처리해주었다
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(DomainValidationException e) {
        log.error(e.toString());
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}
