package com.summer.gateway.remote.controllers;


import com.summer.gateway.discovery.ServiceReady;
import com.summer.gateway.discovery.ServiceRegistrar;
import com.summer.gateway.remote.models.PublishRequestModel;
import com.summer.gateway.remote.models.PublishResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("gateway")
public class GatewayController {

    private ServiceRegistrar serviceRegistrar;
    private ServiceReady serviceReady;

    @Autowired
    public void setServiceRegistrar(ServiceRegistrar serviceRegistrar) {
        this.serviceRegistrar = serviceRegistrar;
    }

    @Autowired
    public void setServiceReady(ServiceReady serviceReady) {
        this.serviceReady = serviceReady;
    }

    @PostMapping("publish")
    public @ResponseBody
    PublishResponseModel publish(@RequestBody PublishRequestModel request) throws URISyntaxException {
        return serviceRegistrar.register(request);
    }

    @GetMapping("ping/{instance_id}")
    public void ping(@PathVariable("instance_id") String instanceId) {
    }

    @GetMapping("ready/{instance_id}")
    public void ready(@PathVariable("instance_id") String instanceId) {
        serviceReady.ready(instanceId);
    }

    @GetMapping("unpublish/{instance_id}")
    public void unpublish(@PathVariable("instance_id") String instanceId) {
    }

}
