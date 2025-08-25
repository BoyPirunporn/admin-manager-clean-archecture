package com.loko.infrastructure.services;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.loko.applications.ports.in.user.VerificationUseCase;
import com.loko.applications.ports.out.user.UserRepositoryPort;
import com.loko.applications.ports.out.verification.VerificationTokenRepositoryPort;
import com.loko.domain.User;
import com.loko.domain.VerificationToken;
import com.loko.domain.exception.ResourceNotFoundException;
import com.loko.domain.exception.UnauthorizeException;

@Service
public class VerificationService implements VerificationUseCase {
    private final VerificationTokenRepositoryPort repositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public VerificationService(VerificationTokenRepositoryPort repositoryPort,UserRepositoryPort userRepositoryPort) {
        this.repositoryPort = repositoryPort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public void verifyEmail(String token) {
        VerificationToken verificationToken = repositoryPort.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid verification token."));
        
        if(verificationToken.getExpiryDate().isBefore(Instant.now())){
            throw new UnauthorizeException("Expired Verification token.");
        }


        // update user
        User user = verificationToken.getUser();

        user.setActive(true);
        user.setEmailVerify(true);
        user.setVerifyAt(Date.from(Instant.now()));
        
        userRepositoryPort.save(user);

        // delete verifyToken
        repositoryPort.delete(verificationToken);
    }

}
