package com.summer.gateway.dao.repositories;

import com.summer.gateway.discovery.model.Api;
import com.summer.gateway.discovery.model.GroupRemoteService;
import com.summer.gateway.discovery.model.RemoteService;

import java.util.List;


public interface RemoteServiceRepository {
    void addService(String nameService, String versionService, List<Api> api, RemoteService newInstance);

    RemoteService findInstanceById(String id);

    GroupRemoteService findGroupByNameAndVersion(String nameService, String versionService);

    List<GroupRemoteService> getActiveGroup();
}
