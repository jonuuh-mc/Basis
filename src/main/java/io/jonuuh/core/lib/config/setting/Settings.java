package io.jonuuh.core.lib.config.setting;

import io.jonuuh.core.lib.config.ConfigManager;
import io.jonuuh.core.lib.config.SettingsConfigurationAdapter;
import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.config.setting.types.array.BoolArrSetting;
import io.jonuuh.core.lib.config.setting.types.array.DoubleArrSetting;
import io.jonuuh.core.lib.config.setting.types.array.IntArrSetting;
import io.jonuuh.core.lib.config.setting.types.array.StringArrSetting;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.single.DoubleSetting;
import io.jonuuh.core.lib.config.setting.types.single.IntSetting;
import io.jonuuh.core.lib.config.setting.types.single.StringSetting;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A collection of {@link Setting}s for some part of a mod.
 * <p>
 * A {@code Settings} object should be linked via a {@link SettingsConfigurationAdapter} singleton to a specific Forge
 * {@link net.minecraftforge.common.config.Configuration Configuration}'s
 * {@link net.minecraftforge.common.config.ConfigCategory ConfigCategory}.
 * <p>
 * Each {@link Setting} is associated in a {@link Settings#settingMap map} with a string key which is suggested to be derived from an enum.
 *
 * @see SettingsConfigurationAdapter
 * @see Setting
 */
public final class Settings
{
    public final String configurationCategory;
    private final Map<String, Setting<?>> settingMap;
    private final String modID;

    public Settings(String modID, String configurationCategory)
    {
        this.configurationCategory = configurationCategory;
        this.settingMap = new HashMap<>();
        this.modID = modID;
    }

    public Settings(String modID)
    {
        this(modID, ConfigManager.DEFAULT_CATEGORY);
    }

    /**
     * Hopefully designed to not be used, but it's here if it's really needed
     */
    public Map<String, Setting<?>> getSettingMap()
    {
        return settingMap;
    }

    public Set<Map.Entry<String, Setting<?>>> getEntrySet()
    {
        return settingMap.entrySet();
    }

    /**
     * Associate the given enum's string representation with the given {@link Setting} in this Settings' {@link Settings#settingMap}.
     *
     * @param <E> An enum type
     * @param settingKey An enum whose {@link Enum#toString() toString()} is used as the key to the {@link Setting}
     * @param setting The {@link Setting} to be associated with the key
     * @return The previous {@link Setting} associated with the key, if it existed, otherwise null
     * @see Map#put(Object, Object)
     */
    public <E extends Enum<E>> Setting<?> put(E settingKey, Setting<?> setting)
    {
        return settingMap.put(settingKey.toString(), setting);
    }

    /**
     * Retrieve the {@link Setting} to which the given enum's string representation is associated in this Settings' {@link Settings#settingMap}.
     *
     * @param <E> An enum type
     * @param settingKey An enum whose {@link Enum#toString() toString()} is used as the key to the {@link Setting}
     * @return The {@link Setting} associated with the key, if it existed, otherwise null
     * @see Map#get(Object)
     */
    public <E extends Enum<E>> Setting<?> get(E settingKey)
    {
        return settingMap.get(settingKey.toString());
    }

    /**
     * Deep copy the keys and values of this Settings into the given Settings.
     */
    public void deepCopyInto(Settings settings)
    {
        for (Map.Entry<String, Setting<?>> entry : this.settingMap.entrySet())
        {
            settings.settingMap.put(entry.getKey(), entry.getValue().copy());
        }
    }

    // TODO: won't reset gui elements
    //  bad idea: just reconstruct the guiscreen?
    public void resetSettings()
    {
        for (Setting<?> setting : settingMap.values())
        {
            setting.reset();
        }
    }

    public <E extends Enum<E>> void resetSetting(E settingKey)
    {
        for (String key : settingMap.keySet())
        {
            if (key.equals(settingKey.toString()))
            {
                settingMap.get(key).reset();
            }
        }
    }

    /**
     * @see SettingsConfigurationAdapter#loadSettingsDefaultValues(Settings)
     */
    public void loadDefaultValues()
    {
        ConfigManager.getAdapter(modID).loadSettingsDefaultValues(this);
    }

    /**
     * @see SettingsConfigurationAdapter#loadSettingsCurrentValues(Settings)
     */
    public void loadCurrentValues()
    {
        ConfigManager.getAdapter(modID).loadSettingsCurrentValues(this);
    }

    // TODO: have a gui button to save a default per setting?

    /**
     * @see SettingsConfigurationAdapter#saveSettingsDefaultValues(Settings)
     */
    public void saveDefaultValues()
    {
        ConfigManager.getAdapter(modID).saveSettingsDefaultValues(this);
    }

    /**
     * @see SettingsConfigurationAdapter#saveSettingsCurrentValues(Settings)
     */
    public void saveCurrentValues()
    {
        ConfigManager.getAdapter(modID).saveSettingsCurrentValues(this);
    }

    public <E extends Enum<E>> BoolSetting getBoolSetting(E settingKey)
    {
        try
        {
            return (BoolSetting) get(settingKey);
        }
        catch (ClassCastException e)
        {
            System.out.printf("'%s' is not a BoolSetting! Returning a new BoolSetting (%s) %n", settingKey.toString(), e);
            return new BoolSetting();
        }
    }

    public <E extends Enum<E>> DoubleSetting getDoubleSetting(E settingKey)
    {
        try
        {
            return (DoubleSetting) get(settingKey);
        }
        catch (ClassCastException e)
        {
            System.out.printf("'%s' is not a DoubleSetting! Returning a new DoubleSetting (%s) %n", settingKey.toString(), e);
            return new DoubleSetting();
        }
    }

    public <E extends Enum<E>> IntSetting getIntSetting(E settingKey)
    {
        try
        {
            return (IntSetting) get(settingKey);
        }
        catch (ClassCastException e)
        {
            System.out.printf("'%s' is not a IntSetting! Returning a new IntSetting (%s) %n", settingKey.toString(), e);
            return new IntSetting();
        }
    }

    public <E extends Enum<E>> StringSetting getStringSetting(E settingKey)
    {
        try
        {
            return (StringSetting) get(settingKey);
        }
        catch (ClassCastException e)
        {
            System.out.printf("'%s' is not a StringSetting! Returning a new StringSetting (%s) %n", settingKey.toString(), e);
            return new StringSetting();
        }
    }

    public <E extends Enum<E>> BoolArrSetting getBoolArrSetting(E settingKey)
    {
        try
        {
            return (BoolArrSetting) get(settingKey);
        }
        catch (ClassCastException e)
        {
            System.out.printf("'%s' is not a BoolArrSetting! Returning a new BoolArrSetting (%s) %n", settingKey.toString(), e);
            return new BoolArrSetting();
        }
    }

    public <E extends Enum<E>> DoubleArrSetting getDoubleArrSetting(E settingKey)
    {
        try
        {
            return (DoubleArrSetting) get(settingKey);
        }
        catch (ClassCastException e)
        {
            System.out.printf("'%s' is not a DoubleArrSetting! Returning a new DoubleArrSetting (%s) %n", settingKey.toString(), e);
            return new DoubleArrSetting();
        }
    }

    public <E extends Enum<E>> IntArrSetting getIntArrSetting(E settingKey)
    {
        try
        {
            return (IntArrSetting) get(settingKey);
        }
        catch (ClassCastException e)
        {
            System.out.printf("'%s' is not a IntArrSetting! Returning a new IntArrSetting (%s) %n", settingKey.toString(), e);
            return new IntArrSetting();
        }
    }

    public <E extends Enum<E>> StringArrSetting getStringArrSetting(E settingKey)
    {
        try
        {
            return (StringArrSetting) get(settingKey);
        }
        catch (ClassCastException e)
        {
            System.out.printf("'%s' is not a StringArrSetting! Returning a new StringArrSetting (%s) %n", settingKey.toString(), e);
            return new StringArrSetting();
        }
    }

    @Override
    public String toString()
    {
        return configurationCategory + "=" + settingMap.toString();
    }
}
