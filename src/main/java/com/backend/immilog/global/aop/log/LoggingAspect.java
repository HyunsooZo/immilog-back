package com.backend.immilog.global.aop.log;

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
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Around("within(@org.springframework.stereotype.Controller *) || " +
            "within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logHttpRequests(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        HttpServletResponse response = attributes != null ? attributes.getResponse() : null;

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        String methodName = joinPoint.getSignature().toShortString();
        String requestBody = getRequestBody(wrappedRequest);

        logRequest(wrappedRequest, methodName, joinPoint.getArgs(), requestBody);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            logException(ex);
            throw ex;
        }

        logResponse(wrappedResponse, result);
        wrappedResponse.copyBodyToResponse();
        return result;
    }

    private void logRequest(
            HttpServletRequest request,
            String methodName,
            Object[] args,
            String body
    ) {
        if (request != null) {
            logger.info(
                    "[IMMILOG Incoming Request] HTTP Method: {}, URL: {}, Body: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    toPrettyJson(args)
            );
        }
    }

    private void logResponse(
            HttpServletResponse response,
            Object result
    ) {
        if (response != null) {
            logger.info(
                    "[IMMILOG Outgoing Response] Status: {},Result: {}",
                    response.getStatus(),
                    toPrettyJson(result)
            );
        }
    }

    private void logException(Throwable ex) {
        logger.error("Exception occurred: ", ex);
    }

    private String getRequestBody(ContentCachingRequestWrapper request) throws IOException {
        byte[] buf = request.getContentAsByteArray();
        return new String(buf, 0, buf.length, StandardCharsets.UTF_8);
    }

    private String toPrettyJson(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            return "Unable to convert to JSON: " + e.getMessage();
        }
    }
}