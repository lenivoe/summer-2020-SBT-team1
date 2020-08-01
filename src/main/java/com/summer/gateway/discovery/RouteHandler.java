package com.summer.gateway.discovery;

import com.summer.gateway.dao.entity.InstanceService;
import com.summer.gateway.dao.entity.RemoteService;
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

    /**
     * Метод обрабатывает изменение состояние какого либо экземпляра
     */
    public void stateInstanceChanged(InstanceService instanceService) {
        // Если состояние экзепмляра стало ACTIVE, на проверить опубликовано ли API
        // того сервиса, которому он принадлежит, если нет, опубликовать
        if (instanceService.getState().equals(StateService.ACTIVE)) {
            RemoteService service = serviceRepository.findByInstancesEquals(instanceService);
            if (!service.isGateway()) {
                refreshableRoutesLocator.clearRoutes();
                service.getApi().forEach(it -> refreshableRoutesLocator.addRoute(it.getPath()));
                refreshableRoutesLocator.buildRoutes();
            }
            service.setGateway(true);
            serviceRepository.save(service);
        }
    }
}
