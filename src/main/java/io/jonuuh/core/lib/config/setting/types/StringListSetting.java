package io.jonuuh.core.lib.config.setting.types;

import io.jonuuh.core.lib.config.setting.Setting;
import io.jonuuh.core.lib.config.setting.SettingType;

import java.util.Arrays;

public class StringListSetting extends Setting<String[]>
{
    public StringListSetting(String[] defaultValue, String[] value)
    {
        super(SettingType.STRING_LIST, defaultValue, value);
    }

    public StringListSetting(String[] defaultValue)
    {
        super(SettingType.STRING_LIST, defaultValue);
    }

    public StringListSetting(StringListSetting setting)
    {
        super(setting);
    }

    public StringListSetting()
    {
        this(new String[]{});
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "value=" + Arrays.toString(value) + '}';
    }
}
