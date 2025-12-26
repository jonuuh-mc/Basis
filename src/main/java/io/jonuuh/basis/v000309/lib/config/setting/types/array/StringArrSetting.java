package io.jonuuh.basis.v000309.lib.config.setting.types.array;

import io.jonuuh.basis.v000309.lib.config.setting.types.Setting;

import java.util.Arrays;

public final class StringArrSetting extends Setting<String[]>
{
    public StringArrSetting(String[] defaultValue, String[] currentValue)
    {
        super(defaultValue, currentValue);
    }

    public StringArrSetting(String[] defaultValue)
    {
        super(defaultValue);
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
        return this.getClass().getSimpleName() + "{" + "currentValue=" + Arrays.toString(currentValue) + '}';
    }
}
