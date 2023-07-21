package com.drawhale.authenticationservice.domain.user.service;

import com.drawhale.authenticationservice.application.exception.UserNotFoundException;
import com.drawhale.authenticationservice.domain.user.dto.UserDto;
import com.drawhale.authenticationservice.domain.user.entity.UserEntity;
import com.drawhale.authenticationservice.domain.user.repository.UserRepository;
import com.drawhale.authenticationservice.domain.user.vo.LoginCommand;
import com.drawhale.authenticationservice.domain.user.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReadService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final AuthenticationManager authenticationManager;

    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not Found"));
        UserDto userDto = mapper.map(userEntity, UserDto.class);
        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);
        return userDto;
    }

    public UserDto authenticate(LoginCommand command) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.getEmail(),
                        command.getPassword()
                )
        );
        UserEntity userEntity = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User Not Found: " + command.getEmail()));
        return mapper.map(userEntity, UserDto.class);
    }
}
