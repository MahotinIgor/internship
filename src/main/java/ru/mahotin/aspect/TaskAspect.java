package ru.mahotin.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.mahotin.entity.Task;
import ru.mahotin.exception.TaskNotFoundException;

@Component
@Aspect
@Order(2)
public class TaskAspect {
    static final Logger log =
            LoggerFactory.getLogger(TaskAspect.class);

    @Around("@annotation(LogCreateEntity)")
    public Object logCreateNewEntityFromDto(final ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Hello from aspect! Before create Task");
        Object res = joinPoint.proceed();
        log.info("Hello from aspect! After create Task");
        log.info("Task = " + res);
        return res;
    }

    @Before("@annotation(LogTaskMapper) && args(task)")
    public void loggingBeforeMapping(final Task task) {
        log.info("Hello from aspect! Order = 2. Before Mapping args = " + task);
    }
    @After("@annotation(LogDeleteTask) && args(id)")
    public void loggingDeleteTask(final Long id) {
        log.info("Delete Task with id = " + id);
    }

    @AfterReturning(
            value = "execution(* ru.mahotin.service.impl.TaskServiceImpl.update(..))",
            returning = "result"
    )
    public void logReceivedDtoToUpdate(final Object result) {
        log.info("Before update method dto = " + result);
    }

    @AfterThrowing(
            value = "@annotation(LogErrorGetTaskById) && args(id)",
            throwing = "ex")
    public void logExceptionNotFoundUser(final TaskNotFoundException ex,
                                         final Long id) {

        log.error(ex.getMessage() + " with id = " + id);
    }
}
