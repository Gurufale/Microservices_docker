package com.gateway.gatewayserver.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(1)
public class TraceFilter implements GlobalFilter {

    public static final Logger logger = LoggerFactory.getLogger(TraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        if(isCorrelationIdPresent(headers)) {
            logger.debug("Bank correlation id present in tracing filter : {} ." ,
                    filterUtility.getCorrelationId(headers));
        } else{
            String correlationId =  generateCorrelationId();
            filterUtility.setCorrelationId(exchange,correlationId);
            logger.debug("Bank correlation id generated in tracing filter : {}",correlationId);
        }
        return chain.filter(exchange);
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }

    private boolean isCorrelationIdPresent(HttpHeaders headers) {
        if(filterUtility.getCorrelationId(headers)!=null)
            return true;
        return false;
    }
}
