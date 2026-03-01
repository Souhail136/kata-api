package com.kata.shopping.infrastructure.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * Intercepteur de logging pour les requêtes HTTP entrantes.
 * Injecte un identifiant de corrélation dans le MDC pour traçabilité bout-en-bout.
 */
@Component
public class InboundHttpLoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(InboundHttpLoggingInterceptor.class);
    static final String CORRELATION_ID_KEY = "correlationId";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        String correlationId = UUID.randomUUID().toString();
        MDC.put(CORRELATION_ID_KEY, correlationId);
        response.setHeader("X-Correlation-Id", correlationId);

        log.info(">>> {} {} | from={}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        log.info("<<< {} {} | status={}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus());
        MDC.clear();
    }
}
