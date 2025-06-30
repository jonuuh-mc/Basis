package io.jonuuh.basis.lib.config.setting.types.single;

import io.jonuuh.basis.lib.config.setting.types.Setting;

public final class IntSetting extends Setting<Integer>
{
    public IntSetting(int defaultValue, int currentValue)
    {
        super(defaultValue, currentValue);
    }

    public IntSetting(int defaultValue)
    {
        super(defaultValue);
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
