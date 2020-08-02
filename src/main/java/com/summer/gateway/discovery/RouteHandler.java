package com.summer.gateway.discovery;

import com.summer.gateway.dao.entity.Api;
import com.summer.gateway.dao.repositories.ApiRepository;
import com.summer.gateway.proxy.RefreshableRoutesLocator;
import com.summer.gateway.remote.model.ApiRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteHandler {
    private final RefreshableRoutesLocator refreshableRoutesLocator;
    private final ApiRepository apiRepository;

    @Autowired
    public RouteHandler(@NonNull final RefreshableRoutesLocator refreshableRoutesLocator,
                        @NonNull final ApiRepository apiRepository) {
        this.refreshableRoutesLocator = refreshableRoutesLocator;
        this.apiRepository = apiRepository;
    }

    public void updateRoutes() {
        this.refreshableRoutesLocator.clearRoutes();

        // TODO(Надо получать из базы только активные API, а не все)
        List<Api> api = apiRepository.findAll().stream().filter(Api::isActive).collect(Collectors.toList());

        api.forEach(it -> this.refreshableRoutesLocator.addRoute(it.getPath()));
        this.refreshableRoutesLocator.buildRoutes();
    }

}
