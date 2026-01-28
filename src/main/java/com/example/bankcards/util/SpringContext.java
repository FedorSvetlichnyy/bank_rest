package com.example.bankcards.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {
    private static volatile ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ctx = applicationContext;
    }

    public static <T> T getBean(Class<T> type) {
        ApplicationContext c = ctx;
        if (c == null) {
            throw new IllegalStateException("Spring ApplicationContext not initialized");
        }
        return c.getBean(type);
    }
}

