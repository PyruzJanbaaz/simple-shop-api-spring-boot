package org.pyruz.api.shop.model.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CategoryUpdateRequest {

    @NotNull
    @ApiModelProperty(name = "id", value = "20", required = true)
    private Integer id;

    @NotBlank
    @ApiModelProperty(name = "title", value = "Category title...", required = true)
    private String title;
}
