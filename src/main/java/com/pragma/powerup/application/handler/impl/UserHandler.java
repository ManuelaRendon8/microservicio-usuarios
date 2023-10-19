package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.AuthenticationRequestDto;
import com.pragma.powerup.application.dto.request.EmployeeRestaurantAssignmentRequestDto;
import com.pragma.powerup.application.dto.request.UserRequestDto;
import com.pragma.powerup.application.dto.response.JwtResponseDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.application.handler.IJwtHandler;
import com.pragma.powerup.application.handler.IUserHandler;
import com.pragma.powerup.application.mapper.IRoleDtoMapper;
import com.pragma.powerup.application.mapper.IUserRequestMapper;
import com.pragma.powerup.application.mapper.IUserResponseMapper;
import com.pragma.powerup.common.exception.LegalAgeException;
import com.pragma.powerup.common.exception.RepeatUserException;
import com.pragma.powerup.domain.api.IRoleServicePort;
import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.infrastructure.input.rest.Plazoleta.IPlazoletaFeignClient;
import com.pragma.powerup.infrastructure.out.jpa.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final IRoleServicePort roleServicePort;
    private final IUserRequestMapper userRequestMapper;
    private final IUserResponseMapper userResponseMapper;
    private final IRoleDtoMapper roleDtoMapper;
    private final AuthenticationManager authenticationManager;
    private final IJwtHandler jwtHandler;


    @Override
    public UserResponseDto register(UserRequestDto userRequestDto) {
        RoleModel role = roleServicePort.getRole(userRequestDto.getRoleId());
        UserModel user = userRequestMapper.toUser(userRequestDto);

        if (repeatUser(user.getEmail())) {
            throw new RepeatUserException();
        }
        if(user.getDateBirth()!=null){
            if (!legalAge(user.getDateBirth())) {
                throw new LegalAgeException();
            }
        }

        user.setRole(role);
        return userResponseMapper.toResponse(userServicePort.saveUser(user), roleDtoMapper.toDto(role));

    }

    private boolean repeatUser(String email) {
        return userServicePort.findUserByEmailOptional(email).isPresent();
    }

    @Override
    public JwtResponseDto login(AuthenticationRequestDto authenticationRequestDto) {
        JwtResponseDto jwtResponseDto = new JwtResponseDto();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequestDto.getEmail(),
                        authenticationRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        var user = userServicePort.findUserByEmailOptional(authenticationRequestDto.getEmail()).orElseThrow();
        var jwtToken = jwtHandler.generateToken(user);

        jwtResponseDto.setToken(jwtToken);
        jwtResponseDto.setBearer(userEntity.getEmail());
        jwtResponseDto.setUserName(userEntity.getName());
        jwtResponseDto.setAuthorities(userEntity.getAuthorities());

        return jwtResponseDto;
    }
    @Override
    public UserResponseDto me(Authentication authentication) {
        return userRequestMapper.toDto(userServicePort.me(authentication));
    }


    private boolean legalAge(Date dateBirth) {

        LocalDate birthDate = dateBirth.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();

        Period age = Period.between(birthDate, currentDate);

        int years = age.getYears();

        return years >= 18;
    }

    @Override
    public UserResponseDto getById(Long userId) {
        UserModel user = userServicePort.getById(userId);
        return userRequestMapper.toDto(user);
    }

    @Override
    public UserResponseDto getByEmail(String email) {
        UserModel user = userServicePort.findUserByEmail(email);
        return userRequestMapper.toDto(user);
    }


}