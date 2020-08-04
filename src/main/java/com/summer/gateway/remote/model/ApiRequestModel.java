package com.summer.gateway.remote.model;


import java.util.Objects;

public class ApiRequestModel {

    private final String path;
    private final String api_version;

    public ApiRequestModel(String path, String api_version) {
        this.path = path;
        this.api_version = api_version;
    }

    public String getPath() {
        return path;
    }

    public String getApi_version() {
        return api_version;
    }

    @Override
    public String toString() {
        return "ApiRequestModel{" +
                "path='" + path + '\'' +
                ", api_version='" + api_version + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiRequestModel that = (ApiRequestModel) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(api_version, that.api_version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, api_version);
    }
}
