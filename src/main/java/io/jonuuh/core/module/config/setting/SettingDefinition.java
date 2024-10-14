package io.jonuuh.core.module.config.setting;

public class SettingDefinition
{
    public final String name;
    public final SettingType type;
    private int length;

    public SettingDefinition(String name, SettingType type, int length)
    {
        this.name = name;
        this.type = type;
        this.length = length;
    }

    public SettingDefinition(String name, SettingType type)
    {
        this(name, type, 1);
    }

    public int getLength()
    {
        return length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }
}
