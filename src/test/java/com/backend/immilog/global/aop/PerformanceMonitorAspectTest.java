package com.backend.immilog.global.aop;

import com.backend.immilog.global.aop.monitor.PerformanceMonitorAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

@DisplayName("PerformanceMonitorAspect 클래스")
class PerformanceMonitorAspectTest {

    private final PerformanceMonitorAspect aspect = new PerformanceMonitorAspect();
    private final ProceedingJoinPoint mockJoinPoint = mock(ProceedingJoinPoint.class);
    private final Signature mockSignature = mock(Signature.class);

    @Test
    @DisplayName("실행 시간 로깅 성공")
    void monitorExecutionTime_shouldLogWarningForLongExecution() throws Throwable {
        when(mockJoinPoint.getSignature()).thenReturn(mockSignature);
        when(mockSignature.getDeclaringTypeName()).thenReturn("com.backend.immilog.TestService");
        when(mockSignature.getName()).thenReturn("testMethod");

        when(mockJoinPoint.proceed()).thenAnswer(invocation -> {
            Thread.sleep(600);
            return "result";
        });

        aspect.monitorExecutionTime(mockJoinPoint);

        verify(mockJoinPoint, times(1)).proceed();
    }

    @Test
    @DisplayName("실행 시간 로깅 실패")
    void monitorExecutionTime_shouldNotLogWarningForShortExecution() throws Throwable {
        when(mockJoinPoint.getSignature()).thenReturn(mockSignature);
        when(mockSignature.getDeclaringTypeName()).thenReturn("com.backend.immilog.TestService");
        when(mockSignature.getName()).thenReturn("testMethod");

        when(mockJoinPoint.proceed()).thenReturn("result");

        aspect.monitorExecutionTime(mockJoinPoint);

        verify(mockJoinPoint, times(1)).proceed();
    }
}
