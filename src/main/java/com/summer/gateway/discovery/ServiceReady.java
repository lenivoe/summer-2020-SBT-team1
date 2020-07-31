package com.summer.gateway.discovery;

import com.summer.gateway.dao.repositories.RemoteServiceRepository;
import com.summer.gateway.discovery.model.RemoteServiceModel;
import com.summer.gateway.discovery.model.StateService;
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
        RemoteServiceModel service = remoteServiceRepository.findInstanceById(instanceId);
        service.setState(StateService.ACTIVE);
    }
}
