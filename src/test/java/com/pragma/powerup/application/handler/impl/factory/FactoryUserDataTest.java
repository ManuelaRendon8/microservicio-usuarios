package com.pragma.powerup.application.handler.impl.factory;

import com.pragma.powerup.application.dto.request.RegisterRequestDto;
import com.pragma.powerup.application.dto.request.UserRequestDto;
import com.pragma.powerup.application.dto.response.ResponseDto;
import com.pragma.powerup.application.dto.response.RoleDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.RoleEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class FactoryUserDataTest {
    public static UserRequestDto getUserRequestDto() {
        UserRequestDto userRequestDto = new UserRequestDto();

        userRequestDto.setName("Usuario");
        userRequestDto.setLastName("Prueba");
        userRequestDto.setNumberId("1193078576");
        userRequestDto.setNumberPhone("+573148022302");
        userRequestDto.setEmail("Usuario@gmail.com");
        userRequestDto.setPassword("12345678");
        userRequestDto.setRoleId(2L);

        return userRequestDto;
    }

    public static UserResponseDto getUserResponseDto() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setName("Usuario");
        userResponseDto.setLastName("Prueba");
        userResponseDto.setNumberId("1193078576");
        userResponseDto.setNumberPhone("+573148022302");
        userResponseDto.setEmail("Usuario@gmail.com");
        userResponseDto.setPassword("12345678");
        userResponseDto.setRole(getRoleDto(2));

        return userResponseDto;
    }
    public static RoleDto getRoleDto(int rol) {
        RoleDto roleDto= new RoleDto();
        if(rol==1){
            roleDto.setName("ADMINISTRADOR");
            roleDto.setDescription("Puede crear restaurantes y propietarios");
        }else if(rol==2){
            roleDto.setName("PROPIETARIO");
            roleDto.setDescription("Puede administrar restaurantes");
        }else if(rol==3){
            roleDto.setName("EMPLEADO");
            roleDto.setDescription("Puede administrar pedidos");
        } else if (rol==4) {
            roleDto.setName("CLIENTE");
            roleDto.setDescription("Puede consultar restaurantes y hacer pedidos");
        }
        return roleDto;
    }
    public static UserEntity getUserEntity2() {
        UserEntity userEntity = new UserEntity();

        userEntity.setId(1L);
        userEntity.setName("Usuario");
        userEntity.setLastName("Prueba");
        userEntity.setNumberId("1193078576");
        userEntity.setNumberPhone("+573148022302");
        userEntity.setEmail("Usuario@gmail.com");
        userEntity.setPassword("12345678");
        userEntity.setRole(getRolEntity(2L));

        return userEntity;
    }


    public static UserModel getUserModel(Long roleId) {
        UserModel userModel = new UserModel();

        userModel.setId(1L);
        userModel.setName("Manuela");
        userModel.setLastName("Rendon");
        userModel.setNumberId("1193078576");
        userModel.setNumberPhone("+573148022302");
        userModel.setEmail("amraga10@gmail.com");
        userModel.setPassword("1234");
        userModel.setRole(getRolModel(roleId));

        return userModel;
    }

    public static UserEntity getUserEntity() {
        UserEntity userEntity = new UserEntity();

        userEntity.setId(1L);
        userEntity.setName("Manuela");
        userEntity.setLastName("Rendon");
        userEntity.setNumberPhone("1193078576");
        userEntity.setNumberPhone("+573148022302");
        userEntity.setEmail("amraga10@gmail.com");
        userEntity.setPassword("1234");
        userEntity.setRole(getRolEntity());

        return userEntity;
    }

    public static RoleEntity getRolEntity(Long rol) {
        RoleEntity roleEntity = new RoleEntity();

        if(rol==1){
            roleEntity.setId(1L);
            roleEntity.setName("ADMINISTRADOR");
            roleEntity.setDescription("Puede crear restaurantes y propietarios");
        }else if(rol==2){
            roleEntity.setId(2L);
            roleEntity.setName("PROPIETARIO");
            roleEntity.setDescription("Puede administrar restaurantes");
        }else if(rol==3){
            roleEntity.setId(3L);
            roleEntity.setName("EMPLEADO");
            roleEntity.setDescription("Puede administrar pedidos");
        } else if (rol==4) {
            roleEntity.setId(4L);
            roleEntity.setName("CLIENTE");
            roleEntity.setDescription("Puede consultar restaurantes y hacer pedidos");
        }
        return roleEntity;
    }
    public static RoleModel getRolModel(Long rol) {
        RoleModel rolModel = new RoleModel();

        if(rol==1){
            rolModel.setId(1L);
            rolModel.setName("ADMINISTRADOR");
            rolModel.setDescription("Puede crear restaurantes y propietarios");
        }else if(rol==2){
            rolModel.setId(2L);
            rolModel.setName("PROPIETARIO");
            rolModel.setDescription("Puede administrar restaurantes");
        }else if(rol==3){
            rolModel.setId(3L);
            rolModel.setName("EMPLEADO");
            rolModel.setDescription("Puede administrar pedidos");
        } else if (rol==4) {
            rolModel.setId(4L);
            rolModel.setName("CLIENTE");
            rolModel.setDescription("Puede consultar restaurantes y hacer pedidos");
        }
        return rolModel;
    }

    public static RoleEntity getRolEntity() {
        RoleEntity rolEntity = new RoleEntity();

        rolEntity.setId(1L);
        rolEntity.setName("ROLE_ADMINISTRADOR");
        rolEntity.setDescription("Administrador");

        return rolEntity;
    }

    public static RoleDto getRolResponseDto() {
        RoleDto rolResponseDto = new RoleDto();

        rolResponseDto.setName("ROLE_ADMINISTRADOR");
        rolResponseDto.setDescription("Administrador");

        return rolResponseDto;
    }



    public static ResponseDto getResponseClientDto() {
        ResponseDto responseClientDto = new ResponseDto();

        responseClientDto.setMessage("");
        responseClientDto.setError(false);
        responseClientDto.setData(getUserRequestDto());

        return responseClientDto;
    }

    public static ResponseEntity<ResponseDto> getResponseEntity() {
        ResponseDto responseClientDto = getResponseClientDto();
        return new ResponseEntity<>(responseClientDto, HttpStatus.FOUND);
    }



    public static RegisterRequestDto getRegisterRequestDto() {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();

        registerRequestDto.setName("Manuela");
        registerRequestDto.setLastname("Rendon");
        registerRequestDto.setDocumentNumber("1193078576");
        registerRequestDto.setPhone("+573148022302");
        registerRequestDto.setEmail("amraga10@gmail.com");
        registerRequestDto.setPassword("1234");

        return registerRequestDto;
    }
}
