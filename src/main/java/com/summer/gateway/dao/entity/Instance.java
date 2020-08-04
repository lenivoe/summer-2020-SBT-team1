package com.summer.gateway.dao.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Instance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;
    private String address;
    private String name;
    private String version;

    @Enumerated(EnumType.STRING)
    private StateService state = StateService.INACTIVE;

    public Instance() {
    }

    public Instance(String uuid, String name, String address, String version) {
        this.uuid = uuid;
        this.address = address;
        this.name = name;
        this.version = version;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public StateService getState() {
        return state;
    }

    public void setState(StateService state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "uuid='" + uuid + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", state=" + state +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instance instance = (Instance) o;
        return Objects.equals(uuid, instance.uuid) &&
                Objects.equals(address, instance.address) &&
                Objects.equals(name, instance.name) &&
                Objects.equals(version, instance.version) &&
                state == instance.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, address, name, version, state);
    }
}
