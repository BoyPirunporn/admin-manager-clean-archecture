package com.loko.infrastructure.persistence.smtp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.loko.applications.ports.out.email.EmailSenderRepositoryPort;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class SmtpEmailSenderAdapter implements EmailSenderRepositoryPort {
    private final JavaMailSender mailSender;

    @Value("${application.frontend.url}")
    private String frontUrl;

    public SmtpEmailSenderAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String to, String username, String token) {
        String verifyEmailUrl = frontUrl + "?token=" + token;
        String subject = "Please verify your account";
        String message = "Hello " + username + "\n\n" + "Please click the link below to verify your email address:\n"
                + verifyEmailUrl;

        MimeMessage email = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);

            mailSender.send(email);
        } catch (MessagingException ex) {
            System.out.println("Messaging Exception " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        } catch (Exception e) {
            throw e;
        }

    }

}
