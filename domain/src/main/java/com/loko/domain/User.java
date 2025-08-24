package com.loko.domain;

import java.util.Collection;
import java.util.HashSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean isActive = false;
    private Role role;
    private Collection<Object> authorities = new HashSet<>();
}
