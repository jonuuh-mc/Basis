package io.jonuuh.basis.v000309.lib.config.setting.types.array;

import io.jonuuh.basis.v000309.lib.config.setting.types.Setting;

import java.util.Arrays;

public final class BoolArrSetting extends Setting<boolean[]>
{
    public BoolArrSetting(boolean[] defaultValue, boolean[] currentValue)
    {
        super(defaultValue, currentValue);
    }

    public BoolArrSetting(boolean[] defaultValue)
    {
        super(defaultValue);
    }

    public BoolArrSetting(BoolArrSetting setting)
    {
        super(setting);
    }

    public BoolArrSetting()
    {
        this(new boolean[]{});
    }

    @Override
    public BoolArrSetting copy()
    {
        return new BoolArrSetting(this);
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "currentValue=" + Arrays.toString(currentValue) + '}';
    }
}
