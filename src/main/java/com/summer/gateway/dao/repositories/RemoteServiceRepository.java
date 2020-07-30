package com.summer.gateway.dao.repositories;

import com.summer.gateway.discovery.model.RemoteService;

import java.util.List;
import java.util.Set;

public interface RemoteServiceRepository {
    void addService(String nameService, RemoteService newService);
    RemoteService findInstanceById(String id);
    Set<String> getServiceName();
    List<RemoteService> findInstancesByPath(String path);
    List<RemoteService> getInstanceIsReady();
    List<RemoteService> getInstanceIsReadyByPath();
}
