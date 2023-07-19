package com.drawhale.authenticationservice.service;

import com.drawhale.authenticationservice.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
}
