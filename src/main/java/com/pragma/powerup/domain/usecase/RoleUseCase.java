package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRoleServicePort;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.spi.IRolePersistencePort;

public class RoleUseCase implements IRoleServicePort {
    private final IRolePersistencePort rolPersistencePort;
    public RoleUseCase(IRolePersistencePort rolPersistencePort){
        this.rolPersistencePort = rolPersistencePort;
    }
    @Override
    public RoleModel getRole(Long idRole) {
        return rolPersistencePort.getRole(idRole);
    }
}
