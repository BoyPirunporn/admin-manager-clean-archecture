package com.loko.infrastructure.persistence.smtp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import com.loko.applications.ports.out.email.EmailSenderRepositoryPort;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class SmtpEmailSenderAdapter implements EmailSenderRepositoryPort {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${application.frontend.url}")
    private String frontUrl;

    public SmtpEmailSenderAdapter(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendVerificationEmail(String templateName, String to, String username, String token) {
        String verifyEmailUrl = frontUrl + "?token=" + token;
        String subject = "Please verify your account";
        String message = "Please click the button below to verify your email address and activate your account.";

        Context context = new Context();

        context.setVariable("MESSAGE", message);
        context.setVariable("VERIFY_LINK", verifyEmailUrl);
        context.setVariable("USERNAME", username);
        context.setVariable("WARNING_MESSAGE", "If you did not create an account, please ignore this email.");
        context.setVariable("COMPANY_NAME", "LoKo Entertainment .Co .Ltd,.");

        String template = (templateName.startsWith("/") ? templateName.substring(1) : templateName);
        System.out.println("TEMPLATE -> " + template);
        templateEngine.getTemplateResolvers().forEach(resolver -> {
            if (resolver instanceof FileTemplateResolver fileResolver) {
                System.out.println("Prefix -> " + fileResolver.getPrefix());
                System.out.println("Suffix -> " + fileResolver.getSuffix());
                System.out.println("TemplateMode -> " + fileResolver.getTemplateMode());
            } else if (resolver instanceof ClassLoaderTemplateResolver classLoaderResolver) {
                System.out.println("Prefix -> " + classLoaderResolver.getPrefix());
                System.out.println("Suffix -> " + classLoaderResolver.getSuffix());
                System.out.println("TemplateMode -> " + classLoaderResolver.getTemplateMode());
            }
        });
        String htmlContent = templateEngine.process(template, context);

        MimeMessage email = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(email);
        } catch (MessagingException ex) {
            System.out.println("Messaging Exception " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        } catch (Exception e) {
            throw e;
        }

    }

}
