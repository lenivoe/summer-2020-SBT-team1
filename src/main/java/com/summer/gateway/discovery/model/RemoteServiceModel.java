package com.summer.gateway.discovery.model;

import com.summer.gateway.discovery.StateServiceHandler;

import java.net.URI;
import java.util.Objects;

/**
 * Удаленный сервис
 */
public class RemoteServiceModel {

    private final String id;
    private final String versionService;
    private final URI uri;

    private StateService state = StateService.PUBLISHED;

    private StateServiceHandler stateServiceHandler;

    public RemoteServiceModel(String id, String versionService, URI uri, StateServiceHandler stateServiceHandler) {
        this.id = id;
        this.uri = uri;
        this.versionService = versionService;
        this.stateServiceHandler = stateServiceHandler;
    }

    public StateService getState() {
        return state;
    }

    public void setState(StateService state) {
        this.state = state;
        stateServiceHandler.stateChanged(state);
    }

    public URI getUri() {
        return uri;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteServiceModel service = (RemoteServiceModel) o;
        return Objects.equals(id, service.id) &&
                Objects.equals(versionService, service.versionService) &&
                Objects.equals(uri, service.uri) &&
                state == service.state &&
                Objects.equals(stateServiceHandler, service.stateServiceHandler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, versionService, uri, state, stateServiceHandler);
    }
}
