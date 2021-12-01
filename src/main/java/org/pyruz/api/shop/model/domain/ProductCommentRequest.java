package org.pyruz.api.shop.model.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductCommentRequest {

    @NotNull
    @ApiModelProperty(name = "productId", value = "5", required = true)
    private Integer productId;

    @NotBlank
    @ApiModelProperty(name = "comment", value = "Your comment...", required = true)
    private String comment;
}
