package com.summer.gateway.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;

class PingInstance {
    private boolean isPing;
    private Timer timer;

    public PingInstance(boolean isPing, Timer timer) {
        this.isPing = isPing;
        this.timer = timer;
    }

    public boolean isPing() {
        return isPing;
    }

    public void setPing(boolean ping) {
        isPing = ping;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PingInstance that = (PingInstance) o;
        return isPing == that.isPing &&
                Objects.equals(timer, that.timer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPing, timer);
    }
}

@Service
public class ServicePing2 {

    private final Map<String, PingInstance> pingInstances = new HashMap<>();

    private final ServiceDelete serviceDelete;

    @Value("${ping.interval}")
    private int pingInterval;

    @Autowired
    ServicePing2(@NonNull final ServiceDelete serviceDelete) {
        this.serviceDelete = serviceDelete;
    }

    public void ping(String uuid) {
        PingInstance pingInstance = pingInstances.get(uuid);
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
                    serviceDelete.delete(uuid);
                    timer.cancel();
                    pingInstances.remove(uuid);
                }
            }
        }, pingInterval, pingInterval);

        pingInstances.put(uuid, new PingInstance(false, timer));
    }
}
