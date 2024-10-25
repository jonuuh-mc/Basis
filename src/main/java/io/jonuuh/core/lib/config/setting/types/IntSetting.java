package io.jonuuh.core.lib.config.setting.types;

import io.jonuuh.core.lib.config.setting.Setting;
import io.jonuuh.core.lib.config.setting.SettingType;

public class IntSetting extends Setting<Integer>
{
    public IntSetting(int defaultValue, int value)
    {
        super(SettingType.INTEGER, defaultValue, value);
    }

    public IntSetting(int defaultValue)
    {
        super(SettingType.INTEGER, defaultValue);
    }

    public IntSetting(IntSetting setting)
    {
        super(setting);
    }

    public IntSetting()
    {
        this(0);
    }
}
