package com.summer.gateway.discovery;

import com.summer.gateway.remote.exceptions.InstanceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class ServicePing {

    private final Map<String, PingInstance> pingInstances = new HashMap<>();

    private final ServiceDelete serviceDelete;

    @Value("${ping.interval}")
    private int pingInterval;

    @Autowired
    ServicePing(@NonNull final ServiceDelete serviceDelete) {
        this.serviceDelete = serviceDelete;
    }

    public void ping(String uuid) {
        PingInstance pingInstance = pingInstances.get(uuid);
        if (pingInstance == null) throw new InstanceNotFound(uuid);
        pingInstance.setPing(true);
    }

    public void addInstance(String uuid) {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                PingInstance pingInstance = pingInstances.get(uuid);
                if (pingInstance.isPing()) {
                    pingInstance.setPing(false);
                } else {
                    serviceDelete.deleteInstance(uuid);
                    timer.cancel();
                    pingInstances.remove(uuid);
                }
            }
        }, pingInterval, pingInterval);

        pingInstances.put(uuid, new PingInstance(false, timer));
    }
}
