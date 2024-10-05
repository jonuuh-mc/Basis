package io.jonuuh.configlib.setting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings
{
    public final Map<String, Setting<?>> settingMap;
    private String configurationCategory;

    public Settings(List<SettingDefinition> settingDefinitions)
    {
        this.settingMap = new HashMap<>();

        for (SettingDefinition settingDefinition : settingDefinitions)
        {
            settingMap.put(settingDefinition.name, createSetting(settingDefinition.type));
        }
    }

    public String getConfigurationCategory()
    {
        return configurationCategory;
    }

    public void setConfigurationCategory(String configurationCategory)
    {
        this.configurationCategory = configurationCategory;
    }

    public void reset()
    {
        // TODO:
    }

    private Setting<?> createSetting(SettingType type)
    {
        switch (type)
        {
            case BOOLEAN:
                return new BoolSetting();
            case DOUBLE:
                return new DoubleSetting();
            case INTEGER:
                return new IntSetting();
            case STRING:
                return new StringSetting();
            default:
                return null;
        }
    }

    public Setting<?> get(String settingName)
    {
        return settingMap.get(settingName);
    }

    public Boolean getBoolSettingValue(String settingName)
    {
        Setting<?> setting = settingMap.get(settingName);
        return (setting instanceof BoolSetting) ? ((BoolSetting) setting).getValue() : false;
    }

    public Integer getIntSettingValue(String settingName)
    {
        Setting<?> setting = settingMap.get(settingName);
        return (setting instanceof IntSetting) ? ((IntSetting) setting).getValue() : 0;
    }

    public Double getDoubleSettingValue(String settingName)
    {
        Setting<?> setting = settingMap.get(settingName);
        return (setting instanceof DoubleSetting) ? ((DoubleSetting) setting).getValue() : 0.0;
    }

    public String getStringSettingValue(String settingName)
    {
        Setting<?> setting = settingMap.get(settingName);
        return (setting instanceof StringSetting) ? ((StringSetting) setting).getValue() : "";
    }
}
