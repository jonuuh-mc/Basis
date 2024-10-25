package io.jonuuh.core.lib.config.setting.types;

import io.jonuuh.core.lib.config.setting.Setting;
import io.jonuuh.core.lib.config.setting.SettingType;

public class StringSetting extends Setting<String>
{
    public StringSetting(String defaultValue, String value)
    {
        super(SettingType.STRING, defaultValue, value);
    }

    public StringSetting(String defaultValue)
    {
        super(SettingType.STRING, defaultValue);
    }

    public StringSetting(StringSetting setting)
    {
        super(setting);
    }

    public StringSetting()
    {
        this("");
    }
}
