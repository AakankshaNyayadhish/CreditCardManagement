package com.ofss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.ofss.config.JwtForwardingInterceptor;

@SpringBootApplication
@EnableDiscoveryClient
public class CardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardServiceApplication.class, args);
		System.out.println("Card Service Started Successfully");
	}
	
	@Bean
	public RestTemplate restTemplate(
	        JwtForwardingInterceptor interceptor) {

	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getInterceptors().add(interceptor);

	    return restTemplate;
	}

}
