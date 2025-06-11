package io.jonuuh.core.lib.config;

import io.jonuuh.core.lib.config.setting.Settings;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ConfigManager
{
    private static final Map<String, SettingsConfigurationAdapter> adapters = new HashMap<>();
    public static final String DEFAULT_CATEGORY = "master";

    /**
     * Get a SettingsConfigurationAdapter instance given a key in the {@link ConfigManager#adapters} map.
     * <p>
     * If the key is not associated with any SettingsConfigurationAdapter, returns null.
     * <p>
     * Use {@link ConfigManager#putAdapter(String, File, Settings)} to create non-default SettingsConfigurationAdapters.
     *
     * @param key A key for a SettingsConfigurationAdapter, this should usually be a mod id or mod name.
     * @return The SettingsConfigurationAdapter associated with the key.
     */
    public static SettingsConfigurationAdapter getAdapter(String key)
    {
        if (!adapters.containsKey(key))
        {
            System.out.printf("Tried to retrieve nonexistent SettingsConfigurationAdapter with the key '%s', returning null!", key);
        }
        return adapters.get(key);
    }

    /**
     * Create a new SettingsConfigurationAdapter and associate it with the given key in the {@link ConfigManager#adapters} map.
     * <p>
     * This will overwrite any existing SettingsConfigurationAdapter already associated with this key.
     *
     * @param key A key for a SettingsConfigurationAdapter, this should usually be a mod id or mod name.
     * @param file The file used for the Configuration, usually the {@link FMLPreInitializationEvent#getSuggestedConfigurationFile() suggested} file.
     * @param settingsList A list of settings to use.
     * @return The newly created SettingsConfigurationAdapter.
     */
    public static SettingsConfigurationAdapter putAdapter(String key, File file, List<Settings> settingsList)
    {
        adapters.put(key, new SettingsConfigurationAdapter(file, settingsList));
        return adapters.get(key);
    }

    /**
     * @see ConfigManager#putAdapter(String, File, List)
     */
    public static SettingsConfigurationAdapter putAdapter(String key, File file, Settings settings)
    {
        return putAdapter(key, file, Collections.singletonList(settings));
    }
}
