package com.summer.gateway.discovery;

import com.summer.gateway.dao.repositories.RemoteServicesRepositoryImpl;
import com.summer.gateway.discovery.model.ApiModel;
import com.summer.gateway.discovery.model.GroupRemoteServiceModel;
import com.summer.gateway.discovery.model.RemoteServiceModel;
import com.summer.gateway.remote.exceptions.ApiBad;
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
        String nameService = request.getName_service();

        GroupRemoteServiceModel group = servicesRepository.findGroupByName(nameService);
        List<ApiModel> requestApi = request.getApi().stream().map(it -> new ApiModel(it.getPath())).collect(Collectors.toList());

        // Проверка что API у нового экземпляра сервиса с именем nameService
        // такое же, если нет считаем что ошибка в запросе от этого instance
        checkApiNewInstance(nameService, group, requestApi);

        if (group == null) {
            //TODO("Создаем новую группу, надо проверить что API будет однозначно")
        }

        String id = UUID.randomUUID().toString();
        RemoteServiceModel remoteService = new RemoteServiceModel(
                id,
                request.getVersion_service(),
                makeURI(request.getAddress(), request.getPort()),
                applicationContext.getBean(StateServiceHandler.class)
        );

        servicesRepository.addService(nameService, requestApi, remoteService);

        return new PublishResponseModel(id, pingInterval);
    }

    private void checkApiNewInstance(String nameService, GroupRemoteServiceModel group, List<ApiModel> requestApi) {
        if (group != null) {
            List<ApiModel> api = group.getApi();
            int count = 0;
            for (var ra : requestApi) {
                for (var a : api) {
                    if (ra.getPath().equals(a.getPath())) {
                        count++;
                    }
                }
            }
            if (count != requestApi.size() || count != group.getApi().size()) {
                throw new ApiBad("Api of new instance " + nameService + " is incorrect");
            }
        }
    }

    private URI makeURI(String address, String port) throws URISyntaxException {
        if (port == null || port.equals("")) {
            return new URI(address);
        } else {
            return new URI(address + ":" + port);
        }
    }
}
