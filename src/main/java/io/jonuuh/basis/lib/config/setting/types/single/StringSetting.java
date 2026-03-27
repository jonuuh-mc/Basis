package io.jonuuh.basis.lib.config.setting.types.single;

import io.jonuuh.basis.lib.config.setting.types.Setting;

public final class StringSetting extends Setting<String>
{
    public StringSetting(String defaultValue, String currentValue)
    {
        super(defaultValue, currentValue);
    }

    public StringSetting(String defaultValue)
    {
        super(defaultValue);
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
