package io.jonuuh.configlib.setting;

public class BoolSetting extends Setting<Boolean>
{
    public BoolSetting()
    {
        this.type = SettingType.BOOLEAN;
        this.value = false;
    }

    @Override
    public Boolean getValue()
    {
        return value;
    }

    @Override
    public void setValue(Boolean value)
    {
        this.value = value;
    }
}
