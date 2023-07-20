package com.drawhale.authenticationservice.domain.user.service;

import com.drawhale.authenticationservice.domain.user.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);
}
