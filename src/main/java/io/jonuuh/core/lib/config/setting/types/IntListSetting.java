package io.jonuuh.core.lib.config.setting.types;

import io.jonuuh.core.lib.config.setting.Setting;
import io.jonuuh.core.lib.config.setting.SettingType;

import java.util.Arrays;

public class IntListSetting extends Setting<int[]>
{
    public IntListSetting(int[] defaultValue, int[] value)
    {
        super(SettingType.INTEGER_LIST, defaultValue, value);
    }

    public IntListSetting(int[] defaultValue)
    {
        super(SettingType.INTEGER_LIST, defaultValue);
    }

    public IntListSetting(IntListSetting setting)
    {
        super(setting);
    }

    public IntListSetting()
    {
        this(new int[]{});
    }

    @Override
    public IntListSetting copy()
    {
        return new IntListSetting(this);
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "value=" + Arrays.toString(value) + '}';
    }
}
