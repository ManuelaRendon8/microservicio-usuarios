package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IRoleEntityMapper {
    RoleEntity toRoleEntity(RoleModel role);
    RoleModel toRole(RoleEntity roleEntity);
    List<RoleModel> toRoleList(List<RoleEntity> roleEntityList);
}
