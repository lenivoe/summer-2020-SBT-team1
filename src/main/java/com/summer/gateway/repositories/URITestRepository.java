package com.summer.gateway.repositories;

import org.springframework.stereotype.Repository;

import java.net.URI;

@Repository
public class URITestRepository {
    private URI uri;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
