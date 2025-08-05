package com.smartpower;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Configuration class for Feign Client to propagate the Authorization header
 * from the incoming HTTP request to outgoing Feign requests.
 */
@Configuration
@Slf4j
public class FeignClientConfig {

    /**
     * Creates a Feign RequestInterceptor bean that intercepts outgoing Feign client requests
     * and adds the Authorization header from the current HTTP request context, if present.
     *
     * @return the configured RequestInterceptor
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

            if (requestAttributes instanceof ServletRequestAttributes) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                String authHeader = request.getHeader("Authorization");

                if (authHeader != null) {
                    log.debug("Forwarding Authorization header in Feign request.");
                    template.header("Authorization", authHeader);
                } else {
                    log.warn("Authorization header not found in the current request. Feign request will be sent without auth header.");
                }
            } else {
                log.warn("RequestAttributes are not an instance of ServletRequestAttributes. Cannot propagate Authorization header.");
            }
        };
    }
}
