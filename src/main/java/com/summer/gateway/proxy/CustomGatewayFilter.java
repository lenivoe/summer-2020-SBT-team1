package com.summer.gateway.proxy;

import com.summer.gateway.dao.entity.RemoteService;
import com.summer.gateway.dao.entity.StateService;
import com.summer.gateway.dao.repositories.RemoteServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

@Component
public class CustomGatewayFilter implements GatewayFilter, Ordered {

    @Override
    public int getOrder() {
        return 10001;
    }

    private final RemoteServiceRepository serviceRepository;

    @Autowired
    public CustomGatewayFilter(@NonNull final RemoteServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
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

        String forwardUri = null;

        List<RemoteService> remoteServices = new LinkedList<>();
        serviceRepository.findAll().iterator().forEachRemaining(remoteServices::add);

        remoteServices = remoteServices.stream().filter(RemoteService::isActive).collect(Collectors.toList());

        for (var instance : remoteServices) {
            if (instance.hasApi(path)) {
                forwardUri = instance.getInstancesByState(StateService.ACTIVE).get(0).getUri().toString();
            }
        }

        if (forwardUri == null) throw new RuntimeException("Плохо очень");

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