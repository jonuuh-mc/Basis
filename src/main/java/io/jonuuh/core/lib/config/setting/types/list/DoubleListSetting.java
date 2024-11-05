package io.jonuuh.core.lib.config.setting.types.list;

import com.google.common.primitives.Doubles;
import io.jonuuh.core.lib.config.setting.types.SettingType;

import java.util.ArrayList;
import java.util.List;

public class DoubleListSetting extends ListSetting<Double>
{
    public DoubleListSetting(List<Double> defaultValue, List<Double> value)
    {
        super(SettingType.DOUBLE_LIST, defaultValue, value);
    }

    public DoubleListSetting(List<Double> defaultValue)
    {
        super(SettingType.DOUBLE_LIST, defaultValue);
    }

    public DoubleListSetting(DoubleListSetting setting)
    {
        super(setting);
    }

    public DoubleListSetting()
    {
        this(new ArrayList<>());
    }

    @Override
    public DoubleListSetting copy()
    {
        return new DoubleListSetting(this);
    }

    public double[] getValueAsArr()
    {
        return Doubles.toArray(value);
    }

    public void setValueFromArr(double[] value)
    {
        this.value = Doubles.asList(value);
    }
}
