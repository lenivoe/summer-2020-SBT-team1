package com.summer.gateway.dao.repositories;

import com.summer.gateway.discovery.model.RemoteService;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RemoteServicesRepositoryImpl implements RemoteServiceRepository {

    private final Map<String, List<RemoteService>> services = new HashMap<>();

    public void addService(String nameService, RemoteService newService) {
        List<RemoteService> instances = services.get(nameService);
        if (instances != null) {
            //TODO(
            // Возможны ситуации:
            // 1 - Конфликт апи
            // 2 - Сервис с таким адресов уже зарегистрирован отклонить или обновить?
            // )
            instances.add(newService);
        } else {
            services.put(nameService, new LinkedList<>() {{
                add(newService);
            }});
        }
    }

    @Override
    public Set<String> getServiceName() {
        return services.keySet();
    }

    @Override
    public List<RemoteService> findInstancesByPath(String path) {
        String t1 = path.split("/")[0];
        // TODO(Надо сделать нормальный компоратор)
        for (var name : getServiceName()) {
            var instance = services.get(name).get(0);
            for (var api : instance.getApi()) {
                if (t1.equals(api.getPath().split("/")[0])) {
                    return services.get(name);
                }
            }
        }
        return null;
    }

    @Override
    public List<RemoteService> getInstanceIsReady() {
        return null;
    }

    @Override
    public List<RemoteService> getInstanceIsReadyByPath() {
        return null;
    }

    @Override
    public RemoteService findInstanceById(String uuid) {
        for (var name : getServiceName()) {
            for (var service : services.get(name)) {
                if (service.getUuid().equals(uuid)) {
                    return service;
                }
            }
        }
        return null;
    }
}
