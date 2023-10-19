package com.pragma.powerup.application.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String numberId;
    private String numberPhone;
    private Date dateBirth;
    private String email;
    private String Password;
    private RoleDto role;
}
