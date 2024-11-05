package io.jonuuh.core.lib.config.setting.types.array;

import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.config.setting.types.SettingType;

import java.util.Arrays;

public class StringArrSetting extends Setting<String[]>
{
    public StringArrSetting(String[] defaultValue, String[] value)
    {
        super(SettingType.STRING_ARRAY, defaultValue, value);
    }

    public StringArrSetting(String[] defaultValue)
    {
        super(SettingType.STRING_ARRAY, defaultValue);
    }

    public StringArrSetting(StringArrSetting setting)
    {
        super(setting);
    }

    public StringArrSetting()
    {
        this(new String[]{});
    }

    @Override
    public StringArrSetting copy()
    {
        return new StringArrSetting(this);
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "value=" + Arrays.toString(value) + '}';
    }
}
