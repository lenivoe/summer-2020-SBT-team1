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
    private int wordsAmount;
    private String path;
    private boolean isActive = false;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Word> words;
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Instance> instances = new HashSet<>();

    public void addInstance(Instance instance) {
        instances.add(instance);
    }

    public Api() {
    }

    public Api(List<Word> words, String path, int wordsAmount) {
        this.words = words;
        this.path = path;
        this.wordsAmount = wordsAmount;
    }

    public boolean comparePath(String path) {
        List<String> words = new LinkedList<>(Arrays.asList(path.split("/")));
        words.remove(0);

        if (words.size() != this.words.size()) return false;
        else {
            // TODO("Точно нужна сотрировка???")
            this.words.sort(Comparator.comparingInt(Word::getIndex));

            for (int i = 0; i < words.size(); i++) {
                if (!this.words.get(i).compareWord(words.get(i))) return false;
            }
        }
        return true;
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

    public int getWordsAmount() {
        return wordsAmount;
    }

    public void setWordsAmount(int wordsAmount) {
        this.wordsAmount = wordsAmount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
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
                ", wordsAmount=" + wordsAmount +
                ", isActive=" + isActive +
                ", words=" + words +
                ", instances=" + instances +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Api api = (Api) o;
        return wordsAmount == api.wordsAmount &&
                isActive == api.isActive &&
                Objects.equals(id, api.id) &&
                Objects.equals(version, api.version) &&
                Objects.equals(words, api.words) &&
                Objects.equals(instances, api.instances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, wordsAmount, isActive, words, instances);
    }
}
