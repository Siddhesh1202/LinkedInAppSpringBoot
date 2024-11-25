package com.siddhesh.linkedin.api_gateway.filters;

import com.siddhesh.linkedin.api_gateway.JWTService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final JWTService jwtService;

    public AuthenticationFilter(JWTService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    public GatewayFilter apply(Config config) {
        log.info("AuthenticationFilter configured");
        return ((exchange, chain) -> {
            log.info("Login request: {}", exchange.getRequest().getURI());

            final String tokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                String token = tokenHeader.split("Bearer ")[1];

                try{
                    String userId = jwtService.getUserIdFromToken(token);
                    ServerWebExchange modifiedExchange = exchange.mutate().request(r -> r.header("X-User-Id", userId)).build();

                    return chain.filter(modifiedExchange);
                }
                catch (JwtException e) {
                    log.error("JWT Exception: {}", e.getLocalizedMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                }
            }
            else{
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                log.error("Authentication Failed -> Token not Found");
            }
            return chain.filter(exchange);
        });
    }
    public static class Config{

    }
}
