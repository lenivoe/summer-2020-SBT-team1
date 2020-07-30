package com.summer.gateway.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class URITestRepositoryTest {

    @Autowired
    private URITestRepository repository;

    @Test
    void contentProvide() throws URISyntaxException {
        var uri = new URI("http://google.com");
        repository.setUri(uri);
        assertEquals(uri, repository.getUri());
    }

}
