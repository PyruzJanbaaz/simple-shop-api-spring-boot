package org.pyruz.api.shop.model.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.pyruz.api.shop.model.enums.Gender;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserSignupRequest {

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "application.message.passwordError.text")
    private String password;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "application.message.passwordError.text")
    private String confirmPassword;

    @NotBlank
    @Size(max = 250)
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @NotBlank
    @Size(min = 11, max = 11)
    @Pattern(regexp = "^(09[0-9]*)$")
    @ApiModelProperty(name = "mobileNumber", value = "09120137195", required = true)
    private String mobileNumber;

    @NotBlank
    @ApiModelProperty(name = "firstName", value = "Pyruz", required = true)
    private String firstName;

    @NotBlank
    @ApiModelProperty(name = "lastName", value = "Janbaaz", required = true)
    private String lastName;

    @NotNull
    @ApiModelProperty(name = "gender", value = "MALE", required = true)
    private Gender gender;
}
