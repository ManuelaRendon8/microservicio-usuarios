package com.pragma.powerup.domain.model;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private Long id;
    private String name;
    private String lastName;
    private String numberId;
    private String numberPhone;
    private Date dateBirth;
    private String email;
    private String password;
    private RoleModel role;

}
