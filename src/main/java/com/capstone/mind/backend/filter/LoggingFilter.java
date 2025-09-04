package com.capstone.mind.backend.filter;

import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;


@Component
public class LoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String userAgentHeader = httpRequest.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentHeader);

        String device = userAgent.getOperatingSystem().getDeviceType().getName();
        String os = userAgent.getOperatingSystem().getName();
        String browser = userAgent.getBrowser().getName();

        log.info("Incoming Request: method={}, uri={}, device={}, os={}, browser={}",
                httpRequest.getMethod(), httpRequest.getRequestURI(), device, os, browser);

        try {
            chain.doFilter(request, response);
            log.info("Response Completed for uri={}", httpRequest.getRequestURI());
        } catch (Exception e) {
            log.error("Error occurred for uri={}, error={}", httpRequest.getRequestURI(), e.getMessage());
            throw e;
        }
    }
}
