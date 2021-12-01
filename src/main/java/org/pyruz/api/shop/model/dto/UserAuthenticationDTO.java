package org.pyruz.api.shop.model.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class UserAuthenticationDTO {
    private String token;
    private Set<String> roles;
}
