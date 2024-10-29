package boomerang.email.controller;

import boomerang.email.dto.EmailSendRequestDto;
import boomerang.email.dto.EmailVerificationRequestDto;
import boomerang.email.service.EmailVerificationService;
import boomerang.global.oauth.dto.PrincipalDetails;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email-verifications")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    // 이메일 인증 코드 발송
    @PostMapping
    public ResponseEntity<Void> sendVerificationEmail(
        @Valid @RequestBody EmailSendRequestDto requestDto) throws MessagingException, IOException {
        emailVerificationService.sendVerificationEmail(requestDto.getEmail());
        return ResponseEntity.ok().build();
    }

    // 이메일 인증 코드 확인
    @PostMapping("/validation")
    public ResponseEntity<Boolean> verifyEmail(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @Valid @RequestBody EmailVerificationRequestDto requestDto) {
        boolean isVerified = emailVerificationService.verifyEmail(
            principalDetails.getMemberEmail(), requestDto);
        return ResponseEntity.ok(isVerified);
    }
}