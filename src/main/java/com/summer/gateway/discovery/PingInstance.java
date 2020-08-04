package com.summer.gateway.discovery;

import java.util.Objects;
import java.util.Timer;

public class PingInstance {
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