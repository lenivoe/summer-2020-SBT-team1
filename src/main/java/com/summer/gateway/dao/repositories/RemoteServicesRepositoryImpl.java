package com.summer.gateway.dao.repositories;

import com.summer.gateway.discovery.model.ApiModel;
import com.summer.gateway.discovery.model.GroupRemoteServiceModel;
import com.summer.gateway.discovery.model.RemoteServiceModel;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RemoteServicesRepositoryImpl implements RemoteServiceRepository {

    private final List<GroupRemoteServiceModel> groupRemoteServices = new LinkedList<>();

    @Override
    public void addService(String nameService, List<ApiModel> api, RemoteServiceModel newInstance) {
        GroupRemoteServiceModel group = findGroupByName(nameService);
        if (group != null) {
            group.addInstance(newInstance);
        } else {
            GroupRemoteServiceModel newGrope = new GroupRemoteServiceModel(nameService, api);
            newGrope.addInstance(newInstance);
            groupRemoteServices.add(newGrope);
        }
    }

    @Override
    public RemoteServiceModel findInstanceById(String id) {
        for (var group : groupRemoteServices) {
            RemoteServiceModel service = group.getInstanceById(id);
            if (service != null) return service;
        }
        return null;
    }

    @Override
    public GroupRemoteServiceModel findGroupByName(String nameService) {
        for (var group : groupRemoteServices) {
            if (group.getNameService().equals(nameService)) {
                return group;
            }
        }
        return null;
    }

    @Override
    public List<GroupRemoteServiceModel> getActiveGroup() {
        return groupRemoteServices.stream().filter(GroupRemoteServiceModel::isActive).collect(Collectors.toList());
    }

    @Override
    public List<ApiModel> getAllApi() {
        return groupRemoteServices.stream().map(GroupRemoteServiceModel::getApi).flatMap(List::stream).collect(Collectors.toList());
    }

}
