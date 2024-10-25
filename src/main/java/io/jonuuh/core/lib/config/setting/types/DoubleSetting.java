package io.jonuuh.core.lib.config.setting.types;

import io.jonuuh.core.lib.config.setting.Setting;
import io.jonuuh.core.lib.config.setting.SettingType;

public class DoubleSetting extends Setting<Double>
{
    public DoubleSetting(double defaultValue, double value)
    {
        super(SettingType.DOUBLE, defaultValue, value);
    }

    public DoubleSetting(double defaultValue)
    {
        super(SettingType.DOUBLE, defaultValue);
    }

    public DoubleSetting(DoubleSetting setting)
    {
        super(setting);
    }

    public DoubleSetting()
    {
        this(0.0);
    }
}
