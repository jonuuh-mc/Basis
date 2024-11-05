package io.jonuuh.core.lib.config.setting.types.list;

import io.jonuuh.core.lib.config.setting.types.SettingType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringListSetting extends ListSetting<String>
{
    public StringListSetting(List<String> defaultValue, List<String> value)
    {
        super(SettingType.STRING_LIST, defaultValue, value);
    }

    public StringListSetting(List<String> defaultValue)
    {
        super(SettingType.STRING_LIST, defaultValue);
    }

    public StringListSetting(StringListSetting setting)
    {
        super(setting);
    }

    public StringListSetting()
    {
        this(new ArrayList<>());
    }

    @Override
    public StringListSetting copy()
    {
        return new StringListSetting(this);
    }

    public String[] getValueAsArr()
    {
        return value.toArray(new String[0]);
    }

    public void setValueFromArr(String[] value)
    {
        this.value = Arrays.asList(value);
    }
}
