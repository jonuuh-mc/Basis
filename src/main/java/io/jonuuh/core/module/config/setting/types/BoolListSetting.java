package io.jonuuh.core.module.config.setting.types;

import io.jonuuh.core.module.config.setting.Setting;
import io.jonuuh.core.module.config.setting.SettingType;

import java.util.Arrays;

public class BoolListSetting extends Setting<boolean[]>
{
    public BoolListSetting(boolean[] defaultValue)
    {
        super(SettingType.BOOLEAN_LIST, defaultValue);
    }

    public BoolListSetting()
    {
        this(new boolean[]{});
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "value=" + Arrays.toString(value) + '}';
    }
}
