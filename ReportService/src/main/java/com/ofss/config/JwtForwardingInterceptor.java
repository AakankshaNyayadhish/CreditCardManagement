package com.ofss.config;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtForwardingInterceptor
        implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution)
            throws IOException {

        RequestAttributes requestAttributes =
                RequestContextHolder.getRequestAttributes();

        boolean hasAttributes = false;
        if (requestAttributes != null) {
            if (requestAttributes instanceof ServletRequestAttributes) {
                hasAttributes = true;
            }
        }

        if (hasAttributes) {

            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) requestAttributes;

            HttpServletRequest currentRequest =
                    attributes.getRequest();

            String token =
                    currentRequest.getHeader("Authorization");

            boolean hasToken = false;
            if (token != null) {
                hasToken = true;
            }

            if (hasToken) {
                request.getHeaders().set(
                        "Authorization",
                        token
                );
            }
        }

        return execution.execute(request, body);
    }
}