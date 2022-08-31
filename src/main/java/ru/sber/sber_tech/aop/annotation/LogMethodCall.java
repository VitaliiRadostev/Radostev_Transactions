package ru.sber.sber_tech.aop.annotation;

import ru.sber.sber_tech.aop.aspect.LogMethodCallAspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LogMethodCall {
}
