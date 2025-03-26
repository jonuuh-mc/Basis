package io.jonuuh.core.lib.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO: is this class really necessary? this is so overengineered and pointless
 *
 * @see net.minecraft.util.LoggingPrintStream
 * @see net.minecraft.init.Bootstrap#redirectOutputToLog()
 *
 */
public class Log4JLogger
{
    public static Log4JLogger INSTANCE;
    private final Logger LOGGER;
    private final String modID;
//    private final boolean isDevEnvironment;

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
        this.modID = modID;

//        // TODO: jank methods of detecting this but probably fine? maybe find a better way
////        this.isDevEnviroment = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
//        Session session = Minecraft.getMinecraft().getSession();
//        this.isDevEnvironment = session.getUsername().equals(session.getPlayerID());

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

        // TODO: add some auto array -> string handling?
//        System.out.println(Arrays.toString(new int[]{1, 2, 3}));

        String header = String.format("[%s.%s(%s:%s)]: ", trace.getClassName(), trace.getMethodName(), trace.getFileName(), trace.getLineNumber());
        String messageStr = header + message.toString();

//        String header = (isDevEnvironment)
//                ? String.format(".(%s:%s): ", trace.getFileName(), trace.getLineNumber())
//                : String.format("[%s.%s:%s]: ", trace.getClassName(), trace.getMethodName(), trace.getLineNumber());

//        String header = (isDevEnvironment)
//                ? ".({}:{}): {}"
//                : "[{}.{}:{}]: {}";
//        Object[] params = (isDevEnvironment)
//                ? new Object[]{trace.getFileName(), trace.getLineNumber(), object.toString()}
//                : new Object[]{trace.getClassName(), trace.getMethodName(), trace.getLineNumber(), object.toString()};

        LOGGER.log(level, messageStr, params);
    }

    private StackTraceElement getStackTraceElement()
    {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//        System.out.println(Arrays.toString(stackTraceElements));
//        System.out.println(stackTraceElements[4]);
        return stackTraceElements[Math.min(4, stackTraceElements.length)];
    }
}
