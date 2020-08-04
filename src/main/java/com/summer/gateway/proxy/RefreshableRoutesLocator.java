/*
 * Copyright (c) 2018 - 2020 by Botorabi. All rights reserved.
 * https://github.com/botorabi/HomieCenter
 *
 * License: MIT License (MIT), read the LICENSE text in
 *          main directory for more details.
 */
package com.summer.gateway.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotNull;

/**
 * A gateway route resolver which is used for dynamically refresh routes during the application runtime.
 *
 * @author boto
 * Creation Date    25th June 2018
 */
@Component
public class RefreshableRoutesLocator implements RouteLocator {

    private final RouteLocatorBuilder builder;
    private final GatewayRoutesRefresher gatewayRoutesRefresher;
    private final BalancedGatewayFilter gatewayFilter;

    private RouteLocatorBuilder.Builder routesBuilder;
    private Flux<Route> route;

    @Autowired
    public RefreshableRoutesLocator(@NonNull final RouteLocatorBuilder builder,
                                    @NonNull final GatewayRoutesRefresher gatewayRoutesRefresher,
                                    @NonNull final BalancedGatewayFilter gatewayFilter) {
        this.builder = builder;
        this.gatewayRoutesRefresher = gatewayRoutesRefresher;
        this.gatewayFilter = gatewayFilter;

        clearRoutes();

        this.route = this.routesBuilder.build().getRoutes();
    }

    /**
     * Remove all routes.
     */
    public void clearRoutes() {
        routesBuilder = builder.routes();
    }

    /**
     * Add a new route. After adding all routes call 'buildRoutes'.
     */
    @NotNull
    public RefreshableRoutesLocator addRoute(@NotNull final String path) {
        routesBuilder.route(fn -> fn
                .path(path + "/**")
                .filters(f -> f.filter(gatewayFilter))
                .uri("lb://application")
        );
        return this;
    }

    /**
     * Call this method in order to publish all routes defined by 'addRoute' calls.
     */
    public void buildRoutes() {
        this.route = routesBuilder.build().getRoutes();
        gatewayRoutesRefresher.refreshRoutes();
    }

    @Override
    public Flux<Route> getRoutes() {
        return this.route;
    }
}
