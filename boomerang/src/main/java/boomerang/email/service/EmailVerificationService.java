package boomerang.email.service;

import boomerang.email.dto.EmailVerificationRequestDto;
import boomerang.email.dto.EmailVerificationResponseDto;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final MemberService memberService;
    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private final SpringTemplateEngine templateEngine; // Thymeleaf 템플릿 엔진

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final long VERIFICATION_TTL = 5L; // 5분

    @Async
    public EmailVerificationResponseDto sendVerificationEmail(String email) {
        String verificationCode = generateVerificationCode();
        String redisKey = "email:verification:" + email;
        try {
            // Redis에 인증 코드 저장
            redisTemplate.opsForValue()
                .set(redisKey, verificationCode, VERIFICATION_TTL, TimeUnit.MINUTES);

            // 이메일 발송
            MimeMessage message = createEmailMessage(email, verificationCode);
            mailSender.send(message);

            return new EmailVerificationResponseDto(email, "이메일 전송에 성공했습니다.");
        } catch (MessagingException e) {
            throw new BusinessException(ErrorCode.MAIL_SEND_ERROR);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.MAIL_RESOURCE_ERROR);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.UNEXPECTED_ERROR);
        }
    }

    private MimeMessage createEmailMessage(String email, String code)
        throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(email);
        helper.setSubject("멘토 등록 이메일 인증");

        Context context = new Context();
        context.setVariable("code", code);
        String logoBase64 = getBase64Image("logo.png");
        String footerBase64 = getBase64Image("footer.png");
        context.setVariable("logoImage", "data:image/png;base64," + logoBase64);
        context.setVariable("footerImage", "data:image/png;base64," + footerBase64);
        String htmlContent = templateEngine.process("email/verification", context);

        helper.setText(htmlContent, true);

        return message;
    }

    private String getBase64Image(String imagePath) throws IOException {
        Resource resource = new ClassPathResource("static/esset/" + imagePath);
        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
        return Base64.getEncoder().encodeToString(bytes);
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

        // 인증 성공시 Member 엔티티 업데이트
        Member member = memberService.getMemberByEmail(email);
        member.verifyEmail();

        // 인증 성공 시 Redis에서 코드 삭제
        redisTemplate.delete(redisKey);

        return new EmailVerificationResponseDto(requestDto.getEmail(), "이메일 인증에 성공했습니다.");
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}