package io.jonuuh.core.module.config.setting.types;

import io.jonuuh.core.module.config.setting.Setting;
import io.jonuuh.core.module.config.setting.SettingType;

public class DoubleSetting extends Setting<Double>
{
    public DoubleSetting(double defaultValue)
    {
        super(SettingType.DOUBLE, defaultValue);
    }

    public DoubleSetting()
    {
        this(0.0);
    }
}
