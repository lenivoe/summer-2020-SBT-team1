package com.summer.gateway.discovery;

import com.summer.gateway.dao.entity.InstanceService;
import com.summer.gateway.dao.entity.StateService;
import com.summer.gateway.dao.repositories.InstanceServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ServiceReady {

    private final InstanceServiceRepository instanceRepository;
    private final RouteHandler routeHandler;

    @Autowired
    public ServiceReady(@NonNull final InstanceServiceRepository instanceRepository,
                        @NonNull final RouteHandler routeHandler) {
        this.instanceRepository = instanceRepository;
        this.routeHandler = routeHandler;
    }

    public void ready(String instanceUid) {
        InstanceService instance = instanceRepository.findByUid(instanceUid);
        instance.setState(StateService.ACTIVE);
        instanceRepository.save(instance);
        routeHandler.stateInstanceChanged(StateService.ACTIVE);
    }
}
