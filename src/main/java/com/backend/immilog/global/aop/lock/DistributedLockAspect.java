package com.backend.immilog.global.aop.lock;

import com.backend.immilog.global.infrastructure.persistence.repository.RedisDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.dao.DataAccessException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class DistributedLockAspect {

    private final RedisDataRepository dataRepository;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    public DistributedLockAspect(RedisDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Around("@annotation(distributedLock)")
    public Object applyLock(
            ProceedingJoinPoint joinPoint,
            DistributedLock distributedLock
    ) throws Throwable {
        String key = evaluateExpression(distributedLock.key(), joinPoint);
        String identifier = evaluateExpression(distributedLock.identifier(), joinPoint);
        int expireTime = distributedLock.expireTime();

        boolean lockAcquired = tryAcquireLock(key, identifier, expireTime);
        if (!lockAcquired) {
            log.warn("Unable to acquire lock for key: {}", key);
            throw new IllegalStateException("Failed to acquire lock for key: " + key);
        }
        try {
            return joinPoint.proceed();
        } finally {
            releaseLock(key, identifier);
        }
    }

    private String evaluateExpression(
            String expression,
            ProceedingJoinPoint joinPoint
    ) {
        if (!(joinPoint.getSignature() instanceof MethodSignature methodSignature)) {
            throw new IllegalArgumentException("Unsupported signature type");
        }

        Method method = methodSignature.getMethod();

        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(
                joinPoint.getTarget(),
                method,
                joinPoint.getArgs(),
                new DefaultParameterNameDiscoverer()
        );

        return parser.parseExpression(expression).getValue(context, String.class);
    }


    private boolean tryAcquireLock(
            String key,
            String identifier,
            int expireTime
    ) {
        try {
            Boolean lockAcquired = dataRepository.saveIfAbsent(key, identifier, expireTime);
            return lockAcquired != null && lockAcquired;
        } catch (DataAccessException e) {
            log.error("Failed to acquire lock for key: {}", key, e);
            return false;
        }
    }

    private void releaseLock(
            String key,
            String identifier
    ) {
        try {
            String storedIdentifier = dataRepository.findByKey(key);
            if (identifier.equals(storedIdentifier)) {
                dataRepository.deleteByKey(key);
            }
        } catch (DataAccessException e) {
            log.error("Failed to release lock for key: {}", key, e);
        }
    }
}

