package com.loko.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends BaseModel{
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean isActive = false;
    private boolean isEmailVerify = false;
    private Date verifyAt;
    private Role role;
    private Collection<Object> authorities = new HashSet<>();
}
