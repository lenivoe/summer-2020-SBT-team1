package com.summer.gateway.dao.entity;

import org.hibernate.annotations.CollectionType;

import javax.persistence.*;
import java.net.URI;
import java.util.Objects;

@Entity
public class InstanceService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uid;
    private String versionService;
    private URI uri;

    @Enumerated(EnumType.STRING)
    private StateService state = StateService.PUBLISHED;

    public InstanceService() {
    }

    public InstanceService(String uid, String versionService, URI uri) {
        this.uid = uid;
        this.versionService = versionService;
        this.uri = uri;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVersionService() {
        return versionService;
    }

    public void setVersionService(String versionService) {
        this.versionService = versionService;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public StateService getState() {
        return state;
    }

    public void setState(StateService state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstanceService that = (InstanceService) o;
        return Objects.equals(uid, that.uid) &&
                Objects.equals(versionService, that.versionService) &&
                Objects.equals(uri, that.uri) &&
                state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, versionService, uri, state);
    }

    @Override
    public String toString() {
        return "InstantService{" +
                "uid='" + uid + '\'' +
                ", versionService='" + versionService + '\'' +
                ", uri=" + uri +
                ", state=" + state +
                '}';
    }
}
