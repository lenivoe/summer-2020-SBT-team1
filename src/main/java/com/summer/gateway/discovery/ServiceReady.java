package com.summer.gateway.discovery;

import com.summer.gateway.dao.repositories.RemoteServiceRepository;
import com.summer.gateway.discovery.model.RemoteService;
import com.summer.gateway.discovery.model.StateService;
import com.summer.gateway.proxy.RefreshableRoutesLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Класс который публикует API внешнего сервиса
 */
@Service
public class ServiceReady {

    private RemoteServiceRepository remoteServiceRepository;

    @Autowired
    public void setRemoteServiceRepository(RemoteServiceRepository remoteServiceRepository) {
        this.remoteServiceRepository = remoteServiceRepository;
    }

    public void ready(String instanceId) {
        RemoteService service = remoteServiceRepository.findInstanceById(instanceId);
        if (service == null) return;
        service.setState(StateService.ACTIVE);
    }
}
