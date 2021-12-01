package org.pyruz.api.shop.service;

import org.pyruz.api.shop.configuration.ApplicationProperties;
import org.pyruz.api.shop.security.JwtTokenProvider;
import org.pyruz.api.shop.utility.ApplicationUtilities;
import org.springframework.stereotype.Service;

@Service
public abstract class BaseService {

    final JwtTokenProvider jwtTokenProvider;
    final ApplicationProperties applicationProperties;

    protected BaseService(JwtTokenProvider jwtTokenProvider, ApplicationProperties applicationProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.applicationProperties = applicationProperties;
    }

    protected String getUsername() {
        String token = jwtTokenProvider.resolveToken(ApplicationUtilities.getInstance(applicationProperties).getCurrentHttpRequest());
        return jwtTokenProvider.getUsername(token);
    }

}
