package com.example.jdog.distributedClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SpringBootApplication
public class DistributedClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributedClientApplication.class, args);
	}


	@Bean
	public MessageProducer messageProducer(
			@Value("${instance.id}") String instanceId,
			@Value("${distServer.host}") String distServerHost,
			@Value("${distServer.port}") int distServerPort,
			RestTemplateBuilder restTemplateBuilder
	) {
		System.out.println(instanceId);
		DefaultUriBuilderFactory defaultUri = new DefaultUriBuilderFactory("http://" + distServerHost + ":" + distServerPort);
		RestTemplate restTemplate = restTemplateBuilder.uriTemplateHandler(defaultUri).build();
		return new MessageProducer(instanceId, Executors.newSingleThreadScheduledExecutor(), restTemplate);
	}

}
