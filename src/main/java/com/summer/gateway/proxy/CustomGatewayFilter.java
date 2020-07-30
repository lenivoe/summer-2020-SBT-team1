package com.summer.gateway.proxy;

import com.summer.gateway.dao.repositories.RemoteServiceRepository;
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
public class CustomGatewayFilter implements GatewayFilter, Ordered {

    @Override
    public int getOrder() {
        return 10001;
    }

    private RemoteServiceRepository remoteServiceRepository;

    @Autowired
    public void setRemoteServiceRepository(RemoteServiceRepository remoteServiceRepository) {
        this.remoteServiceRepository = remoteServiceRepository;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        URI uri = (URI) exchange.getAttributes().get(GATEWAY_REQUEST_URL_ATTR);

        // Здесь мы получаем путь запроса
        String path = uri.getPath();
        // Здесь мы получаем параметры запроса, которые в адресе после ?
        String query = uri.getQuery();

        System.out.println("Path: " + path);
        System.out.println("Query: " + query);

        // Здесь мы должны как-то на основание path должны узнать сервис на который переадресуем запрос
        String forwardUri = remoteServiceRepository.findInstancesByPath(path).get(0).getUri().toString();

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

        System.out.println(result);

        // Здесь мы подставляем итовый запрос
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, result);

        return chain.filter(exchange);
    }
}