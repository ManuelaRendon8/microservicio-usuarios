package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface IUserPersistencePort {
    UserModel saveUser(UserModel userModel);

    Optional<UserEntity> findUserByEmailOptional(String email);
    UserModel findUserByEmail(String email);
    UserModel getById(Long userId);

    List<UserModel> getAllUsers();
    UserModel me(Authentication authentication);
}