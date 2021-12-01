package org.pyruz.api.shop.configuration;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableSwagger2
@EnableTransactionManagement
@EnableJpaAuditing
public class SwaggerConfiguration {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(Predicates.not(PathSelectors.regex("/error")))
        .build()
        .apiInfo(metadata())
        .useDefaultResponseMessages(false)
        .securityContexts(Lists.newArrayList(securityContext()))
        .securitySchemes(
            new ArrayList<>(Arrays.asList(new ApiKey("JWT", "Authorization", "header"))))
        .genericModelSubstitutes(Optional.class);
  }

  private ApiInfo metadata() {
    return new ApiInfoBuilder()
        .title("Shopping REST API")
        .description("This is an implement of JAVA Application on shopping management software using Spring Boot,Spring Security.")
        .version("1.0.0")
        .license("Pyruz Janbaaz License")
        .licenseUrl("https://www.linkedin.com/in/pyruz-janbaaz-9a327226/")
        .contact(
            new Contact(
                "Pyruz Janbaaz",
                "https://www.linkedin.com/in/pyruz-janbaaz-9a327226/",
                "Pyruz.Janbaaz@gmail.com"))
        .build();
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder().securityReferences(defaultAuth()).build();
  }

  List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
  }
}
