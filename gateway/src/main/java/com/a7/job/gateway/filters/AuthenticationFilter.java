package com.a7.job.gateway.filters;

import com.a7.job.gateway.routes.RouteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
@RefreshScope
public class AuthenticationFilter implements GatewayFilter {
    private final WebClient authClient;
    private final RouteValidator routeValidator;

    @Autowired
    public AuthenticationFilter(@LoadBalanced WebClient.Builder authClientBuilder, RouteValidator routeValidator) {
        this.routeValidator = routeValidator;
        this.authClient = authClientBuilder.baseUrl("lb://auth")
                .clientConnector(
                        new ReactorClientHttpConnector(
                                HttpClient.create().responseTimeout(Duration.ofMinutes(1))
                        )
                )
                .build();
    }

    /**
     * Process the Web request and (optionally) delegate to the next {@code WebFilter}
     * through the given {@link GatewayFilterChain}.
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!routeValidator.isSecured.test(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        String tokenHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String token = (tokenHeader != null && tokenHeader.startsWith("Bearer ")) ?
                tokenHeader.substring(7) : null;
        String email = exchange.getRequest().getHeaders().getFirst("X-User-Email");

        if (token == null || email == null) {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return authClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/auth/verify-token")
                        .queryParam("token", token)
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .flatMap(isValid -> {
                    if (Boolean.TRUE.equals(isValid)) {
                        return chain.filter(exchange);
                    } else {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }
                })
                .onErrorResume(e -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }
}
