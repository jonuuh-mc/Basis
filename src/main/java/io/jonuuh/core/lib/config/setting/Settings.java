package io.jonuuh.core.lib.config.setting;

import io.jonuuh.core.lib.config.SettingsConfigurationAdapter;
import io.jonuuh.core.lib.config.setting.types.Setting;

import java.util.HashMap;

public final class Settings extends HashMap<String, Setting<?>>
{
    public final String configurationCategory;

    public Settings(String configurationCategory)
    {
        this.configurationCategory = configurationCategory;
    }

    public Settings()
    {
        this(SettingsConfigurationAdapter.DEFAULT_CATEGORY);
    }

    /**
     * Deep copy the keys and values of these Settings into the given Settings.
     */
    public Settings deepCopyInto(Settings settings)
    {
        for (Entry<String, Setting<?>> entry : this.entrySet())
        {
            settings.put(entry.getKey(), entry.getValue().copy());
        }
        return settings;
    }

    // TODO: won't reset gui elements
    //  bad idea: just reconstruct the guiscreen?
    public void reset()
    {
        for (Setting<?> setting : values())
        {
            setting.reset();
        }
    }

    public void loadCurrentValues()
    {
        SettingsConfigurationAdapter.INSTANCE.loadSettingsCurrentValues(this);
    }

    public void saveCurrentValues()
    {
        SettingsConfigurationAdapter.INSTANCE.saveSettingsCurrentValues(this);
    }

    public void loadDefaultValues()
    {
        SettingsConfigurationAdapter.INSTANCE.loadSettingsDefaultValues(this);
    }

    // TODO: have a gui button to save a default per setting?
    public void saveDefaultValues()
    {
        SettingsConfigurationAdapter.INSTANCE.saveSettingsDefaultValues(this);
    }

    /**
     * Get a Setting given a settingName key
     *
     * @param settingName The key associated with the Setting
     * @param type The class type which the returned Setting will be cast to
     * @return The cast Setting associated with the given key if it exists in this Settings and is of the given type, else null
     */
    public <T extends Setting<?>> T get(String settingName, Class<T> type)
    {
        try
        {
            return type.cast(this.get(settingName));
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    @Override
    public String toString()
    {
        return configurationCategory + "=" + super.toString();
    }
}
