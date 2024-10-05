package io.jonuuh.configlib.setting;

public class SettingDefinition
{
    public final String name;
    public final SettingType type;

    public SettingDefinition(String name, SettingType type)
    {
        this.name = name;
        this.type = type;
    }
}
