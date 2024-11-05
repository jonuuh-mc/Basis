package io.jonuuh.core.lib.config.setting.types.single;

import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.config.setting.types.SettingType;

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

    @Override
    public IntSetting copy()
    {
        return new IntSetting(this);
    }
}
