package com.pragma.powerup.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {
    private String token;
    private String bearer;
    private String userName;
    private Collection<? extends GrantedAuthority> authorities;
}
