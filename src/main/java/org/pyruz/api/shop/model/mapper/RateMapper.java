package org.pyruz.api.shop.model.mapper;

import org.mapstruct.Mapper;
import org.pyruz.api.shop.model.dto.RateDTO;
import org.pyruz.api.shop.model.entity.Rate;

@Mapper
public interface RateMapper {
    RateDTO RATE_DTO(Rate rate);
}
