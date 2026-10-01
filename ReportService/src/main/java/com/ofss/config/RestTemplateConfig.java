package com.ofss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate(
	        JwtForwardingInterceptor interceptor) {

	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getInterceptors().add(interceptor);

	    return restTemplate;
	}
}