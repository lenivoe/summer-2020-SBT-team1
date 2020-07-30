package com.summer.gateway.proxy;

import com.summer.gateway.repositories.URITestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

@Component
public class MyGatewayFilter implements GatewayFilter, Ordered {

    @Override
    public int getOrder() {
        return 10001;
    }

    private URITestRepository repository;

    @Autowired
    public void setRepository(URITestRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        URI uri = (URI) exchange.getAttributes().get(GATEWAY_REQUEST_URL_ATTR);

        // Здесь мы получаем путь запроса
        String path = uri.getPath();
        // Здесь мы получаем параметры запроса, которые в адресе после ?
        String query = uri.getQuery();

        System.out.println(path);
        System.out.println(query);

        // Здесь мы должны как-то основание path узнать сервис на который переадресуем запрос
        URI forwardUri = repository.getUri();

        System.out.println(query);

        // Формируем итоговый запрос
        URI result = null;
        try {
            if (query == null) {
                result = new URI(forwardUri + path);
            } else {
                result = new URI(forwardUri + path + "?" + query);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        // Здесь мы подставляем итовый запрос
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, result);

        return chain.filter(exchange);
    }
}