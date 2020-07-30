package com.summer.gateway.discovery.model;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Удаленный сервис
 */
public class RemoteService {

    private final String uuid;
    private final String nameService;
    private final String versionService;
    private final URI uri;
    private final List<Api> api;

    private boolean ready = false;

    public RemoteService(String uuid, String nameService, String versionService, URI uri, List<Api> api) {
        this.uuid = uuid;
        this.nameService = nameService;
        this.versionService = versionService;
        this.uri = uri;
        this.api = api;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public String getNameService() {
        return nameService;
    }

    public String getVersionService() {
        return versionService;
    }

    public URI getUri() {
        return uri;
    }

    public List<Api> getApi() {
        return api;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteService that = (RemoteService) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(nameService, that.nameService) &&
                Objects.equals(versionService, that.versionService) &&
                Objects.equals(uri, that.uri) &&
                Objects.equals(api, that.api);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, nameService, versionService, uri, api);
    }
}
