package com.summer.gateway.dao.repositories;

import com.summer.gateway.discovery.model.Api;
import com.summer.gateway.discovery.model.GroupRemoteService;
import com.summer.gateway.discovery.model.RemoteService;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RemoteServicesRepositoryImpl implements RemoteServiceRepository {

    private final List<GroupRemoteService> groupRemoteServices = new LinkedList<>();

    @Override
    public void addService(String nameService, String versionService, List<Api> api, RemoteService newInstance) {
        GroupRemoteService group = findGroupByNameAndVersion(nameService, versionService);
        if (group != null) {
            group.addInstance(newInstance);
        } else {
            GroupRemoteService newGrope = new GroupRemoteService(nameService, versionService, api);
            newGrope.addInstance(newInstance);
            groupRemoteServices.add(newGrope);
        }
    }

    @Override
    public RemoteService findInstanceById(String id) {
        for (var group : groupRemoteServices) {
            RemoteService service = group.getInstanceById(id);
            if (service != null) return service;
        }
        return null;
    }

    @Override
    public GroupRemoteService findGroupByNameAndVersion(String nameService, String versionService) {
        for (var group : groupRemoteServices) {
            if (group.getNameService().equals(nameService) && group.getVersionService().equals(versionService)) {
                return group;
            }
        }
        return null;
    }

    @Override
    public List<GroupRemoteService> getActiveGroup() {
        return groupRemoteServices.stream().filter(GroupRemoteService::isActive).collect(Collectors.toList());
    }

}
