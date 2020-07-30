package com.summer.gateway.discovery;

import com.summer.gateway.dao.repositories.RemoteServiceRepository;
import com.summer.gateway.discovery.model.RemoteService;
import com.summer.gateway.proxy.RefreshableRoutesLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Класс который публикует API внешнего сервиса
 */
@Service
public class ServiceReady {

    private RefreshableRoutesLocator refreshableRoutesLocator;
    private RemoteServiceRepository remoteServiceRepository;

    @Autowired
    public void setRefreshableRoutesLocator(RefreshableRoutesLocator refreshableRoutesLocator) {
        this.refreshableRoutesLocator = refreshableRoutesLocator;
    }

    @Autowired
    public void setRemoteServiceRepository(RemoteServiceRepository remoteServiceRepository) {
        this.remoteServiceRepository = remoteServiceRepository;
    }

    public void ready(String instanceId) {
        RemoteService service = remoteServiceRepository.findInstanceById(instanceId);
        service.setReady(true);
        refreshableRoutesLocator.clearRoutes();
        service.getApi().forEach(it -> refreshableRoutesLocator.addRoute(it.getPath()));
        refreshableRoutesLocator.buildRoutes();
    }
}
