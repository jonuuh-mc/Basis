package io.jonuuh.core.lib.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO: is this class really necessary?
 *
 * @see net.minecraft.util.LoggingPrintStream
 * @see net.minecraft.init.Bootstrap#redirectOutputToLog()
 *
 */
public class Log4JLogger
{
    public static Log4JLogger INSTANCE;
    private final Logger LOGGER;

    public static void createInstance(String modID)
    {
        if (INSTANCE != null)
        {
            throw new IllegalStateException("ModLogger instance has already been created");
        }
        INSTANCE = new Log4JLogger(modID);
    }

    private Log4JLogger(String modID)
    {
        this.LOGGER = LogManager.getLogger(modID);
    }

    public void trace(Object message, Object... params)
    {
        log(Level.TRACE, message, params);
    }

    public void debug(Object message, Object... params)
    {
        log(Level.DEBUG, message, params);
    }

    public void info(Object message, Object... params)
    {
        log(Level.INFO, message, params);
    }

    public void warn(Object message, Object... params)
    {
        log(Level.WARN, message, params);
    }

    public void error(Object message, Object... params)
    {
        log(Level.ERROR, message, params);
    }

    public void fatal(Object message, Object... params)
    {
        log(Level.FATAL, message, params);
    }

    public void log(Level level, Object message, Object... params)
    {
        StackTraceElement trace = getStackTraceElement();
        String header = String.format("[%s.%s(%s:%s)]: ", trace.getClassName(), trace.getMethodName(), trace.getFileName(), trace.getLineNumber());
        String messageStr = header + message.toString();
        LOGGER.log(level, messageStr, params);
    }

    private StackTraceElement getStackTraceElement()
    {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements[Math.min(4, stackTraceElements.length)];
    }
}
