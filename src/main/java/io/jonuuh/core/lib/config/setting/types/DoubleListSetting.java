package io.jonuuh.core.lib.config.setting.types;

import io.jonuuh.core.lib.config.setting.Setting;
import io.jonuuh.core.lib.config.setting.SettingType;

import java.util.Arrays;

public class DoubleListSetting extends Setting<double[]>
{
    public DoubleListSetting(double[] defaultValue, double[] value)
    {
        super(SettingType.DOUBLE_LIST, defaultValue, value);
    }

    public DoubleListSetting(double[] defaultValue)
    {
        super(SettingType.DOUBLE_LIST, defaultValue);
    }

    public DoubleListSetting(DoubleListSetting setting)
    {
        super(setting);
    }

    public DoubleListSetting()
    {
        this(new double[]{});
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "value=" + Arrays.toString(value) + '}';
    }
}
