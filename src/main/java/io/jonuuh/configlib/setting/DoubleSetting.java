package io.jonuuh.configlib.setting;

public class DoubleSetting extends Setting<Double>
{
    public DoubleSetting()
    {
        this.type = SettingType.DOUBLE;
        this.value = 0.0;
    }

    @Override
    public Double getValue()
    {
        return value;
    }

    @Override
    public void setValue(Double value)
    {
        this.value = value;
    }
}
