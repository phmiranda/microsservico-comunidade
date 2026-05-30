/*
 * Author: phmiranda
 * Project: comunidade
 * Task Number: 71
 * Description: Introdução ao Spring Boot
 * Date: 26/03/2022
 */

package br.com.phmiranda.comunidade.config.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class DocumentationConfiguration {

    @Value("${application.title:microsservico-comunidade}")
    private String applicationTitle;

    @Value("${application.version:1.0.0}")
    private String applicationVersion;

    @Bean
    public Docket comunidadeApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("br.com.phmiranda.comunidade.controller"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
            .securitySchemes(Collections.singletonList(apiKey()))
            .securityContexts(Collections.singletonList(securityContext()))
            .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title(applicationTitle)
            .description("API para gerenciamento de cursos, usuários, dúvidas e respostas da comunidade.")
            .version(applicationVersion)
            .contact(new Contact("phmiranda", "", ""))
            .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.any())
            .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[] { authorizationScope };
        return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
    }
}
