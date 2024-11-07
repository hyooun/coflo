package com.reviewping.coflo.global.aop;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TimerAspect {

    private final MeterRegistry meterRegistry;

    @Around("execution(* com.reviewping.coflo.domain..*(..))") // 대상 패키지 내 모든 메소드
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        //        String timerName = String.format("%s.%s", className, methodName);

        Timer timer =
                Timer.builder("method.execution.time")
                        .description("Execution time of method")
                        .tag("class", className)
                        .tag("method", methodName)
                        .register(meterRegistry);

        return timer.recordCallable(
                () -> {
                    try {
                        return joinPoint.proceed();
                    } catch (Throwable throwable) {
                        try {
                            throw throwable;
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }
}
