package com.springcloud.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class GlobalFilters implements GlobalFilter {

    private final Logger logger = LoggerFactory.getLogger(GlobalFilters.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Executing pre filter");

        // Despues de pasar el filtro, se crea un objeto reactivo(Mono) que realiza un
        // accion
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info("Executing post filter");
            exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "red").build());
            exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        }));
    }

}