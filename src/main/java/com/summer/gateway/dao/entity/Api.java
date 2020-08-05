package com.summer.gateway.dao.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Api {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String version;
    private String path;
    private boolean isActive = false;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Instance> instances = new HashSet<>();

    public void addInstance(Instance instance) {
        instances.add(instance);
    }

    public Api() {
    }

    public Api(String path, String version) {
        this.path = path;
        this.version = version;
    }

    public boolean equalsPath(String path) {
        List<String> words = new LinkedList<>(Arrays.asList(path.split("/")));
        words.remove(0);
        List<String> thisWords = new LinkedList<>(Arrays.asList(this.path.split("/")));
        thisWords.remove(0);

        thisWords = thisWords.stream().map(m -> {
            if (m.startsWith("{") && m.endsWith("}")) return "{}";
            else return m;
        }).collect(Collectors.toList());

        if (words.size() != thisWords.size()) return false;
        else {
            for (int i = 0; i < words.size(); i++) {
                if (!equalsWord(thisWords.get(i), words.get(i))) return false;
            }
        }
        return true;
    }

    public boolean equalsWord(String w1, String w2) {
        if (w1.equals("{}"))
            return true;
        else {
            return w1.equals(w2);
        }
    }

    public List<Instance> getInstancesByState(StateService state) {
        return this.instances.stream().filter(f -> f.getState().equals(state)).collect(Collectors.toList());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<Instance> getInstances() {
        return instances;
    }

    public void setInstances(Set<Instance> instances) {
        this.instances = instances;
    }

    @Override
    public String toString() {
        return "API{" +
                "id=" + id +
                ", version='" + version + '\'' +
                ", isActive=" + isActive +
                ", instances=" + instances +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Api api = (Api) o;
        return isActive == api.isActive &&
                Objects.equals(id, api.id) &&
                Objects.equals(version, api.version) &&
                Objects.equals(instances, api.instances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, isActive, instances);
    }
}
