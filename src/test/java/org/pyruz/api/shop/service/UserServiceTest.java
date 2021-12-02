package org.pyruz.api.shop.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.pyruz.api.shop.configuration.ApplicationProperties;
import org.pyruz.api.shop.exception.ServiceException;
import org.pyruz.api.shop.model.domain.UserSignupRequest;
import org.pyruz.api.shop.model.dto.BaseDTO;
import org.pyruz.api.shop.model.entity.Contact;
import org.pyruz.api.shop.model.entity.Role;
import org.pyruz.api.shop.model.entity.User;
import org.pyruz.api.shop.model.enums.Gender;
import org.pyruz.api.shop.repository.ContactRepository;
import org.pyruz.api.shop.repository.RoleRepository;
import org.pyruz.api.shop.repository.UserRepository;
import org.pyruz.api.shop.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Array;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static final String EXISTING_USERNAME ="Existing Username";
    private static final String NEW_USERNAME ="Not Available Username";
    private static final User USER = new User();
    private static final UserSignupRequest USER_SIGNUP_REQUEST = new UserSignupRequest();
    private static final Contact CONTACT = new Contact();

    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private final ContactRepository contactRepository = mock(ContactRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
    private final ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
    private final UserService userService = new UserService(
            jwtTokenProvider,
            applicationProperties,
            userRepository,
            roleRepository,
            contactRepository,
            passwordEncoder,
            authenticationManager
    );

    private final Authentication authentication = mock(Authentication.class);


    @BeforeAll
    static void init()
    {
        USER.setUsername(NEW_USERNAME);

        USER_SIGNUP_REQUEST.setEmail("pyruz.janbaz@gmail.com");
        USER_SIGNUP_REQUEST.setFirstName("Pyruz");
        USER_SIGNUP_REQUEST.setLastName("Janbaz");
        USER_SIGNUP_REQUEST.setGender(Gender.MALE);
        USER_SIGNUP_REQUEST.setMobileNumber("+989124404846");
        String password = "Egs82463@";
        USER_SIGNUP_REQUEST.setPassword(password);
        USER_SIGNUP_REQUEST.setConfirmPassword(password);


    }

    @Test
    void findUserByUsername() {

        when(userRepository.findUserByUsername(EXISTING_USERNAME)).thenReturn(Optional.of(USER));
        User foundUser=userService.findUserByUsername(EXISTING_USERNAME);
        assertEquals(foundUser, USER);

        when(userRepository.findUserByUsername(NEW_USERNAME)).thenReturn(Optional.empty());
        assertThrows(ServiceException.class,()->userService.findUserByUsername(NEW_USERNAME));

    }

    @Test
    void lockUser() {
        when(userRepository.findUserByUsername(EXISTING_USERNAME)).thenReturn(Optional.of(USER));
        when(userRepository.save(any(User.class))).thenReturn(USER);
        BaseDTO baseDTO = userService.lockUser(EXISTING_USERNAME);
        assertFalse(USER.getIsActive());
        assertNotNull(baseDTO);

        when(userRepository.findUserByUsername(NEW_USERNAME)).thenReturn(Optional.empty());
        assertThrows(ServiceException.class,()->userService.lockUser(NEW_USERNAME));
    }

    @Test
    void unlockUser() {
        USER.setIsActive(false);
        when(userRepository.findUserByUsername(EXISTING_USERNAME)).thenReturn(Optional.of(USER));
        when(userRepository.save(any(User.class))).thenReturn(USER);
        BaseDTO baseDTO = userService.unlockUser(EXISTING_USERNAME);
        assertTrue(USER.getIsActive());
        assertNotNull(baseDTO);

        when(userRepository.findUserByUsername(NEW_USERNAME)).thenReturn(Optional.empty());
        assertThrows(ServiceException.class,()->userService.unlockUser(NEW_USERNAME));


    }

    @Test
    void signup() {
        // New User wants to Sign up
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(USER);
        when(contactRepository.findContactByMobileNumber(anyString())).thenReturn(Optional.empty());
        when(contactRepository.save(any())).thenReturn(null);
        BaseDTO baseDTO = userService.signup(USER_SIGNUP_REQUEST);
        assertTrue(USER.getIsActive());
        assertNotNull(baseDTO);

        // Registered User wants to sign up again
        User otherUser = new User();
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(USER));
        when(userRepository.save(any(User.class))).thenReturn(USER);
        when(contactRepository.findContactByMobileNumber(anyString())).thenReturn(Optional.of(CONTACT));
        when(contactRepository.save(any())).thenReturn(CONTACT);
        baseDTO = userService.signup(USER_SIGNUP_REQUEST);
        assertNotNull(baseDTO);
        assertEquals(CONTACT.getUser(), USER);
    }


}