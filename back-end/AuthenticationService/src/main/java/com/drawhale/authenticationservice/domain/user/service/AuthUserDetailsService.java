package com.drawhale.authenticationservice.domain.user.service;

import com.drawhale.authenticationservice.domain.user.dto.AuthUserDetails;
import com.drawhale.authenticationservice.domain.user.entity.UserEntity;
import com.drawhale.authenticationservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user with that " + username));
        HashSet<GrantedAuthority> grantedAuthorities = new HashSet<>();
        return new AuthUserDetails(
                userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                true, true, true, true, grantedAuthorities
        );
    }
}
