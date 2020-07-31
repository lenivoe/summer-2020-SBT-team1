package com.summer.gateway.discovery;

import com.summer.gateway.dao.repositories.RemoteServicesRepositoryImpl;
import com.summer.gateway.discovery.model.Api;
import com.summer.gateway.discovery.model.RemoteService;
import com.summer.gateway.remote.models.PublishModelRequest;
import com.summer.gateway.remote.models.PublishModelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Класс который регистрирует сервисы в системе
 */
@Service
public class ServiceRegistrar {

    private RemoteServicesRepositoryImpl servicesRepository;
    private ApplicationContext applicationContext;

    //TODO("Надо внедрять из вне")
    private final int pingInterval = 10_000;

    @Autowired
    public void setServicesRepository(RemoteServicesRepositoryImpl servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public PublishModelResponse register(PublishModelRequest request) throws URISyntaxException {
        String id = UUID.randomUUID().toString();
        String nameService = request.getName_service();
        String versionService = request.getVersion_service();
        List<Api> api = request.getApi().stream().map(it -> new Api(it.getPath())).collect(Collectors.toList());

        RemoteService remoteService = new RemoteService(
                id,
                makeURI(request.getAddress(), request.getPort())
        );

        remoteService.setStateServiceHandler(applicationContext.getBean(StateServiceHandler.class));

        servicesRepository.addService(nameService, versionService, api, remoteService);

        return new PublishModelResponse(id, pingInterval);
    }

    private URI makeURI(String address, String port) throws URISyntaxException {
        if (port == null || port.equals("")) {
            return new URI(address);
        } else {
            return new URI(address + ":" + port);
        }
    }

}
