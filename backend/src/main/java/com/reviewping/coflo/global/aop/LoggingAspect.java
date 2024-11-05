package com.reviewping.coflo.global.aop;

import com.reviewping.coflo.global.error.exception.BusinessException;
import com.reviewping.coflo.global.util.WebHookUtils;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final String WEBHOOK_MESSAGE = "Error occurs : Webhooks sent";

    @Pointcut("within(@com.reviewping.coflo.global.aop.LogExecution *)")
    public void beanAnnotatedWithLogExecution() {}

    @Around("beanAnnotatedWithLogExecution()")
    public Object logExceptionsAndInputs(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        try {
            Object result = joinPoint.proceed();
            log.info(
                    "{}.{}() with arguments:{} Successfully executed",
                    className,
                    methodName,
                    Arrays.toString(args));
            return result;
        } catch (Exception ex) {
            String errorLogMessage = getErrorLogMessage(className, methodName, args, ex);
            WebHookUtils.sendWebHookMessage(errorLogMessage);
            log.info(WEBHOOK_MESSAGE);
            log.info("{}", errorLogMessage);
            throw ex;
        }
    }

    private String getErrorLogMessage(
            String className, String methodName, Object[] args, Exception ex) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(
                String.format(
                        "```\nException in %s.%s() called with arguments: %s\n",
                        className, methodName, Arrays.toString(args)));

        messageBuilder.append("Exception message: ").append(ex.getMessage()).append("\n");

        if (ex instanceof BusinessException) {
            Throwable cause = ex.getCause();
            if (cause != null) {
                messageBuilder.append("Cause: ").append(cause.getMessage()).append("\n");
            }
        }

        messageBuilder.append("```");
        return messageBuilder.toString();
    }
}
