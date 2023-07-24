package com.drawhale.authenticationservice.domain.user.service;

import com.drawhale.authenticationservice.application.exception.UserNotFoundException;
import com.drawhale.authenticationservice.domain.token.entity.TokenEntity;
import com.drawhale.authenticationservice.domain.token.entity.TokenType;
import com.drawhale.authenticationservice.domain.token.repository.TokenRepository;
import com.drawhale.authenticationservice.domain.token.service.JwtService;
import com.drawhale.authenticationservice.domain.token.vo.AuthenticationResponse;
import com.drawhale.authenticationservice.domain.user.dto.AuthUserDetails;
import com.drawhale.authenticationservice.domain.user.dto.UserDto;
import com.drawhale.authenticationservice.domain.user.entity.UserEntity;
import com.drawhale.authenticationservice.domain.user.repository.UserRepository;
import com.drawhale.authenticationservice.domain.user.vo.LoginCommand;
import com.drawhale.authenticationservice.domain.user.vo.ResponseOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReadService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final ModelMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final StringRedisTemplate redisTemplate;

    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not Found"));
        UserDto userDto = mapper.map(userEntity, UserDto.class);
        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);
        return userDto;
    }

    public AuthenticationResponse authenticate(LoginCommand command) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.getEmail(),
                        command.getPassword()
                )
        );
        UserEntity userEntity = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User Not Found: " + command.getEmail()));
        UserDetails user = AuthUserDetails.builder()
                .email(userEntity.getEmail())
                .userId(userEntity.getUserId())
                .build();
        String accessToken = jwtService.generateAccessToken(user);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(command.getEmail(), accessToken);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserRefreshToken(userEntity, refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserRefreshToken(UserEntity userEntity, String refreshToken) {
        TokenEntity tokenEntity = TokenEntity.builder()
                .user(userEntity)
                .refreshToken(refreshToken)
                .tokeType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        revokeAllUserTokens(userEntity);
        tokenRepository.save(tokenEntity);
    }

    private void revokeAllUserTokens(UserEntity userEntity) {
        List<TokenEntity> validTokens = tokenRepository.findAllValidTokensByUser(userEntity.getId());
        if (validTokens.isEmpty())
            return;
        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            UserEntity userEntity = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("Token User Not Found"));
            AuthUserDetails user = AuthUserDetails.builder().userId(userEntity.getUserId()).email(userEntity.getEmail()).build();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateAccessToken(user);
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}
