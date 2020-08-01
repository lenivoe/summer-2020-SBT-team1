package com.summer.gateway.dao.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class RemoteService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nameService;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Api> api = new HashSet<>();
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<InstanceService> instances = new HashSet<>();

    private boolean isGateway = false;

    public RemoteService() {

    }

    public RemoteService(String nameService, Set<Api> api) {
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

    /**
     * Проверить пренадлежить ли API этому сервису
     */
    public boolean hasApi(String path) {
        for (var api : this.api) {
            if (api.comparePath(path)) return true;
        }
        return false;
    }

    /**
     * Добавить экземпляр сервиса
     */
    public void addInstance(InstanceService instance) {
        this.instances.add(instance);
    }

    /**
     * Получить список экземпляров с заданным статусом
     */
    public List<InstanceService> getInstancesByState(StateService stateService) {
        return instances.stream().filter(f -> f.getState().equals(stateService)).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameService() {
        return nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }

    public Set<Api> getApi() {
        return api;
    }

    public void setApi(Set<Api> api) {
        this.api = api;
    }

    public Set<InstanceService> getInstances() {
        return instances;
    }

    public void setInstances(Set<InstanceService> instances) {
        this.instances = instances;
    }

    public boolean isGateway() {
        return isGateway;
    }

    public void setGateway(boolean gateway) {
        isGateway = gateway;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteService that = (RemoteService) o;
        return isGateway == that.isGateway &&
                Objects.equals(nameService, that.nameService) &&
                Objects.equals(api, that.api) &&
                Objects.equals(instances, that.instances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameService, api, instances, isGateway);
    }

    @Override
    public String toString() {
        return "RemoteService{" +
                "id=" + id +
                ", nameService='" + nameService + '\'' +
                ", api=" + api +
                ", instances=" + instances +
                ", isGateway=" + isGateway +
                '}';
    }
}
