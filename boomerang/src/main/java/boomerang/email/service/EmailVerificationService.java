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
@RequiredArgsConstructor
public class EmailVerificationService {

    private final MemberService memberService;
    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final long VERIFICATION_TTL = 5L;

    @Async
    public EmailVerificationResponseDto sendVerificationEmail(String email) {
        String verificationCode = generateVerificationCode();
        String redisKey = "email:verification:" + email;
        try {
            redisTemplate.opsForValue()
                .set(redisKey, verificationCode, VERIFICATION_TTL, TimeUnit.MINUTES);

            MimeMessage message = createEmailMessage(email, verificationCode);
            mailSender.send(message);

            return new EmailVerificationResponseDto(email, "이메일 전송에 성공했습니다.");
        } catch (MessagingException e) {
            throw new BusinessException(ErrorCode.MAIL_SEND_ERROR);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.UNEXPECTED_ERROR);
        }
    }

    private MimeMessage createEmailMessage(String email, String code)
        throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(email);
        helper.setSubject("멘토 등록 이메일 인증");

        Context context = new Context();
        context.setVariable("code", code);

        String htmlContent = templateEngine.process("email/verification", context);
        helper.setText(htmlContent, true);

        return message;
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