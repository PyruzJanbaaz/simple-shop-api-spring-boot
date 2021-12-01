package org.pyruz.api.shop.model.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserVerificationRequest {

  @NotBlank
  @Size(min = 11, max = 11)
  @Pattern(regexp = "^(09[0-9]*)$")
  @ApiModelProperty(name = "phoneNumber", value = "09120137195", required = true)
  private String mobileNumber;

  @NotBlank
  @Size(min = 5, max = 5)
  @Pattern(regexp = "^([0-9]*)$")
  @ApiModelProperty(name = "pinCode", value = "20050", required = true)
  private String pinCode;

}
