package org.pyruz.api.shop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.pyruz.api.shop.exception.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/api/v*/user", "/api/v*/user/verification",
                        "/api/v*/user/signin", "/api/v*/product/search", "/api/v*/products",
                        "/api/v*/product/findByCategoryId").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v*/category").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v*/product").permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
    }

    @Override
    public void configure(WebSecurity web) {
        // -> Allow swagger to be accessed without authentication
        web.ignoring().antMatchers("/v2/api-docs")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/configuration/**")
                .antMatchers("/webjars/**")
                .antMatchers("/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

}
