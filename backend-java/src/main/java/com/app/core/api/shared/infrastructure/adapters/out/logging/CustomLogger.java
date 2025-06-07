package com.app.core.api.shared.infrastructure.adapters.out.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

/**
 * Logger customizado que automaticamente resolve o contexto de classe e método para logging.
 * 
 * Uso:
 * 
 * private final CustomLogger logger = CustomLogger.getLogger(getClass());
 * 
 * logger.info("mensagem");
 */
public class CustomLogger {

    private final Logger logger;
    private final Class<?> clazz;

    private CustomLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
        this.clazz = clazz;
    }

    public static CustomLogger getLogger(Class<?> clazz) {
        return new CustomLogger(clazz);
    }

    public void info(String message, Object... args) {
        log(LocationAwareLogger.INFO_INT, message, args);
    }

    public void debug(String message, Object... args) {
        log(LocationAwareLogger.DEBUG_INT, message, args);
    }

    public void warn(String message, Object... args) {
        log(LocationAwareLogger.WARN_INT, message, args);
    }

    public void error(String message, Object... args) {
        log(LocationAwareLogger.ERROR_INT, message, args);
    }

    private void log(int level, String message, Object... args) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
        String className = caller.getClassName();
        String methodName = caller.getMethodName();
        String simpleName = className.substring(className.lastIndexOf('.') + 1);
        String formattedMessage = String.format("[%s::%s] %s", simpleName, methodName, message);

        switch (level) {
            case LocationAwareLogger.INFO_INT -> logger.info(formattedMessage, args);
            case LocationAwareLogger.DEBUG_INT -> logger.debug(formattedMessage, args);
            case LocationAwareLogger.WARN_INT -> logger.warn(formattedMessage, args);
            case LocationAwareLogger.ERROR_INT -> logger.error(formattedMessage, args);
        }
    }
} 