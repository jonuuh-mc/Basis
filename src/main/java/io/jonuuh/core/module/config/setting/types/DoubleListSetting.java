package io.jonuuh.core.module.config.setting.types;

import io.jonuuh.core.module.config.setting.Setting;
import io.jonuuh.core.module.config.setting.SettingType;

import java.util.Arrays;

public class DoubleListSetting extends Setting<double[]>
{
    public DoubleListSetting(double[] defaultValue)
    {
        super(SettingType.DOUBLE_LIST, defaultValue);
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
