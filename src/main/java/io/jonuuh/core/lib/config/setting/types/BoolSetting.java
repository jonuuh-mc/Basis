package io.jonuuh.core.lib.config.setting.types;

import io.jonuuh.core.lib.config.setting.Setting;
import io.jonuuh.core.lib.config.setting.SettingType;

public class BoolSetting extends Setting<Boolean>
{
    public BoolSetting(boolean defaultValue, boolean value)
    {
        super(SettingType.BOOLEAN, defaultValue, value);
    }

    public BoolSetting(boolean defaultValue)
    {
        super(SettingType.BOOLEAN, defaultValue);
    }

    public BoolSetting(BoolSetting setting)
    {
        super(setting);
    }

    public BoolSetting()
    {
        this(false);
    }
}
