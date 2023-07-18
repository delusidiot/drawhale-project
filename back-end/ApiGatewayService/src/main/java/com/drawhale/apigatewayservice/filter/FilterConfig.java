package com.drawhale.apigatewayservice.filter;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;


/**
 * Api Gateway Filter Example
 */
//@Configuration
public class FilterConfig {
//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route( r -> r.path("/uri/**")
                        .filters(f -> f
                        .addRequestHeader("request-header-name", "request-header-value")
                        .addResponseHeader("response-header-name", "response-header-value"))
                        .uri("ld://redirect uri"))
                .build();
    }
}
