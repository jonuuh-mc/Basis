package io.jonuuh.core.lib.config.setting.types.single;

import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.config.setting.types.SettingType;

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

    @Override
    public StringSetting copy()
    {
        return new StringSetting(this);
    }
}
