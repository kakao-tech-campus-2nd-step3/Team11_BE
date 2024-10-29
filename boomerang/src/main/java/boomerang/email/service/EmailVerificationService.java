package boomerang.email.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.thymeleaf.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import java.util.Base64;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private final SpringTemplateEngine templateEngine; // Thymeleaf 템플릿 엔진

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final long VERIFICATION_TTL = 5L; // 5분

    @Async
    public void sendVerificationEmail(String email) throws MessagingException, IOException {
        String verificationCode = generateVerificationCode();
        String redisKey = "email:verification:" + email;

        // Redis에 인증 코드 저장
        redisTemplate.opsForValue()
            .set(redisKey, verificationCode, VERIFICATION_TTL, TimeUnit.MINUTES);

        // 이메일 발송
        MimeMessage message = createEmailMessage(email, verificationCode);
        mailSender.send(message);
    }

    private MimeMessage createEmailMessage(String email, String code)
        throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(email);
        helper.setSubject("멘토 등록 이메일 인증");

        Context context = new Context();
        context.setVariable("code", code);  // template에서 ${code}로 사용
        String logoBase64 = getBase64Image("logo.svg");
        String footerBase64 = getBase64Image("footer.svg");
        context.setVariable("logoImage", "data:image/svg+xml;base64," + logoBase64);
        context.setVariable("footerImage", "data:image/svg+xml;base64," + footerBase64);
        String htmlContent = templateEngine.process("email/verification", context);

        helper.setText(htmlContent, true);

        return message;
    }

    private String getBase64Image(String imagePath) throws IOException {
        Resource resource = new ClassPathResource("static/esset/" + imagePath);
        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
        return Base64.getEncoder().encodeToString(bytes);
    }

    @Transactional(readOnly = true)
    public boolean verifyEmail(String email, String code) {
        String redisKey = "email:verification:" + email;
        String savedCode = redisTemplate.opsForValue().get(redisKey);

        if (savedCode == null) {
            throw new BusinessException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        if (!savedCode.equals(code)) {
            throw new BusinessException(ErrorCode.VERIFICATION_CODE_INVALID);
        }

        // 인증 성공 시 Redis에서 코드 삭제
        redisTemplate.delete(redisKey);

        return true;
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}