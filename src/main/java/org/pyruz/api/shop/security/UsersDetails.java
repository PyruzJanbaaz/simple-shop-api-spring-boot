package org.pyruz.api.shop.security;

import org.pyruz.api.shop.model.entity.User;
import org.pyruz.api.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.pyruz.api.shop.configuration.ApplicationProperties;
import org.pyruz.api.shop.configuration.ApplicationContextHolder;
import org.pyruz.api.shop.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsersDetails extends ApplicationContextHolder implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findUserByUsernameIgnoreCase(username);
        if (!user.isPresent()) {
            throw ServiceException.builder()
                    .code(applicationProperties.getCode("application.message.invalidLogin.code"))
                    .message(applicationProperties.getProperty("application.message.invalidLogin.text"))
                    .httpStatus(HttpStatus.UNAUTHORIZED).build();
        }
        if (Boolean.FALSE.equals(user.get().getIsActive())) {
            throw ServiceException.builder()
                    .code(applicationProperties.getCode("application.message.user.locked.code"))
                    .message(applicationProperties.getProperty("application.message.user.locked.text"))
                    .httpStatus(HttpStatus.LOCKED).build();
        }
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        user.get().getRoles().forEach(role -> simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getTitle())));
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.get().getPassword())
                .authorities(simpleGrantedAuthorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }

}
