package org.pyruz.api.shop.model.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;

@Data
public class ProductRateRequest {

    @NotNull
    @ApiModelProperty(name = "productId", value = "12", required = true)
    private Integer productId;

    @NotNull
    @ApiModelProperty(name = "rate", value = "1-5", required = true)
    @Range(min=1, max=5)
    private Float rate;
}
