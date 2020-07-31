package com.summer.gateway.discovery.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GroupRemoteServiceModel {

    private final String nameService;
    private final List<ApiModel> api;

    private boolean isGateway = false;

    private final List<RemoteServiceModel> instances = new LinkedList<>();

    public GroupRemoteServiceModel(String nameService, List<ApiModel> api) {
        this.nameService = nameService;
        this.api = api;
    }

    /**
     * Если хотя бы один экземпляр сервиса готов принимать запросы, считает группу активной и публикуем их API
     */
    public boolean isActive() {
        for (var instance : instances) {
            if (instance.getState().equals(StateService.ACTIVE)) return true;
        }
        return false;
    }

    public void addInstance(RemoteServiceModel instance) {
        this.instances.add(instance);
    }

    public boolean isGateway() {
        return isGateway;
    }

    public void setGateway(boolean gateway) {
        isGateway = gateway;
    }

    public boolean hasApi(String path) {
        for (var api : this.api) {
            if (api.comparePath(path)) return true;
        }
        return false;
    }

    public List<ApiModel> getApi() {
        return api;
    }

    public List<RemoteServiceModel> getInstances() {
        return instances;
    }

    public RemoteServiceModel getInstanceById(String id) {
        for (var instance : instances) {
            if (instance.getId().equals(id)) return instance;
        }
        return null;
    }

    public List<RemoteServiceModel> getInstancesByState(StateService stateService) {
        return instances.stream().filter(m -> m.getState().equals(stateService)).collect(Collectors.toList());
    }

    public String getNameService() {
        return nameService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupRemoteServiceModel that = (GroupRemoteServiceModel) o;
        return isGateway == that.isGateway &&
                Objects.equals(nameService, that.nameService) &&
                Objects.equals(api, that.api) &&
                Objects.equals(instances, that.instances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameService, api, isGateway, instances);
    }
}
