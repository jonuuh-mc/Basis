package io.jonuuh.core.lib.config.setting.types;

import io.jonuuh.core.lib.config.setting.Setting;
import io.jonuuh.core.lib.config.setting.SettingType;

import java.util.Arrays;

public class BoolListSetting extends Setting<boolean[]>
{
    public BoolListSetting(boolean[] defaultValue, boolean[] value)
    {
        super(SettingType.BOOLEAN_LIST, defaultValue, value);
    }

    public BoolListSetting(boolean[] defaultValue)
    {
        super(SettingType.BOOLEAN_LIST, defaultValue);
    }

    public BoolListSetting(BoolListSetting setting)
    {
        super(setting);
    }

    public BoolListSetting()
    {
        this(new boolean[]{});
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "value=" + Arrays.toString(value) + '}';
    }
}
