package ru.mahotin.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.mahotin.entity.Task;

@Component
@Aspect
@Order(1)
public class TaskAspectSortingByOrder {

    static final Logger log =
            LoggerFactory.getLogger(TaskAspectSortingByOrder.class);

    @Before("@annotation(LogTaskMapper) && args(task)")
    public void loggingBeforeMapping(final Task task) {
        log.info("Hello from aspect! Order = 1. Before Mapping args = " + task);
    }
}
