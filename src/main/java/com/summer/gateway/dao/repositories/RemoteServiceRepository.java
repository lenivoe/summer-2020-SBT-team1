package com.summer.gateway.dao.repositories;

import com.summer.gateway.discovery.model.ApiModel;
import com.summer.gateway.discovery.model.GroupRemoteServiceModel;
import com.summer.gateway.discovery.model.RemoteServiceModel;

import java.util.List;


public interface RemoteServiceRepository {
    void addService(String nameService, List<ApiModel> api, RemoteServiceModel newInstance);

    RemoteServiceModel findInstanceById(String id);

    GroupRemoteServiceModel findGroupByName(String nameService);

    List<GroupRemoteServiceModel> getActiveGroup();

    List<ApiModel> getAllApi();
}
