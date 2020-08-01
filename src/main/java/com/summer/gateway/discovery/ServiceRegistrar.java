package com.summer.gateway.discovery;

import com.summer.gateway.dao.entity.Api;
import com.summer.gateway.dao.entity.InstanceService;
import com.summer.gateway.dao.entity.RemoteService;
import com.summer.gateway.dao.repositories.ApiRepository;
import com.summer.gateway.dao.repositories.InstanceServiceRepository;
import com.summer.gateway.dao.repositories.RemoteServiceRepository;
import com.summer.gateway.remote.exceptions.ApiBad;
import com.summer.gateway.remote.exceptions.URIBad;
import com.summer.gateway.remote.model.PublishRequestModel;
import com.summer.gateway.remote.model.PublishResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServiceRegistrar {

    private final InstanceServiceRepository instanceRepository;
    private final ApiRepository apiRepository;
    private final RemoteServiceRepository serviceRepository;

    @Value("${ping.interval}")
    private int pingInterval;

    @Autowired
    public ServiceRegistrar(@NonNull final InstanceServiceRepository instanceRepository,
                            @NonNull final ApiRepository apiRepository,
                            @NonNull final RemoteServiceRepository serviceRepository) {
        this.instanceRepository = instanceRepository;
        this.apiRepository = apiRepository;
        this.serviceRepository = serviceRepository;
    }

    public PublishResponseModel register(PublishRequestModel request) throws URISyntaxException {
        // URI
        URI uri = createURI(request.getAddress(), request.getPort());
        checkURI(uri);

        String nameService = request.getName_service();
        RemoteService service = serviceRepository.findByNameService(nameService);
        Set<Api> requestApi = request.getApi().stream().map(it ->
                new Api(it.getPath())).collect(Collectors.toSet());

        if (service != null) checkApiNewInstance(nameService, service, requestApi);
        checkApiNewGroup(service, requestApi);

        // Создание нового экземпляра сервиса
        String id = UUID.randomUUID().toString();
        InstanceService remoteService = new InstanceService(
                id,
                request.getVersion_service(),
                uri
        );

        // Сохранение
        save(nameService, service, requestApi, remoteService);

        return new PublishResponseModel(id, pingInterval);
    }

    private void save(String nameService, RemoteService service, Set<Api> requestApi, InstanceService remoteService) {
        instanceRepository.save(remoteService);
        for (var ra : requestApi) {
            apiRepository.save(ra);
        }

        if (service != null) {
            service.addInstance(remoteService);
        } else {
            RemoteService newService = new RemoteService(nameService, requestApi);
            newService.addInstance(remoteService);
            serviceRepository.save(newService);
        }
    }

    /**
     * Если мы создаем новую группу, надо проверить что ее API не будет конфликтовать с другими группами
     */
    private void checkApiNewGroup(RemoteService service, Set<Api> requestApi) {
        if (service == null) {
            for (var ra : requestApi) {
                apiRepository.findAll().forEach(it -> {
                    if (it.comparePath(ra.getPath()))
                        throw new ApiBad("Api of new service api: " + ra.getPath() + " ambiguous");
                });

            }
        }
    }

    /**
     * Проверка, возможно сервис с таким URI уже есть
     */
    private void checkURI(URI uri) {
        instanceRepository.findAll().forEach(it -> {
            if (it.getUri().equals(uri)) throw new URIBad("This URI: " + uri + " already exist");
        });
    }

    /**
     * Создание URI, завит от переданных параметров
     */
    private URI createURI(String address, String port) throws URISyntaxException {
        if (port == null || port.equals("")) {
            return new URI(address);
        } else {
            return new URI(address + ":" + port);
        }
    }

    /**
     * Проверка на то что API у нового экземпляра сервиса с именем nameService
     * такое же, если нет считаем что ошибка в запросе от этого instance
     */
    protected void checkApiNewInstance(String nameService, RemoteService service, Set<Api> requestApi) {
        if (requestApi.size() != service.getApi().size())
            throw new ApiBad("Api of new instance " + nameService + " is incorrect");

        Set<Api> api = service.getApi();
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
