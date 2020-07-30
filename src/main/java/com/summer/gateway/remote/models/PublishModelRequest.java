package com.summer.gateway.remote.models;


import java.util.List;
import java.util.Objects;

public class PublishModelRequest {

    private final String address;
    private final String port;
    private final String name_service;
    private final String version_service;
    private final List<ApiModelRequest> api;

    public PublishModelRequest(String address, String port, String name_service, String version_service, List<ApiModelRequest> api) {
        this.address = address;
        this.port = port;
        this.name_service = name_service;
        this.version_service = version_service;
        this.api = api;
    }

    public String getAddress() {
        return address;
    }

    public String getPort() {
        return port;
    }

    public String getName_service() {
        return name_service;
    }

    public String getVersion_service() {
        return version_service;
    }

    public List<ApiModelRequest> getApi() {
        return api;
    }

    @Override
    public String toString() {
        return "PublishModelRequest{" +
                "address='" + address + '\'' +
                ", port='" + port + '\'' +
                ", name_service='" + name_service + '\'' +
                ", version_service='" + version_service + '\'' +
                ", api=" + api +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublishModelRequest that = (PublishModelRequest) o;
        return Objects.equals(address, that.address) &&
                Objects.equals(port, that.port) &&
                Objects.equals(name_service, that.name_service) &&
                Objects.equals(version_service, that.version_service) &&
                Objects.equals(api, that.api);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, port, name_service, version_service, api);
    }
}
