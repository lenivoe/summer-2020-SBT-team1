package com.summer.gateway.remote.models;


import java.util.Objects;

public class PublishResponseModel {

    private final String instance_id;
    private final int ping_interval;

    public PublishResponseModel(String instance_id, int ping_interval) {
        this.instance_id = instance_id;
        this.ping_interval = ping_interval;
    }

    public String getInstance_id() {
        return instance_id;
    }

    public int getPing_interval() {
        return ping_interval;
    }

    @Override
    public String toString() {
        return "PublishModelResponse{" +
                "instance_id='" + instance_id + '\'' +
                ", ping_interval=" + ping_interval +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublishResponseModel that = (PublishResponseModel) o;
        return ping_interval == that.ping_interval &&
                Objects.equals(instance_id, that.instance_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instance_id, ping_interval);
    }
}
