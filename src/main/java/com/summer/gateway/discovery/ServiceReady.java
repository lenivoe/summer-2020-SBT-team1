package com.summer.gateway.discovery;

import com.summer.gateway.dao.entity.Api;
import com.summer.gateway.dao.entity.Instance;
import com.summer.gateway.dao.entity.StateService;
import com.summer.gateway.dao.repositories.ApiRepository;
import com.summer.gateway.dao.repositories.InstanceRepository;
import com.summer.gateway.remote.exceptions.InstanceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceReady {

    private final InstanceRepository instanceRepository;
    private final ApiRepository apiRepository;
    private final RouteHandler routeHandler;

    @Autowired
    public ServiceReady(@NonNull final InstanceRepository instanceRepository,
                        @NonNull final ApiRepository apiRepository,
                        @NonNull final RouteHandler routeHandler) {
        this.instanceRepository = instanceRepository;
        this.apiRepository = apiRepository;
        this.routeHandler = routeHandler;
    }

    public void ready(String instanceUid) {
        Instance instance = instanceRepository.findByUuid(instanceUid);
        if (instance == null) throw new InstanceNotFound(instanceUid);

        // Поставили активное состояние для инстанса
        instance.setState(StateService.ACTIVE);
        instanceRepository.save(instance);

        // Надо поставить состояние isActive для всех его API
        List<Api> api = apiRepository.findByInstancesEquals(instance);
        List<Api> inactiveApi = api.stream().filter(f -> !f.isActive()).collect(Collectors.toList());

        // Если есть не активное api, сделать его активным и пересобрать маршруты
        if (!inactiveApi.isEmpty()) {
            inactiveApi.forEach(it -> it.setActive(true));
            apiRepository.saveAll(inactiveApi);
            routeHandler.updateRoutes();
        }
    }
}
