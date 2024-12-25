package com.backend.immilog.global.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";

    @Around("execution(* com.backend.immilog..*(..)) && @within(org.springframework.web.bind.annotation.RestController)")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getCurrentHttpRequest();
        HttpServletResponse response = getCurrentHttpResponse();

        // 요청 로깅
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("method", Objects.requireNonNull(request).getMethod());
        requestMap.put("uri", request.getRequestURI());
        requestMap.put("body", getRequestBody(request));

        String requestLog = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestMap);
        logger.info(ANSI_YELLOW + "[Incoming Request] : {}" + ANSI_RESET, requestLog);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            logger.error("Exception occurred: ", ex);
            throw ex;
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", Objects.requireNonNull(response).getStatus());
        responseMap.put("body", result);

        String responseLog = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseMap);
        logger.info(ANSI_GREEN + "Outgoing Response: {}" + ANSI_RESET, responseLog);

        return result;
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    private HttpServletResponse getCurrentHttpResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getResponse() : null;
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    private Map<String, String> getHeadersInfo(HttpServletResponse response) {
        Map<String, String> headers = new HashMap<>();
        for (String headerName : response.getHeaderNames()) {
            headers.put(headerName, response.getHeader(headerName));
        }
        return headers;
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        request.setCharacterEncoding("UTF-8");
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }
}
