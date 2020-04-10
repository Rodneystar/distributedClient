package com.example.jdog.distributedClient;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageProducer implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        startProducing();
    }

    ScheduledExecutorService scheduler;
    String instanceId;
    RestTemplate restTemplate;

    public MessageProducer(String instanceId, ScheduledExecutorService scheduler, RestTemplate restTemplate) {
        this.scheduler = scheduler;
        this.instanceId = instanceId;
        this.restTemplate = restTemplate;
    }

    public void startProducing() {
        DistMessage message = new DistMessage( instanceId, "hellow there");
        scheduler.scheduleWithFixedDelay( sendMessage(message), 5, 5, TimeUnit.SECONDS );
    }

    private Runnable sendMessage(DistMessage message) {
        return () -> {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity("/message", message, String.class);
            } catch (RestClientException e) {
                System.out.printf("post failed: %s\n", e.getMessage());
            }
        };
    }
}
