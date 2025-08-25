package com.loko.applications.ports.out.email;


public interface EmailSenderRepositoryPort {
    void sendVerificationEmail(String to,String username,String token);
}
