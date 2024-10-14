package io.jonuuh.core.module.config.setting.types;

import io.jonuuh.core.module.config.setting.Setting;
import io.jonuuh.core.module.config.setting.SettingType;

public class StringSetting extends Setting<String>
{
    public StringSetting(String defaultValue)
    {
        super(SettingType.STRING, defaultValue);
    }

    public StringSetting()
    {
        this("");
    }
}
