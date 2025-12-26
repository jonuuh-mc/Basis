package io.jonuuh.basis.v000309.lib.config.setting.types.array;

import io.jonuuh.basis.v000309.lib.config.setting.types.Setting;

import java.util.Arrays;

public final class IntArrSetting extends Setting<int[]>
{
    public IntArrSetting(int[] defaultValue, int[] currentValue)
    {
        super(defaultValue, currentValue);
    }

    public IntArrSetting(int[] defaultValue)
    {
        super(defaultValue);
    }

    public IntArrSetting(IntArrSetting setting)
    {
        super(setting);
    }

    public IntArrSetting()
    {
        this(new int[]{});
    }

    @Override
    public IntArrSetting copy()
    {
        return new IntArrSetting(this);
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "currentValue=" + Arrays.toString(currentValue) + '}';
    }
}
