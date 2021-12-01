package org.pyruz.api.shop.model.dto;

import lombok.Data;

@Data
public class RateDTO extends BaseEntityDTO<Integer> {
    private Float rate;
}
