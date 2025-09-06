package org.defesasoft.apigateway.config;

import org.defesasoft.apigateway.security.JwtAuthenticationFilter;
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

    private final JwtAuthenticationFilter filter;

    public RouteConfig (JwtAuthenticationFilter filter) {
        this.filter = filter;
    }


    @Bean
    public RouteLocator createRouteLocate(RouteLocatorBuilder builder){
        return builder.routes()
                .route(bankServiceId, route -> route.path(bankServicePath).filters(gtf-> gtf.filter(filter)).uri(bankServiceUrl))
                .route(accountServiceId, route -> route.path(accountServicePath).filters(gtf-> gtf.filter(filter)).uri(accountServiceUrl))
                .route(transactionServiceId, route -> route.path(transactionServicePath).filters(gtf-> gtf.filter(filter)).uri(transactionServiceUrl))
                .route("auth-service", route -> route.path("/api/auth/**").uri("http://localhost:8085"))
                .build();
    }
}
