package com.summer.gateway.dao.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
public class Api {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String path;
    @Transient
    private Pattern pattern;

    public Api() {
    }

    public Api(String path) {
        this.path = path;
    }

    /**
     * Сравнить пути
     */
    public boolean comparePath(String path) {
        if (pattern == null) this.pattern = createPatten();
        return pattern.matcher(path).find();
    }

    /**
     * Создать паттерн для сравнения
     */
    private Pattern createPatten() {
        var segments = path.split("/");
        StringBuilder regex = new StringBuilder();
        for (var s : segments) {
            if (s.endsWith("}") && s.startsWith("{")) {
                //TODO("Улучшить везде, но тут обязательно")
                regex.append(".+");
            } else {
                regex.append(s);
            }
            regex.append("/");
        }
        regex.deleteCharAt(regex.length() - 1);
        return Pattern.compile(regex.toString());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
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
