package io.jonuuh.core.lib.config.setting;

import io.jonuuh.core.lib.config.Config;
import io.jonuuh.core.lib.config.setting.types.Setting;

import java.util.HashMap;

public class Settings extends HashMap<String, Setting<?>>
{
    public final String configurationCategory;

    public Settings(String configurationCategory)
    {
        this.configurationCategory = configurationCategory;
    }

    public Settings()
    {
        this("master");
    }

    /**
     * Deep copy these Settings into a new Settings
     * <p>
     * This also copies the configuration category, which shouldn't be desirable at all (saving to file will overwrite)
     */
    public Settings deepCopy()
    {
        Settings settings = new Settings(this.configurationCategory);
        return deepCopyInto(settings);
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

    // TODO: won't reset ui elements
    public void reset()
    {
        for (Setting<?> setting : values())
        {
            setting.reset();
        }
    }

    public void load()
    {
        Config.INSTANCE.loadSettings(configurationCategory);
    }

    public void save()
    {
        Config.INSTANCE.saveSettings(configurationCategory);
    }

    public void load(String settingName)
    {
        Config.INSTANCE.loadSetting(configurationCategory, settingName);
    }

    public void save(String settingName)
    {
        Config.INSTANCE.saveSetting(configurationCategory, settingName);
    }

    public <T extends Setting<?>> T getAsSettingType(String settingName, Class<T> type)
    {
        try
        {
            return type.cast(get(settingName));
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

//    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////
//
//    public BoolSetting getBoolSetting(String settingName)
//    {
//        Setting<?> setting = get(settingName);
//        return (setting instanceof BoolSetting) ? ((BoolSetting) setting) : null;
//    }
//
//    public IntSetting getIntSetting(String settingName)
//    {
//        Setting<?> setting = get(settingName);
//        return (setting instanceof IntSetting) ? ((IntSetting) setting) : null;
//    }
//
//    public DoubleSetting getDoubleSetting(String settingName)
//    {
//        Setting<?> setting = get(settingName);
//        return (setting instanceof DoubleSetting) ? ((DoubleSetting) setting) : null;
//    }
//
//    public StringSetting getStringSetting(String settingName)
//    {
//        Setting<?> setting = get(settingName);
//        return (setting instanceof StringSetting) ? ((StringSetting) setting) : null;
//    }
//
//    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////
//
//    public BoolArrSetting getBoolArrSetting(String settingName)
//    {
//        Setting<?> setting = get(settingName);
//        return (setting instanceof BoolArrSetting) ? ((BoolArrSetting) setting) : null;
//    }
//
//    public IntArrSetting getIntArrSetting(String settingName)
//    {
//        Setting<?> setting = get(settingName);
//        return (setting instanceof IntArrSetting) ? ((IntArrSetting) setting) : null;
//    }
//
//    public DoubleArrSetting getDoubleArrSetting(String settingName)
//    {
//        Setting<?> setting = get(settingName);
//        return (setting instanceof DoubleArrSetting) ? ((DoubleArrSetting) setting) : null;
//    }
//
//    public StringArrSetting getStringArrSetting(String settingName)
//    {
//        Setting<?> setting = get(settingName);
//        return (setting instanceof StringArrSetting) ? ((StringArrSetting) setting) : null;
//    }
//
//    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////
//
//    public BoolListSetting getBoolListSetting(String settingName)
//    {
//        Setting<?> setting = get(settingName);
//        return (setting instanceof BoolListSetting) ? ((BoolListSetting) setting) : null;
//    }
//
//    public IntListSetting getIntListSetting(String settingName)
//    {
//        Setting<?> setting = get(settingName);
//        return (setting instanceof IntListSetting) ? ((IntListSetting) setting) : null;
//    }
//
//    public DoubleListSetting getDoubleListSetting(String settingName)
//    {
//        Setting<?> setting = get(settingName);
//        return (setting instanceof DoubleListSetting) ? ((DoubleListSetting) setting) : null;
//    }
//
//    public StringListSetting getStringListSetting(String settingName)
//    {
//        Setting<?> setting = get(settingName);
//        return (setting instanceof StringListSetting) ? ((StringListSetting) setting) : null;
//    }
}
