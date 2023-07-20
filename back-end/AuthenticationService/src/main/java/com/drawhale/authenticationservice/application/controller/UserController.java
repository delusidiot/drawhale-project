package com.drawhale.authenticationservice.application.controller;

import com.drawhale.authenticationservice.domain.user.dto.UserDto;
import com.drawhale.authenticationservice.domain.user.service.UserService;
import com.drawhale.authenticationservice.domain.user.vo.RegisterUserCommand;
import com.drawhale.authenticationservice.domain.user.vo.ResponseUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper mapper;

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> register(
            @Valid @RequestBody RegisterUserCommand user
    ) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        UserDto savedUser = userService.createUser(userDto);
        ResponseUser responseUser = mapper.map(savedUser, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(
            @PathVariable("userId") String userId
    ) {
        UserDto userDto = userService.getUserByUserId(userId);
        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.ok(responseUser);
    }
}
