package org.defesasoft.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    @Value("${bank-service.url}") private String bankServiceUrl;
    @Value("${bank-service.id}") private String bankServiceId;
    @Value("${bank-service.path}") private String bankServicePath;

    @Bean
    public RouteLocator createRouteLocate(RouteLocatorBuilder builder){
        return builder.routes()
                .route(bankServiceId, route -> route.path(bankServicePath).uri(bankServiceUrl))
                .build();
    }
}
