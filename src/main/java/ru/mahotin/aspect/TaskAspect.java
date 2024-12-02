package ru.mahotin.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TaskAspect {
    static final Logger log =
            LoggerFactory.getLogger(TaskAspect.class);
    @Around("@annotation(LogCreateEntity)")
    public Object logCreateNewEntityFromDto(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Hello from aspect! Before");
        Object res =  joinPoint.proceed();
        log.info("Hello from aspect! After");
        return res;
    }

}
