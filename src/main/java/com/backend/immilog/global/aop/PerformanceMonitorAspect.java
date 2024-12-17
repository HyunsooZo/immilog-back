package com.backend.immilog.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceMonitorAspect {
    @Around("@annotation(com.backend.immilog.global.aop.PerformanceMonitor)")
    public Object monitorExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;
        if (executionTime > 500) { // 500ms 이상만
            log.warn("[PERFORMANCE] {}.{} executed in {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    executionTime);
        }
        return result;
    }

}
