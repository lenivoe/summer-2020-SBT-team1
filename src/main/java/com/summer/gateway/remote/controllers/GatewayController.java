package com.summer.gateway.remote.controllers;


import com.summer.gateway.discovery.ServiceReady;
import com.summer.gateway.discovery.ServiceRegistrar;
import com.summer.gateway.remote.models.PublishRequestModel;
import com.summer.gateway.remote.models.PublishResponseModel;
import com.summer.gateway.remote.validators.PublishRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("gateway")
public class GatewayController {

    private ServiceRegistrar serviceRegistrar;
    private ServiceReady serviceReady;
    private PublishRequestValidator requestValidator;

    @Autowired
    public void setServiceRegistrar(ServiceRegistrar serviceRegistrar) {
        this.serviceRegistrar = serviceRegistrar;
    }

    @Autowired
    public void setServiceReady(ServiceReady serviceReady) {
        this.serviceReady = serviceReady;
    }

    @Autowired
    public void setRequestValidator(PublishRequestValidator requestValidator) {
        this.requestValidator = requestValidator;
    }

    @PostMapping("publish")
    public @ResponseBody
    PublishResponseModel publish(@RequestBody PublishRequestModel request) {
        requestValidator.validate(request);
        try {
            return serviceRegistrar.register(request);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
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
