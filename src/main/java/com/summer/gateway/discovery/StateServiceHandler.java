package com.summer.gateway.discovery;

import com.summer.gateway.dao.repositories.RemoteServiceRepository;
import com.summer.gateway.discovery.model.GroupRemoteService;
import com.summer.gateway.discovery.model.StateService;
import com.summer.gateway.proxy.RefreshableRoutesLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateServiceHandler {

    private RefreshableRoutesLocator refreshableRoutesLocator;
    private RemoteServiceRepository serviceRepository;

    @Autowired
    public void setRefreshableRoutesLocator(RefreshableRoutesLocator refreshableRoutesLocator) {
        this.refreshableRoutesLocator = refreshableRoutesLocator;
    }

    @Autowired
    public void setServiceRepository(RemoteServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public void stateChanged(StateService stateService) {
        if (stateService.equals(StateService.ACTIVE)) {
            refreshableRoutesLocator.clearRoutes();
            List<GroupRemoteService> groupRemoteServices = serviceRepository.getActiveGroup();
            for (var group : groupRemoteServices) {
                if (group.isActive()) {
                    group.getApi().forEach(it -> refreshableRoutesLocator.addRoute(it.getPath()));
                }
                group.setGateway(true);
            }
            refreshableRoutesLocator.buildRoutes();
        }
    }
}
