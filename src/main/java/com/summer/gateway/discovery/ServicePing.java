package com.summer.gateway.discovery;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class ServicePing {
    public ArrayList<String> arrayOfInstanses = new ArrayList<>() ;

    public void pingMain(Integer indexOfInstanceId) {


    }

    public boolean checkPing() {

    }

    public void addInstance(String instanceId) {
                arrayOfInstanses.add(instanceId);
            }

            public boolean timerPing(Integer indexOfInstanceId) {
                final Timer time = new Timer();
                int index = indexOfInstanceId;
                time.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        while (true) {
                            if (checkPing() == true) {
                                break;
                            }
                        }
                    }
        }, 0, 10000);
        index++;
        pingMain(index);
        return true;
    }
}
