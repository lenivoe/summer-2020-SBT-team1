package com.summer.gateway.controllers;


import com.summer.gateway.models.PublishModelRequest;
import com.summer.gateway.models.PublishModelResponse;
import com.summer.gateway.proxy.RefreshableRoutesLocator;
import com.summer.gateway.repositories.URITestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("gateway")
public class GatewayController {

    private RefreshableRoutesLocator refreshableRoutesLocator;
    private URITestRepository repository;

    @Autowired
    public void setRepository(URITestRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setRefreshableRoutesLocator(RefreshableRoutesLocator refreshableRoutesLocator) {
        this.refreshableRoutesLocator = refreshableRoutesLocator;
    }

    @GetMapping("clear")
    public void clear() {
        refreshableRoutesLocator.clearRoutes();
        refreshableRoutesLocator.buildRoutes();
    }

    @PostMapping("publish")
    public @ResponseBody
    PublishModelResponse publish(@RequestBody PublishModelRequest request) throws URISyntaxException {

        refreshableRoutesLocator.clearRoutes();

        URI uri;

        if (request.getPort() == null || request.getPort().equals("")) {
            uri = new URI(request.getAddress());
        } else {
            uri = new URI(request.getAddress() + ":" + request.getPort());
        }

        repository.setUri(uri);
        request.getApi().forEach(it -> refreshableRoutesLocator.addRoute(it.getPath()));

        refreshableRoutesLocator.buildRoutes();

        return new PublishModelResponse(UUID.randomUUID().toString(), 10_000);
    }

    @GetMapping("ping/{instance_id}")
    public void ping(@PathVariable("instance_id") String instanceId) {
    }

    @GetMapping("ready/{instance_id}")
    public void ready(@PathVariable("instance_id") String instanceId) {
    }

    @GetMapping("unpublish/{instance_id}")
    public void unpublish(@PathVariable("instance_id") String instanceId) {
    }

}
