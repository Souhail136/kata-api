package com.kata.shopping.infrastructure.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Intercepteur de logging pour les appels HTTP sortants via RestTemplate.
 */
@Component
public class OutboundHttpLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(OutboundHttpLoggingInterceptor.class);

    @Override
    @NonNull
    public ClientHttpResponse intercept(@NonNull HttpRequest request,
                                        @NonNull byte[] body,
                                        @NonNull ClientHttpRequestExecution execution) throws IOException {
        log.info(">>> Outbound {} {}", request.getMethod(), request.getURI());
        long start = System.currentTimeMillis();
        ClientHttpResponse response = execution.execute(request, body);
        log.info("<<< Outbound {} {} | status={} | {}ms",
                request.getMethod(),
                request.getURI(),
                response.getStatusCode(),
                System.currentTimeMillis() - start);
        return response;
    }
}
