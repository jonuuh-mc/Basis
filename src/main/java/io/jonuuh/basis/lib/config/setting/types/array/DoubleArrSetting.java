package io.jonuuh.basis.lib.config.setting.types.array;

import io.jonuuh.basis.lib.config.setting.types.Setting;

import java.util.Arrays;

public final class DoubleArrSetting extends Setting<double[]>
{
    public DoubleArrSetting(double[] defaultValue, double[] currentValue)
    {
        super(defaultValue, currentValue);
    }

    public DoubleArrSetting(double[] defaultValue)
    {
        super(defaultValue);
    }

    public DoubleArrSetting(DoubleArrSetting setting)
    {
        super(setting);
    }

    public DoubleArrSetting()
    {
        this(new double[]{});
    }

    @Override
    public DoubleArrSetting copy()
    {
        return new DoubleArrSetting(this);
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "currentValue=" + Arrays.toString(currentValue) + '}';
    }
}
