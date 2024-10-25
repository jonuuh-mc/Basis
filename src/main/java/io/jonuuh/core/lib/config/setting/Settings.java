package io.jonuuh.core.lib.config.setting;

import io.jonuuh.core.lib.config.Config;
import io.jonuuh.core.lib.config.setting.types.BoolListSetting;
import io.jonuuh.core.lib.config.setting.types.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.DoubleListSetting;
import io.jonuuh.core.lib.config.setting.types.DoubleSetting;
import io.jonuuh.core.lib.config.setting.types.IntListSetting;
import io.jonuuh.core.lib.config.setting.types.IntSetting;
import io.jonuuh.core.lib.config.setting.types.StringListSetting;
import io.jonuuh.core.lib.config.setting.types.StringSetting;

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

//    public Settings deepCopy()
//    {
//        Settings settingsClone = new Settings(this.configurationCategory);
//
//        for (Entry<String, Setting<?>> entry : this.entrySet())
//        {
//            settingsClone.put(entry.getKey(), new Setting(entry.getValue()));
//        }
//
//        return settingsClone;
//    }

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
        Config.getInstance().loadSettings(configurationCategory);
    }

    public void save()
    {
        Config.getInstance().saveSettings(configurationCategory);
    }

    public void load(String settingName)
    {
        Config.getInstance().loadSetting(configurationCategory, settingName);
    }

    public void save(String settingName)
    {
        Config.getInstance().saveSetting(configurationCategory, settingName);
    }

    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////

    public BoolSetting getBoolSetting(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof BoolSetting) ? ((BoolSetting) setting) : null;
    }

    public IntSetting getIntSetting(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof IntSetting) ? ((IntSetting) setting) : null;
    }

    public DoubleSetting getDoubleSetting(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof DoubleSetting) ? ((DoubleSetting) setting) : null;
    }

    public StringSetting getStringSetting(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof StringSetting) ? ((StringSetting) setting) : null;
    }

    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////

    public BoolListSetting getBoolListSetting(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof BoolListSetting) ? ((BoolListSetting) setting) : null;
    }

    public IntListSetting getIntListSetting(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof IntListSetting) ? ((IntListSetting) setting) : null;
    }

    public DoubleListSetting getDoubleListSetting(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof DoubleListSetting) ? ((DoubleListSetting) setting) : null;
    }

    public StringListSetting getStringListSetting(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof StringListSetting) ? ((StringListSetting) setting) : null;
    }
}
