package org.pyruz.api.shop.service.intrface;

import org.pyruz.api.shop.model.domain.UserSignupRequest;
import org.pyruz.api.shop.model.domain.UserVerificationRequest;
import org.pyruz.api.shop.model.dto.BaseDTO;

public interface IUserService {

    BaseDTO lockUser(String username);

    BaseDTO unlockUser(String username);

    BaseDTO signup(UserSignupRequest userSignUpRequest);

    BaseDTO verifyUser(UserVerificationRequest userVerificationRequest);

    BaseDTO signin(String username, String password);
}
