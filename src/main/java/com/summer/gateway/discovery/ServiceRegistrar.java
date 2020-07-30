package com.summer.gateway.discovery;

import com.summer.gateway.dao.repositories.RemoteServicesRepositoryImpl;
import com.summer.gateway.discovery.model.Api;
import com.summer.gateway.discovery.model.RemoteService;
import com.summer.gateway.remote.models.PublishModelRequest;
import com.summer.gateway.remote.models.PublishModelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

    //TODO("Надо внедрять из вне")
    private final int pingInterval = 10_000;

    @Autowired
    public void setServicesRepository(RemoteServicesRepositoryImpl servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    public PublishModelResponse register(PublishModelRequest request) throws URISyntaxException {
        String uuid = UUID.randomUUID().toString();
        String nameService = request.getName_service();
        List<Api> api = request.getApi().stream().map(it -> new Api(it.getPath())).collect(Collectors.toList());

        RemoteService remoteService = new RemoteService(
                uuid,
                nameService,
                request.getVersion_service(),
                makeURI(request.getAddress(), request.getPort()),
                api
        );

        servicesRepository.addService(nameService, remoteService);

        return new PublishModelResponse(uuid, pingInterval);
    }

    private URI makeURI(String address, String port) throws URISyntaxException {
        if (port == null || port.equals("")) {
            return new URI(address);
        } else {
            return new URI(address + ":" + port);
        }
    }

}
