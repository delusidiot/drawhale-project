package com.drawhale.authenticationservice.domain.user.service;

import com.drawhale.authenticationservice.domain.user.dto.UserDto;
import com.drawhale.authenticationservice.domain.user.entity.UserEntity;
import com.drawhale.authenticationservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserWriteService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));

        UserEntity savedUser = userRepository.save(userEntity);
        return mapper.map(savedUser, UserDto.class);
    }
}
