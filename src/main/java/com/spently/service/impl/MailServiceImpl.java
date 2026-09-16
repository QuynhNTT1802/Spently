package com.spently.service.impl;

import com.spently.config.Constant;
import com.spently.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    @Override
    public void sendForgotPasswordMail(String toEmail, String username, String rawPassword, String token) {

        String verifyLink =
                "http://localhost:9999"
                        + "/verify-reset-password"
                        + "?username=" + username
                        + "&token=" + token;

        String subject = "[Nipa] Reset Password Verification";

        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("rawPassword", rawPassword);
        context.setVariable("verifyLink", verifyLink);

        String htmlContent =
                templateEngine.process("mail/reset-password", context);

        sendMail(toEmail, subject, htmlContent);
    }

    private void sendMail(String toMail, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("nt.thuyquynh1802@gmail.com");
            helper.setTo(toMail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new IllegalStateException(Constant.ERROR_EMAIL_SEND, e);
        }
    }

}
