package io.jonuuh.core.module.config.setting.types;

import io.jonuuh.core.module.config.setting.Setting;
import io.jonuuh.core.module.config.setting.SettingType;

import java.util.Arrays;

public class IntListSetting extends Setting<int[]>
{
    public IntListSetting(int[] defaultValue)
    {
        super(SettingType.INTEGER_LIST, defaultValue);
    }

    public IntListSetting()
    {
        this(new int[]{});
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "value=" + Arrays.toString(value) + '}';
    }
}
