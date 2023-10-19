package com.pragma.powerup.infrastructure.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeingClientInterceptorImp implements RequestInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public static String getBearerTokenHeader() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header(AUTHORIZATION_HEADER, getBearerTokenHeader());
    }
}
