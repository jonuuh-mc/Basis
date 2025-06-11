package io.jonuuh.core.lib.util.logging;

import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;

public final class ChatLoggerManager
{
    private static final Map<String, ChatLogger> loggers = new HashMap<>();

    /**
     * Get a ChatLogger instance given a key in the {@link ChatLoggerManager#loggers} map.
     * <p>
     * If the key is not associated with any ChatLogger, associates a new default ChatLogger with it, then returns that new ChatLogger.
     * <p>
     * Use {@link ChatLoggerManager#putLogger(String, EnumChatFormatting, EnumChatFormatting)} to create non-default ChatLoggers.
     *
     * @param key A key for a ChatLogger, this should usually be a mod id or mod name.
     * @return The ChatLogger associated with the key.
     */
    public static ChatLogger getLogger(String key)
    {
        if (!loggers.containsKey(key))
        {
            loggers.put(key, new ChatLogger(key));
        }
        return loggers.get(key);
    }

    /**
     * Create a new ChatLogger and associate it with the given key in the {@link ChatLoggerManager#loggers} map.
     * <p>
     * This will overwrite any existing ChatLogger already associated with this key.
     *
     * @param key A key for a ChatLogger, this should usually be a mod id or mod name.
     * @param mainColor A main color used to create the ChatLogger.
     * @param accentColor An accent color used to create the ChatLogger.
     * @return The newly created ChatLogger.
     */
    public static ChatLogger putLogger(String key, EnumChatFormatting mainColor, EnumChatFormatting accentColor)
    {
        loggers.put(key, new ChatLogger(key, mainColor, accentColor));
        return loggers.get(key);
    }
}
