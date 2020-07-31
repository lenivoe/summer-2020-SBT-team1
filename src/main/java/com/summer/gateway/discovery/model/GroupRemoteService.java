package com.summer.gateway.discovery.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GroupRemoteService {

    private final String nameService;
    private final String versionService;
    private final List<Api> api;

    private boolean isGateway = false;

    private final List<RemoteService> instances = new LinkedList<>();

    public GroupRemoteService(String nameService, String versionService, List<Api> api) {
        this.nameService = nameService;
        this.versionService = versionService;
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

    public void addInstance(RemoteService instance) {
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

    public List<Api> getApi() {
        return api;
    }

    public List<RemoteService> getInstances() {
        return instances;
    }

    public RemoteService getInstanceById(String id) {
        for (var instance : instances) {
            if (instance.getId().equals(id)) return instance;
        }
        return null;
    }

    public List<RemoteService> getInstancesByState(StateService stateService) {
        return instances.stream().filter(m -> m.getState().equals(stateService)).collect(Collectors.toList());
    }

    public String getNameService() {
        return nameService;
    }

    public String getVersionService() {
        return versionService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupRemoteService that = (GroupRemoteService) o;
        return Objects.equals(nameService, that.nameService) &&
                Objects.equals(versionService, that.versionService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameService, versionService);
    }
}
