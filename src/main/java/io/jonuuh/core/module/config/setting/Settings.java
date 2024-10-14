package io.jonuuh.core.module.config.setting;

import io.jonuuh.core.module.config.setting.types.BoolListSetting;
import io.jonuuh.core.module.config.setting.types.BoolSetting;
import io.jonuuh.core.module.config.setting.types.DoubleListSetting;
import io.jonuuh.core.module.config.setting.types.DoubleSetting;
import io.jonuuh.core.module.config.setting.types.IntListSetting;
import io.jonuuh.core.module.config.setting.types.IntSetting;
import io.jonuuh.core.module.config.setting.types.StringListSetting;
import io.jonuuh.core.module.config.setting.types.StringSetting;

import java.util.HashMap;

public class Settings extends HashMap<String, Setting<?>>
{
    public final String configurationCategory;

    public Settings(String configurationCategory)
    {
        this.configurationCategory = configurationCategory;
    }

    public void reset()
    {
        for (Setting<?> setting : values())
        {
            setting.reset();
        }
    }

    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////

    public Boolean getBoolSettingValue(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof BoolSetting) ? ((BoolSetting) setting).getValue() : false;
    }

    public Integer getIntSettingValue(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof IntSetting) ? ((IntSetting) setting).getValue() : 0;
    }

    public Double getDoubleSettingValue(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof DoubleSetting) ? ((DoubleSetting) setting).getValue() : 0.0;
    }

    public String getStringSettingValue(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof StringSetting) ? ((StringSetting) setting).getValue() : "";
    }

    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////

    public boolean[] getBoolListSettingValue(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof BoolListSetting) ? ((BoolListSetting) setting).getValue() : new boolean[]{};
    }

    public int[] getIntListSettingValue(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof IntListSetting) ? ((IntListSetting) setting).getValue() : new int[]{};
    }

    public double[] getDoubleListSettingValue(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof DoubleListSetting) ? ((DoubleListSetting) setting).getValue() : new double[]{};
    }

    public String[] getStringListSettingValue(String settingName)
    {
        Setting<?> setting = get(settingName);
        return (setting instanceof StringListSetting) ? ((StringListSetting) setting).getValue() : new String[]{};
    }
}
