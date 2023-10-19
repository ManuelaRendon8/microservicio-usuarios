package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.AuthenticationRequestDto;
import com.pragma.powerup.application.dto.request.UserRequestDto;
import com.pragma.powerup.application.dto.response.JwtResponseDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;
import org.springframework.security.core.Authentication;

public interface IUserHandler {

    UserResponseDto register(UserRequestDto userRequestDto);

    UserResponseDto getById(Long userId);

    UserResponseDto getByEmail(String email);

    JwtResponseDto login(AuthenticationRequestDto authenticationRequestDto);
    UserResponseDto me(Authentication authentication);
}