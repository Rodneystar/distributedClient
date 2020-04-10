package com.example.jdog.distributedClient;

public class DistMessage {

    public String fromId;
    public String message;

    public DistMessage(String instanceId, String message) {

        this.fromId = instanceId;
        this.message = message;
    }
}
