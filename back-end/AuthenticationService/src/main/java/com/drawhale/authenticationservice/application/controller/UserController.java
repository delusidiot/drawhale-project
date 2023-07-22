package com.drawhale.authenticationservice.application.controller;

import com.drawhale.authenticationservice.domain.token.vo.AuthenticationResponse;
import com.drawhale.authenticationservice.domain.user.dto.UserDto;
import com.drawhale.authenticationservice.domain.user.service.UserReadService;
import com.drawhale.authenticationservice.domain.user.service.UserWriteService;
import com.drawhale.authenticationservice.domain.user.vo.LoginCommand;
import com.drawhale.authenticationservice.domain.user.vo.RegisterUserCommand;
import com.drawhale.authenticationservice.domain.user.vo.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserReadService userReadService;
    private final UserWriteService userWriteService;
    private final ModelMapper mapper;

    @PostMapping("/users")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterUserCommand user
    ) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        UserDto savedUser = userWriteService.createUser(userDto);
        UserResponse responseUser = mapper.map(savedUser, UserResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody LoginCommand request
    ) {
        return ResponseEntity.ok(userReadService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        userReadService.refreshToken(request, response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable("userId") String userId
    ) {
        UserDto userDto = userReadService.getUserByUserId(userId);
        UserResponse responseUser = mapper.map(userDto, UserResponse.class);
        return ResponseEntity.ok(responseUser);
    }
}
