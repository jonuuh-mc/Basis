package io.jonuuh.core.module.config.setting.types;

import io.jonuuh.core.module.config.setting.Setting;
import io.jonuuh.core.module.config.setting.SettingType;

import java.util.Arrays;

public class StringListSetting extends Setting<String[]>
{
    public StringListSetting(String[] defaultValue)
    {
        super(SettingType.STRING_LIST, defaultValue);
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
