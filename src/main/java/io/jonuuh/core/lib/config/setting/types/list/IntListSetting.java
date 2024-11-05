package io.jonuuh.core.lib.config.setting.types.list;

import com.google.common.primitives.Ints;
import io.jonuuh.core.lib.config.setting.types.SettingType;

import java.util.ArrayList;
import java.util.List;

public class IntListSetting extends ListSetting<Integer>
{
    public IntListSetting(List<Integer> defaultValue, List<Integer> value)
    {
        super(SettingType.INTEGER_LIST, defaultValue, value);
    }

    public IntListSetting(List<Integer> defaultValue)
    {
        super(SettingType.INTEGER_LIST, defaultValue);
    }

    public IntListSetting(IntListSetting setting)
    {
        super(setting);
    }

    public IntListSetting()
    {
        this(new ArrayList<>());
    }

    @Override
    public IntListSetting copy()
    {
        return new IntListSetting(this);
    }

    public int[] getValueAsArr()
    {
        return Ints.toArray(value);
    }

    public void setValueFromArr(int[] value)
    {
        this.value = Ints.asList(value);
    }
}
