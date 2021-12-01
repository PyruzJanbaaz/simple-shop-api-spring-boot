package org.pyruz.api.shop.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class BaseDTO {

    private MetaDTO meta;

    private Object data;

    public BaseDTO(MetaDTO meta, Object data) {
        this.meta = meta;
        this.data = data;
    }

    public BaseDTO(MetaDTO meta) {
        this.meta = meta;
    }



}
