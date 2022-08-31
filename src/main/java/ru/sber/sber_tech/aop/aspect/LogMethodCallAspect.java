package ru.sber.sber_tech.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
@Aspect
@Component
public class LogMethodCallAspect {

    @Pointcut("@within(ru.sber.sber_tech.aop.annotation.LogMethodCall)")
    void classesWithLogAnnotation() {
    }

    @Pointcut("@annotation(ru.sber.sber_tech.aop.annotation.LogMethodCall)")
    void methodsWithLogAnnotation() {
    }

    @Around("classesWithLogAnnotation() || methodsWithLogAnnotation()")
    public Object handleMethodCall(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodCall = (MethodSignature) pjp.getSignature();

        logInMethod(methodCall, pjp);
        Object resultMethodCalling = pjp.proceed();
        logOutMethod(methodCall, resultMethodCalling);

        return resultMethodCalling;
    }

    private void logInMethod(MethodSignature methodCall, ProceedingJoinPoint pjp) {
        Parameter[] parameters = methodCall.getMethod().getParameters();
        Object[] args = pjp.getArgs();

        log.debug(
                "Method {} - start, parameters: {}",
                methodCall.getName(),
                IntStream.range(0, parameters.length)
                        .mapToObj(i -> String.format("%s = %s", parameters[i].getName(), args[i].toString()))
                        .collect(Collectors.joining(", "))
        );
    }

    private void logOutMethod(MethodSignature methodCall, Object returnValue) {
        log.debug(
                "Method {} - end, returnValue: {}",
                methodCall.getName(),
                returnValue
        );
    }
}
