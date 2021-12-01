package org.pyruz.api.shop.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pyruz.api.shop.configuration.ApplicationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaDTO {
    private Integer code;
    private String message;

    public static MetaDTO getInstance(ApplicationProperties applicationProperties) {
        return new MetaDTO(
                applicationProperties.getCode("application.message.success.code"),
                applicationProperties.getProperty("application.message.success.text")
        );
    }

}
