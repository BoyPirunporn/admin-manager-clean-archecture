package com.loko.applications.ports.out.verification;

import java.util.Optional;

import com.loko.domain.VerificationToken;

public interface VerificationTokenRepositoryPort {
    VerificationToken save(VerificationToken verificationToken);
    Optional<VerificationToken> findByToken(String token);
    boolean existsByToken(String token);
    void delete(VerificationToken token);
}
