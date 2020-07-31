package com.summer.gateway.discovery;

import com.summer.gateway.dao.repositories.RemoteServicesRepositoryImpl;
import com.summer.gateway.discovery.model.ApiModel;
import com.summer.gateway.discovery.model.GroupRemoteServiceModel;
import com.summer.gateway.discovery.model.RemoteServiceModel;
import com.summer.gateway.remote.exceptions.ApiBad;
import com.summer.gateway.remote.exceptions.URIBad;
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
        URI uri = makeURI(request.getAddress(), request.getPort());

        checkURI(uri);
        checkApiNewInstance(nameService, group, requestApi);
        checkApiNewGroup(group, requestApi);

        String id = UUID.randomUUID().toString();
        RemoteServiceModel remoteService = new RemoteServiceModel(
                id,
                request.getVersion_service(),
                uri,
                applicationContext.getBean(StateServiceHandler.class)
        );

        servicesRepository.addService(nameService, requestApi, remoteService);

        return new PublishResponseModel(id, pingInterval);
    }

    /**
     * Если мы создаем новую группу, надо проверить что ее API не будет конфликтовать с другими группами
     */
    private void checkApiNewGroup(GroupRemoteServiceModel group, List<ApiModel> requestApi) {
        if (group == null) {
            List<ApiModel> allApi = servicesRepository.getAllApi();
            for (var ra : requestApi) {
                for (var aa : allApi) {
                    if (aa.comparePath(ra.getPath()))
                        throw new ApiBad("Api of new service api: " + ra.getPath() + " ambiguous");
                }
            }
        }
    }

    /**
     * Проверка, возможно сервис с таким URI уже есть
     */
    private void checkURI(URI uri) {
        for (var instance : servicesRepository.getAllServices()) {
            if (instance.getUri().equals(uri)) throw new URIBad("This URI: " + uri + " already exist");
        }
    }

    /**
     * Проверка на то что API у нового экземпляра сервиса с именем nameService
     * такое же, если нет считаем что ошибка в запросе от этого instance
     */
    private void checkApiNewInstance(String nameService, GroupRemoteServiceModel group, List<ApiModel> requestApi) {
        if (group != null) {
            if (requestApi.size() != group.getApi().size())
                throw new ApiBad("Api of new instance " + nameService + " is incorrect");

            List<ApiModel> api = group.getApi();
            int count = 0;
            for (var ra : requestApi) {
                for (var a : api) {
                    if (ra.getPath().equals(a.getPath())) {
                        count++;
                    }
                }
            }
            if (count != requestApi.size()) {
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
