package io.jonuuh.core.lib.config.setting.types.array;

import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.config.setting.types.SettingType;

import java.util.Arrays;

public class BoolArrSetting extends Setting<boolean[]>
{
    public BoolArrSetting(boolean[] defaultValue, boolean[] value)
    {
        super(SettingType.BOOLEAN_ARRAY, defaultValue, value);
    }

    public BoolArrSetting(boolean[] defaultValue)
    {
        super(SettingType.BOOLEAN_ARRAY, defaultValue);
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
        return this.getClass().getSimpleName() + "{" + "value=" + Arrays.toString(value) + '}';
    }
}
