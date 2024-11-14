package boomerang.email.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailSender emailSender;

    private String testEmail;
    private String testCode;

    @BeforeEach
    void setUp() {
        testEmail = "test@example.com";
        testCode = "123456";
        ReflectionTestUtils.setField(emailSender, "fromEmail", "noreply@boomerang.com");
    }

    @Test
    void testSendEmail_Success() {
        // given
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("email/verification"), any()))
                .thenReturn("<html>Test Email Content</html>");

        // when
        emailSender.sendEmail(testEmail, testCode);

        // then
        verify(mailSender).send(any(MimeMessage.class));
        verify(templateEngine).process(eq("email/verification"), any());
    }

    @Test
    void testSendEmail_HandlesException() {
        // given
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("email/verification"), any()))
                .thenReturn("<html>Test Email Content</html>");
        doThrow(new MailSendException("Failed to send email"))
                .when(mailSender).send(any(MimeMessage.class));

        // when
        emailSender.sendEmail(testEmail, testCode);

        // then
        verify(mailSender).send(any(MimeMessage.class));
    }
}