package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    public UserUseCase(IUserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public UserModel saveUser(UserModel userModel) {

        return userPersistencePort.saveUser(userModel);
    }

    @Override
    public Optional<UserEntity> findUserByEmailOptional(String email) {
        return userPersistencePort.findUserByEmailOptional(email);
    }

    @Override
    public UserModel findUserByEmail(String email) {
        return userPersistencePort.findUserByEmail(email);

    }

    @Override
    public UserModel getById(Long userId) {
        return userPersistencePort.getById(userId);
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userPersistencePort.getAllUsers();
    }
    @Override
    public UserModel me(Authentication authentication) {
        return userPersistencePort.me(authentication);
    }

}