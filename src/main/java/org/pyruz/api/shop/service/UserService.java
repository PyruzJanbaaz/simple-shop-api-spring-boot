package org.pyruz.api.shop.service;

import org.pyruz.api.shop.configuration.ApplicationProperties;
import org.pyruz.api.shop.exception.ServiceException;
import org.pyruz.api.shop.model.domain.UserSignupRequest;
import org.pyruz.api.shop.model.domain.UserVerificationRequest;
import org.pyruz.api.shop.model.dto.BaseDTO;
import org.pyruz.api.shop.model.dto.MetaDTO;
import org.pyruz.api.shop.model.dto.UserAuthenticationDTO;
import org.pyruz.api.shop.model.entity.Contact;
import org.pyruz.api.shop.model.entity.User;
import org.pyruz.api.shop.repository.ContactRepository;
import org.pyruz.api.shop.repository.RoleRepository;
import org.pyruz.api.shop.repository.UserRepository;
import org.pyruz.api.shop.security.JwtTokenProvider;
import org.pyruz.api.shop.service.intrface.IUserService;
import org.pyruz.api.shop.utility.ApplicationUtilities;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService extends BaseService implements IUserService {

    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final ContactRepository contactRepository;
    final PasswordEncoder passwordEncoder;
    final AuthenticationManager authenticationManager;

    protected UserService(JwtTokenProvider jwtTokenProvider, ApplicationProperties applicationProperties, UserRepository userRepository, RoleRepository roleRepository, ContactRepository contactRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        super(jwtTokenProvider, applicationProperties);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.contactRepository = contactRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(
                () -> ServiceException.builder()
                        .code(applicationProperties.getCode("application.message.notFoundRecord.code"))
                        .message(applicationProperties.getProperty("application.message.notFoundRecord.text"))
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build()
        );
    }

    @Override
    public BaseDTO lockUser(String username) {
        User user = findUserByUsername(username);
        user.setIsActive(false);
        userRepository.save(user);
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .build();
    }

    @Override
    public BaseDTO unlockUser(String username) {
        User user = findUserByUsername(username);
        user.setIsActive(true);
        userRepository.save(user);
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .build();
    }

    @Override
    @Transactional
    public BaseDTO signup(UserSignupRequest userSignupRequest) {
        User user = User.builder()
                .username(userSignupRequest.getMobileNumber())
                .password(passwordEncoder.encode(userSignupRequest.getPassword()))
                .firstName(userSignupRequest.getFirstName())
                .lastName(userSignupRequest.getLastName())
                .gender(userSignupRequest.getGender().name())
                .roles(Collections.singletonList(roleRepository.findRoleByTitleIgnoreCase("ROLE_USER")))
                .build();
        Optional<User> existUser = userRepository.findUserByUsername(userSignupRequest.getMobileNumber());
        if (existUser.isPresent()) {
            user.setId(existUser.get().getId());
        }
        user.setIsActive(false);
        user = userRepository.save(user);

        Contact contact = Contact.builder()
                .user(user)
                .mobileNumber(userSignupRequest.getMobileNumber())
                .build();
        Optional<Contact> existContact = contactRepository.findContactByMobileNumber(userSignupRequest.getMobileNumber());
        if (existContact.isPresent()) {
            contact = existContact.get();
            contact.setUser(user);
        }
        contactRepository.save(ApplicationUtilities.getInstance(applicationProperties).generatePinCode(contact));
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .data(contact.getTempPinCode().toString())
                .build();
    }

    public BaseDTO verifyUser(UserVerificationRequest userVerificationRequest) {
        Contact contact = contactRepository.findContactByMobileNumber(userVerificationRequest.getMobileNumber()).orElseThrow(
                () -> ServiceException.builder()
                        .code(applicationProperties.getCode("user-not-found-code"))
                        .message(applicationProperties.getProperty("user-not-found-text"))
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build()
        );
        checkVerificationCode(contact, userVerificationRequest);
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .build();
    }

    public BaseDTO signin(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username.toLowerCase(), password));
        Set<String> roles = new LinkedHashSet<>();
        authentication.getAuthorities().forEach(i -> roles.add(i.toString()));
        return BaseDTO.builder()
                .meta(MetaDTO.getInstance(applicationProperties))
                .data(
                        UserAuthenticationDTO.builder()
                                .token(jwtTokenProvider.createToken(username.toLowerCase(), Long.valueOf(applicationProperties.getProperty("admin.security.jwt.token.expire.time"))))
                                .roles(roles)
                                .build())
                .build();

    }


    private Boolean checkVerificationCode(Contact contact, UserVerificationRequest userVerificationRequest) {
        Calendar calendar = Calendar.getInstance();
        Date currentDateTime = calendar.getTime();
        if (contact.getTempPinCode() == Integer.parseInt(userVerificationRequest.getPinCode())) {
            if (!currentDateTime.after(contact.getExpirationPinCode())) {
                contact.setPinCode(contact.getTempPinCode());
                contact.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                contactRepository.save(contact);
                User user = findUserByUsername(userVerificationRequest.getMobileNumber());
                user.setIsActive(true);
                userRepository.save(user);
            } else {
                // ACTIVATION CODE WAS EXPIRED
                throw ServiceException.builder()
                        .code(applicationProperties.getCode("registration-pin-code-was-expired-code"))
                        .message(applicationProperties.getProperty("registration-pin-code-was-expired-text"))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }
        } else {
            //INVALID ACTIVATION CODE
            throw ServiceException.builder()
                    .code(applicationProperties.getCode("registration-invalid-pin-code-code"))
                    .message(applicationProperties.getProperty("registration-invalid-pin-code-text"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return true;
    }

}
