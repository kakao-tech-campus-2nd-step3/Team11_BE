package boomerang.email.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private static final long VERIFICATION_TTL = 5L; // 5분

    @Async
    public void sendVerificationEmail(String email) {
        String verificationCode = generateVerificationCode();

        System.out.println("pass: " + verificationCode);

        // Redis에 인증 코드 저장
        String redisKey = "email:verification:" + email;
        redisTemplate.opsForValue()
            .set(redisKey, verificationCode, VERIFICATION_TTL, TimeUnit.MINUTES);

        // 이메일 발송
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("멘토 등록 이메일 인증");
        mailMessage.setText("인증 코드: " + verificationCode);

        System.out.println(mailMessage);

        mailSender.send(mailMessage);

        System.out.println("pass1");
    }

    @Transactional(readOnly = true)
    public boolean verifyEmail(String email, String code) {
        String redisKey = "email:verification:" + email;
        String savedCode = redisTemplate.opsForValue().get(redisKey);

        System.out.println(redisKey);
        System.out.println(savedCode);

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