package com.loko.infrastructure.services;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loko.applications.dto.auth.VerifyEmailRequestDto;
import com.loko.applications.dto.user.VerifyTokenResponseDto;
import com.loko.applications.ports.in.user.VerificationUseCase;
import com.loko.applications.ports.out.user.UserRepositoryPort;
import com.loko.applications.ports.out.verification.VerificationTokenRepositoryPort;
import com.loko.domain.User;
import com.loko.domain.VerificationToken;
import com.loko.domain.exception.BadRequestException;
import com.loko.domain.exception.ResourceNotFoundException;
import com.loko.domain.exception.UnauthorizeException;


@Service
@Transactional
public class VerificationService implements VerificationUseCase {
    private static final Logger logger = LoggerFactory.getLogger(VerificationService.class);
    private final VerificationTokenRepositoryPort repositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public VerificationService(VerificationTokenRepositoryPort repositoryPort,UserRepositoryPort userRepositoryPort,PasswordEncoder passwordEncoder) {
        this.repositoryPort = repositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void verifyEmail(VerifyEmailRequestDto dto) {
        if(!dto.password().equals(dto.confirmPassword())){
            throw new BadRequestException("Password doesn't match");
        }
        VerificationToken verificationToken = repositoryPort.findByToken(dto.token())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid verification token."));
        if(verificationToken.getExpiryDate().isBefore(Instant.now())){
            throw new UnauthorizeException("Expired Verification token.");
        }


        // update user
        User user = verificationToken.getUser();
        logger.info("USER ID -> {} ROLE ID {}" , user.getId(),user.getRole().getId());

        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setActive(true);
        user.setEmailVerify(true);
        user.setVerifyAt(Date.from(Instant.now()));
        userRepositoryPort.save(user);

        // delete verifyToken
        repositoryPort.delete(verificationToken);
    }

    @Override
    public VerifyTokenResponseDto verifyToken(String token) {
        System.out.println("TOKEN -> " + token);
        VerificationToken verificationToken = repositoryPort.findByToken(token).orElse(null);
        if(verificationToken == null){
            return new VerifyTokenResponseDto(false,"invalid");
        }
        if(verificationToken.getExpiryDate().isBefore(Instant.now())){
            return new VerifyTokenResponseDto(false,"expired");
        }
       return new VerifyTokenResponseDto(true,"valid");
    }

}
