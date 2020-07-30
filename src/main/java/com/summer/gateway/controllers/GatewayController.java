package com.summer.gateway.controllers;


import com.summer.gateway.models.PublishModelRequest;
import com.summer.gateway.models.PublishModelResponse;
import com.summer.gateway.proxy.RefreshableRoutesLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("gateway")
public class GatewayController {

    private RefreshableRoutesLocator refreshableRoutesLocator;

    @Autowired
    public void setRefreshableRoutesLocator(RefreshableRoutesLocator refreshableRoutesLocator) {
        this.refreshableRoutesLocator = refreshableRoutesLocator;
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

        request.getApi().forEach(it -> {
            try {
                refreshableRoutesLocator.addRoute(UUID.randomUUID().toString(), it.getPath(), uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
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
