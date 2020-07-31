package com.summer.gateway.remote.models;


import java.util.Objects;

public class ApiRequestModel {

    private final String method_type;
    private final String path;
    private final String api_version;

    public ApiRequestModel(String method_type, String path, String api_version) {
        this.method_type = method_type;
        this.path = path;
        this.api_version = api_version;
    }

    public String getMethod_type() {
        return method_type;
    }

    public String getPath() {
        return path;
    }

    public String getApi_version() {
        return api_version;
    }

    @Override
    public String toString() {
        return "Api{" +
                "method_type='" + method_type + '\'' +
                ", path='" + path + '\'' +
                ", api_version='" + api_version + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiRequestModel apiRequest = (ApiRequestModel) o;
        return Objects.equals(method_type, apiRequest.method_type) &&
                Objects.equals(path, apiRequest.path) &&
                Objects.equals(api_version, apiRequest.api_version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method_type, path, api_version);
    }
}
