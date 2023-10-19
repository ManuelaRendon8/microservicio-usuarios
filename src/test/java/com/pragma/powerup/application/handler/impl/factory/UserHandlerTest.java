package com.pragma.powerup.application.handler.impl.factory;

import com.pragma.powerup.application.dto.request.RegisterRequestDto;
import com.pragma.powerup.application.dto.request.UserRequestDto;
import com.pragma.powerup.application.dto.response.ResponseDto;
import com.pragma.powerup.application.dto.response.RoleDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.application.handler.IJwtHandler;
import com.pragma.powerup.application.handler.impl.UserHandler;
import com.pragma.powerup.application.handler.impl.factory.FactoryUserDataTest;
import com.pragma.powerup.application.mapper.IRoleDtoMapper;
import com.pragma.powerup.application.mapper.IUserRequestMapper;
import com.pragma.powerup.application.mapper.IUserResponseMapper;
import com.pragma.powerup.common.exception.RepeatUserException;
import com.pragma.powerup.domain.api.IRoleServicePort;
import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.infrastructure.configuration.FeingClientInterceptorImp;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import com.pragma.powerup.infrastructure.input.rest.Plazoleta.IPlazoletaFeignClient;
import com.pragma.powerup.infrastructure.out.jpa.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserHandlerTest {
    @InjectMocks
    UserHandler userHandler;
    @Mock
    IRoleServicePort roleServicePort;
    @Mock
    IUserRequestMapper userRequestMapper;

    @Mock
    IUserServicePort userServicePort;
    @Mock
    IUserResponseMapper userResponseMapper;

    @Mock
    IRoleDtoMapper roleDtoMapper;

    @Test
    void mustRegisterAUser() {
        UserRequestDto userRequestDto = FactoryUserDataTest.getUserRequestDto();
        RoleModel roleModel = FactoryUserDataTest.getRolModel(userRequestDto.getRoleId());
        UserResponseDto userResponseDto = FactoryUserDataTest.getUserResponseDto();
        UserModel userModel = FactoryUserDataTest.getUserModel(2L);
        RoleDto rolResponseDto = FactoryUserDataTest.getRolResponseDto();

        when(roleServicePort.getRole(userRequestDto.getRoleId())).thenReturn(roleModel);
        when(userServicePort.findUserByEmailOptional(any())).thenReturn(Optional.empty());
        when(userRequestMapper.toUser(any())).thenReturn(userModel);
        when(roleDtoMapper.toDto(any())).thenReturn(rolResponseDto);
        when(userResponseMapper.toResponse(any(), any())).thenReturn(userResponseDto);

        Assertions.assertEquals(userResponseDto, userHandler.register(userRequestDto));

        verify(userServicePort).saveUser(any(UserModel.class));
    }
}




