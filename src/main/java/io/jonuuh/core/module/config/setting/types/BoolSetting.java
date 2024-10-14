package io.jonuuh.core.module.config.setting.types;

import io.jonuuh.core.module.config.setting.Setting;
import io.jonuuh.core.module.config.setting.SettingType;

public class BoolSetting extends Setting<Boolean>
{
    public BoolSetting(boolean defaultValue)
    {
        super(SettingType.BOOLEAN, defaultValue);
    }

    public BoolSetting()
    {
        this(false);
    }
}
