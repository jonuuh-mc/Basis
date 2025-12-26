package io.jonuuh.basis.v000309.lib.config.setting.types.single;

import io.jonuuh.basis.v000309.lib.config.setting.types.Setting;

public final class BoolSetting extends Setting<Boolean>
{
    public BoolSetting(boolean defaultValue, boolean currentValue)
    {
        super(defaultValue, currentValue);
    }

    public BoolSetting(boolean defaultValue)
    {
        super(defaultValue);
    }

    public BoolSetting(BoolSetting setting)
    {
        super(setting);
    }

    public BoolSetting()
    {
        this(false);
    }

    @Override
    public BoolSetting copy()
    {
        return new BoolSetting(this);
    }
}
