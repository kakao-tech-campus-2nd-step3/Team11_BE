package boomerang.email.service;

import boomerang.email.dto.EmailVerificationRequestDto;
import boomerang.email.dto.EmailVerificationResponseDto;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailVerificationService {

    private final MemberService memberService;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailSender emailSender;


    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final long VERIFICATION_TTL = 5L;

    public EmailVerificationResponseDto sendVerificationEmail(String email) {
        String verificationCode = generateVerificationCode();
        String redisKey = "email:verification:" + email;

        // Redis에 인증 코드 저장
        redisTemplate.opsForValue()
            .set(redisKey, verificationCode, VERIFICATION_TTL, TimeUnit.MINUTES);

        // 비동기로 이메일 전송 요청
        emailSender.sendEmail(email, verificationCode);

        // 즉시 응답 반환
        return new EmailVerificationResponseDto(email, "이메일 전송이 요청되었습니다.");
    }

    @Transactional
    public EmailVerificationResponseDto verifyEmail(String email,
        EmailVerificationRequestDto requestDto) {
        String redisKey = "email:verification:" + requestDto.getEmail();
        String savedCode = redisTemplate.opsForValue().get(redisKey);

        if (savedCode == null) {
            throw new BusinessException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        if (!savedCode.equals(requestDto.getVerificationCode())) {
            throw new BusinessException(ErrorCode.VERIFICATION_CODE_INVALID);
        }

        Member member = memberService.getMemberByEmail(email);
        member.verifyEmail();
        redisTemplate.delete(redisKey);

        return new EmailVerificationResponseDto(requestDto.getEmail(), "이메일 인증에 성공했습니다.");
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}