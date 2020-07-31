package com.summer.gateway.discovery;

import com.summer.gateway.dao.repositories.RemoteServicesRepositoryImpl;
import com.summer.gateway.discovery.model.ApiModel;
import com.summer.gateway.discovery.model.RemoteServiceModel;
import com.summer.gateway.remote.models.PublishRequestModel;
import com.summer.gateway.remote.models.PublishResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${ping.interval}")
    private int pingInterval;

    @Autowired
    public void setServicesRepository(RemoteServicesRepositoryImpl servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public PublishResponseModel register(PublishRequestModel request) throws URISyntaxException {
        String id = UUID.randomUUID().toString();
        String nameService = request.getName_service();
        List<ApiModel> api = request.getApi().stream().map(it -> new ApiModel(it.getPath())).collect(Collectors.toList());

        RemoteServiceModel remoteService = new RemoteServiceModel(
                id,
                request.getVersion_service(),
                makeURI(request.getAddress(), request.getPort()),
                applicationContext.getBean(StateServiceHandler.class)
        );

        servicesRepository.addService(nameService, api, remoteService);

        return new PublishResponseModel(id, pingInterval);
    }

    private URI makeURI(String address, String port) throws URISyntaxException {
        if (port == null || port.equals("")) {
            return new URI(address);
        } else {
            return new URI(address + ":" + port);
        }
    }
}
