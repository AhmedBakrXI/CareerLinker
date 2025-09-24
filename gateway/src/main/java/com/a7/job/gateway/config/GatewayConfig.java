package com.a7.job.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/v1/auth/**")
//                        .filters(f -> f.filter(null))
                        .uri("lb://auth"))
                .route("notification-service",
                        r -> r.path("/api/v1/notifications/**")
//                                .filters(f -> f.filter(null))
                        .uri("lb://notification"))
                .build();
    }
}
