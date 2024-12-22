package com.backend.immilog.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("LoggingAspect 클래스")
class LoggingAspectTest {

    private final LoggingAspect loggingAspect = new LoggingAspect();
    private final HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    private final HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    private final ProceedingJoinPoint mockJoinPoint = mock(ProceedingJoinPoint.class);

    @BeforeEach
    void setUp() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest, mockResponse));
    }

    @Test
    @DisplayName("요청 및 응답 로깅 성공")
    void logRequestAndResponse_shouldLogRequestAndResponseSuccessfully() throws Throwable {
        when(mockRequest.getMethod()).thenReturn("POST");
        when(mockRequest.getRequestURI()).thenReturn("/test");
        when(mockRequest.getReader()).thenReturn(new BufferedReader(new StringReader("{\"key\":\"value\"}")));

        when(mockResponse.getStatus()).thenReturn(200);

        Object mockResponseBody = "response-body";
        when(mockJoinPoint.proceed()).thenReturn(mockResponseBody);

        Object result = loggingAspect.logRequestAndResponse(mockJoinPoint);

        assertEquals(mockResponseBody, result);

        verify(mockJoinPoint, times(1)).proceed();
        verify(mockRequest, times(1)).getMethod();
        verify(mockRequest, times(1)).getRequestURI();
        verify(mockRequest, times(1)).getReader();
        verify(mockResponse, times(1)).getStatus();
    }

    @Test
    @DisplayName("로그 출력 시 예외가 발생하면 예외를 처리하고 에러 로깅")
    void logRequestAndResponse_shouldHandleExceptionAndLogError() throws Throwable {
        when(mockRequest.getMethod()).thenReturn("GET");
        when(mockRequest.getRequestURI()).thenReturn("/error");
        when(mockRequest.getReader()).thenReturn(new BufferedReader(new StringReader("")));

        RuntimeException exception = new RuntimeException("Test Exception");
        when(mockJoinPoint.proceed()).thenThrow(exception);

        try {
            loggingAspect.logRequestAndResponse(mockJoinPoint);
        } catch (RuntimeException e) {
            assertEquals(exception, e);
        }

        verify(mockJoinPoint, times(1)).proceed();
    }
}
