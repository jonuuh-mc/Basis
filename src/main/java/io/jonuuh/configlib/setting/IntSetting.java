package io.jonuuh.configlib.setting;

public class IntSetting extends Setting<Integer>
{
    public IntSetting()
    {
        this.type = SettingType.INTEGER;
        this.value = 0;
    }

    @Override
    public Integer getValue()
    {
        return value;
    }

    @Override
    public void setValue(Integer value)
    {
        this.value = value;
    }
}
