package com.kata.shopping.infrastructure.config;

import com.kata.shopping.infrastructure.logging.InboundHttpLoggingInterceptor;
import com.kata.shopping.infrastructure.logging.OutboundHttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final InboundHttpLoggingInterceptor inboundInterceptor;
    private final OutboundHttpLoggingInterceptor outboundInterceptor;

    public WebConfig(InboundHttpLoggingInterceptor inboundInterceptor,
                     OutboundHttpLoggingInterceptor outboundInterceptor) {
        this.inboundInterceptor = inboundInterceptor;
        this.outboundInterceptor = outboundInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(inboundInterceptor);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of(outboundInterceptor));
        return restTemplate;
    }
}
