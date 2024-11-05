package io.jonuuh.core.lib.config.setting.types.array;

import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.config.setting.types.SettingType;

import java.util.Arrays;

public class IntArrSetting extends Setting<int[]>
{
    public IntArrSetting(int[] defaultValue, int[] value)
    {
        super(SettingType.INTEGER_ARRAY, defaultValue, value);
    }

    public IntArrSetting(int[] defaultValue)
    {
        super(SettingType.INTEGER_ARRAY, defaultValue);
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
        return this.getClass().getSimpleName() + "{" + "value=" + Arrays.toString(value) + '}';
    }
}
