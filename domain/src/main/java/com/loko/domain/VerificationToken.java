package com.loko.domain;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationToken extends BaseModel{
    private String token;
    private User user;
    private Instant expiryDate;
}
