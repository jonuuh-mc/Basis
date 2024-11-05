package io.jonuuh.core.lib.config.setting.types.single;

import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.config.setting.types.SettingType;

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

    @Override
    public BoolSetting copy()
    {
        return new BoolSetting(this);
    }
}
