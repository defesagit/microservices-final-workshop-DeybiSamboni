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

    @Value("${accounts-service.url}") private String accountServiceUrl;
    @Value("${accounts-service.id}") private String accountServiceId;
    @Value("${accounts-service.path}") private String accountServicePath;

    @Value("${transactions-service.url}") private String transactionServiceUrl;
    @Value("${transactions-service.id}") private String transactionServiceId;
    @Value("${transactions-service.path}") private String transactionServicePath;

    @Bean
    public RouteLocator createRouteLocate(RouteLocatorBuilder builder){
        return builder.routes()
                .route(bankServiceId, route -> route.path(bankServicePath).uri(bankServiceUrl))
                .route(accountServiceId, route -> route.path(accountServicePath).uri(accountServiceUrl))
                .route(transactionServiceId, route -> route.path(transactionServicePath).uri(transactionServiceUrl))
                .build();
    }
}
