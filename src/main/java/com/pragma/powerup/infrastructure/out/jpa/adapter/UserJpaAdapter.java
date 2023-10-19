package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import com.pragma.powerup.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserModel saveUser(UserModel userModel) {
        UserEntity userEntity = userEntityMapper.toUserEntity(userModel);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity user = userRepository.save(userEntity);
        return userEntityMapper.toUserModel(user);
    }

    @Override
    public Optional<UserEntity> findUserByEmailOptional(String email) {
        return userRepository.findByEmail(email);

    }

    @Override
    public UserModel findUserByEmail(String email) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
        UserEntity userEntity = userEntityOptional.get();
        UserModel userModel = userEntityMapper.toUserModel(userEntity);
        return userModel;
    }

    @Override
    public UserModel getById(Long userId) {

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(NoDataFoundException::new);
        return userEntityMapper.toUserModel(userEntity);
    }

    @Override
    public List<UserModel> getAllUsers() {
        List<UserEntity> entityList = userRepository.findAll();
        if (entityList.isEmpty()) {
            throw new NoDataFoundException();
        }
        return userEntityMapper.toUserModelList(entityList);
    }

    @Override
    public UserModel me(Authentication authentication) {
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        return userEntityMapper.toUserModel(userEntity);
    }

}