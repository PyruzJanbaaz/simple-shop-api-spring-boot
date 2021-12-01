package org.pyruz.api.shop.model.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductUpdateRequest {

    @NotNull
    @ApiModelProperty(name = "id", value = "12", required = true)
    private Integer id;

    @NotNull
    @ApiModelProperty(name = "price", value = "15.50", required = true)
    private Float price;

    @NotBlank
    @ApiModelProperty(name = "title", value = "Product title...", required = true)
    private String title;
}
