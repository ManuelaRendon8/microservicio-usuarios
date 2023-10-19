package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.RoleDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        uses = {IRoleDtoMapper.class})
public interface IUserResponseMapper {
    IRoleDtoMapper INSTANCE = Mappers.getMapper(IRoleDtoMapper.class);
    @Mapping(target = "role.name", source = "roleDto.name")
    @Mapping(target = "role.description", source = "roleDto.description")
    @Mapping(target = "name", source = "user.name")
    UserResponseDto toResponse(UserModel user, RoleDto roleDto);
}
