package com.loko.applications.ports.out.email;


public interface EmailSenderRepositoryPort {
    void sendVerificationEmail(String templateName,String to,String username,String token);
}
