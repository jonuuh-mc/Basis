package io.jonuuh.core.lib.config.setting.types.list;

import com.google.common.primitives.Booleans;
import io.jonuuh.core.lib.config.setting.types.SettingType;

import java.util.ArrayList;
import java.util.List;

public class BoolListSetting extends ListSetting<Boolean>
{
    public BoolListSetting(List<Boolean> defaultValue, List<Boolean> value)
    {
        super(SettingType.BOOLEAN_LIST, defaultValue, value);
    }

    public BoolListSetting(List<Boolean> defaultValue)
    {
        super(SettingType.BOOLEAN_LIST, defaultValue);
    }

    public BoolListSetting(BoolListSetting setting)
    {
        super(setting);
    }

    public BoolListSetting()
    {
        this(new ArrayList<>());
    }

    @Override
    public BoolListSetting copy()
    {
        return new BoolListSetting(this);
    }

    public boolean[] getValueAsArr()
    {
        return Booleans.toArray(value);
    }

    public void setValueFromArr(boolean[] value)
    {
        this.value = Booleans.asList(value);
    }
}
