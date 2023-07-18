package com.drawhale.apigatewayservice.filter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * CustomFilter Example
 * @Component 주석을 풀고 각 route 설정에서 아래 내용 추가
 * ```
 * filters:
 *  - CustomFilter
 * ```
 * Global Filter 적용시 spring.cloud.gateway.default-filters 에 추가될 수 있다.
 * ```
 * spring:
 *  cloud:
 *    gateway:
 *      default-filters:
 *        - name: [Filter Name]
 *          args:
 *            [Filter Config field Name]: [Filter Config field Value]
 *            ...
 * ```
 * 이러한 Configuration 정보는 Config Server 에서 받아와서 사용할 수 있다. 즉, 외부 데이터를 바로 가져와서 사용할 수 있으므로 마이크로 서비스 변경이 필요 없을 수도 있다.
 */
//@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        // Custom Pre Filter
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (config.isPreLogger())
                log.info("Custom PRE filter request id -> {} : {}", request.getId(), config.getBaseMessage());
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                        if (config.isPostLogger())
                            log.info("Custom POST filter response code -> {}: {}", response.getStatusCode(), config.getBaseMessage());
                    })
            );
        });
    }

    @Getter
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
    public CustomFilter() {
        super(Config.class);
    }
}
