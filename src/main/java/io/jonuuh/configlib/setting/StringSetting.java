package io.jonuuh.configlib.setting;

public class StringSetting extends Setting<String>
{
    public StringSetting()
    {
        this.type = SettingType.STRING;
        this.value = "";
    }

    @Override
    public String getValue()
    {
        return value;
    }

    @Override
    public void setValue(String value)
    {
        this.value = value;
    }
}
