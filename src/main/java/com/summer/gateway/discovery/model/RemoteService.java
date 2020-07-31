package com.summer.gateway.discovery.model;

import com.summer.gateway.discovery.StateServiceHandler;

import java.net.URI;

/**
 * Удаленный сервис
 */
public class RemoteService {

    private final String id;
    private final URI uri;

    private StateService state = StateService.PUBLISHED;

    private StateServiceHandler stateServiceHandler;

    public RemoteService(String id, URI uri) {
        this.id = id;
        this.uri = uri;
    }

    public void setStateServiceHandler(StateServiceHandler stateServiceHandler) {
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

}
