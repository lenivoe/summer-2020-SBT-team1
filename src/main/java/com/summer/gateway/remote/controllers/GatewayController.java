package com.summer.gateway.remote.controllers;


import com.summer.gateway.discovery.ServiceDelete;
import com.summer.gateway.discovery.ServicePing;
import com.summer.gateway.discovery.ServiceReady;
import com.summer.gateway.discovery.ServiceRegistrar;
import com.summer.gateway.remote.model.PublishRequestModel;
import com.summer.gateway.remote.model.PublishResponseModel;
import com.summer.gateway.remote.validators.PublishRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("gateway")
public class GatewayController {
    private final ServiceRegistrar serviceRegistrar;
    private final ServiceReady serviceReady;
    private final ServiceDelete serviceDelete;
    private final PublishRequestValidator requestValidator;
    private final ServicePing servicePing;

    @Autowired
    public GatewayController(@NonNull final ServiceRegistrar serviceRegistrar,
                             @NonNull final ServiceReady serviceReady,
                             @NonNull final ServiceDelete serviceDelete,
                             @NonNull final PublishRequestValidator requestValidator,
                             @NonNull final ServicePing servicePing) {
        this.serviceRegistrar = serviceRegistrar;
        this.serviceReady = serviceReady;
        this.serviceDelete = serviceDelete;
        this.requestValidator = requestValidator;
        this.servicePing = servicePing;
    }

    @PostMapping("publish")
    public @ResponseBody
    PublishResponseModel publish(@RequestBody PublishRequestModel request) {
        requestValidator.validate(request);
        return serviceRegistrar.register(request);
    }

    @GetMapping("ping/{instance_id}")
    public void ping(@PathVariable("instance_id") String instanceId) {
        servicePing.ping(instanceId);
    }

    @GetMapping("ready/{instance_id}")
    public void ready(@PathVariable("instance_id") String instanceId) {
        serviceReady.ready(instanceId);
    }

    @GetMapping("unpublish/{instance_id}")
    public void unpublish(@PathVariable("instance_id") String instanceId) {
        serviceDelete.unpublish(instanceId);
    }

}
