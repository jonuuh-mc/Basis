package io.jonuuh.core.module.config.setting.types;

import io.jonuuh.core.module.config.setting.Setting;
import io.jonuuh.core.module.config.setting.SettingType;

public class IntSetting extends Setting<Integer>
{
    public IntSetting(int defaultValue)
    {
        super(SettingType.INTEGER, defaultValue);
    }

    public IntSetting()
    {
        this(0);
    }
}
