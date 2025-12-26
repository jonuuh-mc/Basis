package io.jonuuh.basis.v000309.lib.config.setting.types.single;

import io.jonuuh.basis.v000309.lib.config.setting.types.Setting;

public final class DoubleSetting extends Setting<Double>
{
    public DoubleSetting(double defaultValue, double currentValue)
    {
        super(defaultValue, currentValue);
    }

    public DoubleSetting(double defaultValue)
    {
        super(defaultValue);
    }

    public DoubleSetting(DoubleSetting setting)
    {
        super(setting);
    }

    public DoubleSetting()
    {
        this(0.0);
    }

    @Override
    public DoubleSetting copy()
    {
        return new DoubleSetting(this);
    }
}
