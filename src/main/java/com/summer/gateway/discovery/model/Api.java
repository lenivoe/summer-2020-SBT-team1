package com.summer.gateway.discovery.model;

import java.util.Objects;

public class Api {

    private final String path;

    public Api(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "Api{" +
                "path='" + path + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Api api = (Api) o;
        return Objects.equals(path, api.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
