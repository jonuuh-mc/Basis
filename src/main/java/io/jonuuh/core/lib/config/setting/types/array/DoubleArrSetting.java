package io.jonuuh.core.lib.config.setting.types.array;

import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.config.setting.types.SettingType;

import java.util.Arrays;

public class DoubleArrSetting extends Setting<double[]>
{
    public DoubleArrSetting(double[] defaultValue, double[] value)
    {
        super(SettingType.DOUBLE_ARRAY, defaultValue, value);
    }

    public DoubleArrSetting(double[] defaultValue)
    {
        super(SettingType.DOUBLE_ARRAY, defaultValue);
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
        return this.getClass().getSimpleName() + "{" + "value=" + Arrays.toString(value) + '}';
    }
}
