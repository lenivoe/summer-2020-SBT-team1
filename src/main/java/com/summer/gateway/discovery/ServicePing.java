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
    private final Map<String, Boolean> pingInstances = new HashMap<>();

    @Value("${ping.interval}")
    private int pingInterval;
    private final ServiceDelete serviceDelete;

    @Autowired
    ServicePing(@NonNull final ServiceDelete serviceDelete) {
        this.serviceDelete = serviceDelete;
    }

    public void ping(String uuid) {
        Boolean ping = pingInstances.get(uuid);
        if (ping == null) throw new InstanceNotFound(uuid);
        pingInstances.put(uuid, true);
    }

    public void addInstance(String uuid) {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                boolean ping = pingInstances.get(uuid);
                if (ping) {
                    pingInstances.put(uuid, false);
                } else {
                    serviceDelete.deleteInstance(uuid);
                    timer.cancel();
                    pingInstances.remove(uuid);
                }
            }
        }, pingInterval, pingInterval);

        pingInstances.put(uuid, false);
    }
}
