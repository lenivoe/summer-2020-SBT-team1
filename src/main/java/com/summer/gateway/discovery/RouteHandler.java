package com.summer.gateway.discovery;

import com.summer.gateway.dao.entity.StateService;
import com.summer.gateway.dao.repositories.RemoteServiceRepository;
import com.summer.gateway.proxy.RefreshableRoutesLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class RouteHandler {
    private final RefreshableRoutesLocator refreshableRoutesLocator;
    private final RemoteServiceRepository serviceRepository;

    @Autowired
    public RouteHandler(@NonNull final RefreshableRoutesLocator refreshableRoutesLocator,
                        @NonNull final RemoteServiceRepository serviceRepository) {
        this.refreshableRoutesLocator = refreshableRoutesLocator;
        this.serviceRepository = serviceRepository;
    }

    public void stateInstanceChanged(StateService stateService) {
        if (stateService.equals(StateService.ACTIVE)) {
            refreshableRoutesLocator.clearRoutes();
            for (var service : this.serviceRepository.findAll()) {
                if (service.isActive() && !service.isGateway()) {
                    service.getApi().forEach(it -> refreshableRoutesLocator.addRoute(it.getPath()));
                }
                service.setGateway(true);
            }
            refreshableRoutesLocator.buildRoutes();
        }
    }
}
