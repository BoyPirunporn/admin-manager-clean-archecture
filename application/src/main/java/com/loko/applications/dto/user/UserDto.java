package com.loko.applications.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto{
        private String id;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private boolean isActive;
        private String role;
}
