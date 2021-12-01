package org.pyruz.api.shop.model.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductNewRequest {
    @NotNull
    @ApiModelProperty(name = "categoryId", value = "5", required = true)
    private Integer categoryId;

    @NotNull
    @ApiModelProperty(name = "price", value = "105", required = true)
    private Float price;

    @NotBlank
    @ApiModelProperty(name = "title", value = "Product title...", required = true)
    private String title;
}
