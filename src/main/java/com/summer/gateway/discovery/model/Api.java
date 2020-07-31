package com.summer.gateway.discovery.model;

import java.util.Objects;
import java.util.regex.Pattern;

public class Api {

    private final String path;
    private final Pattern pattern;

    public Api(String path) {
        this.path = path;
        this.pattern = makePatten();
    }

    public String getPath() {
        return path;
    }

    public boolean comparePath(String path) {
        return pattern.matcher(path).find();
    }

    private Pattern makePatten() {
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
