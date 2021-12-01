package org.pyruz.api.shop.exception;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.pyruz.api.shop.configuration.ApplicationContextHolder;
import org.pyruz.api.shop.model.dto.BaseDTO;
import org.pyruz.api.shop.model.dto.MetaDTO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint extends ApplicationContextHolder implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.getWriter().write(
                new Gson().toJson(new BaseDTO(new MetaDTO(
                                applicationProperties.getCode("application.message.accessDenied.code"),
                                applicationProperties.getProperty("application.message.accessDenied.text")
                        ), "")
                )
        );
    }
}
