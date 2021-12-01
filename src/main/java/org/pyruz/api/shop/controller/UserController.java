package org.pyruz.api.shop.controller;

import io.swagger.annotations.ApiParam;
import org.pyruz.api.shop.model.domain.UserSignupRequest;
import org.pyruz.api.shop.model.domain.UserVerificationRequest;
import org.pyruz.api.shop.model.dto.BaseDTO;
import org.pyruz.api.shop.service.intrface.IUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@RestController
public class UserController extends BaseController {

    final IUserService iUserService;

    public UserController(@Qualifier("userService") IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @PostMapping(value = "v1/user")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public BaseDTO signup(@Valid @RequestBody UserSignupRequest userSignupRequest) {
        return iUserService.signup(userSignupRequest);
    }

    @PutMapping(value = "v1/user/verification")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public BaseDTO verifyUser(@Valid @RequestBody UserVerificationRequest userVerificationRequest) {
        return iUserService.verifyUser(userVerificationRequest);
    }

    @GetMapping("v1/user/signin")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseDTO signin(@ApiParam(value = "09120137195", name = "username", required = true)
                          @RequestParam @Size(min = 3, max = 20) String username,
                          @ApiParam(value = "Aa123456", name = "password", required = true)
                          @RequestParam @Size(min = 5, max = 20) String password) {
        return iUserService.signin(username, password);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("v1/user/lock")
    @ResponseStatus(HttpStatus.OK)
    public BaseDTO lockUser(@RequestParam String username) {
        return iUserService.lockUser(username);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("v1/user/unlock")
    @ResponseStatus(HttpStatus.OK)
    public BaseDTO unlockUser(@RequestParam String username) {
        return iUserService.unlockUser(username);
    }

}
