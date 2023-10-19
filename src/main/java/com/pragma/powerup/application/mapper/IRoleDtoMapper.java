package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.RoleDto;
import com.pragma.powerup.domain.model.RoleModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import javax.management.relation.Role;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRoleDtoMapper {
    RoleDto toDto(RoleModel roleModel);
}
