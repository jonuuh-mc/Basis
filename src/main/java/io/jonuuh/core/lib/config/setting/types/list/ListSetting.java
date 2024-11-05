package io.jonuuh.core.lib.config.setting.types.list;

import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.config.setting.types.SettingType;

import java.util.Collection;
import java.util.List;

public abstract class ListSetting<T> extends Setting<List<T>>
{
    protected ListSetting(SettingType type, List<T> defaultValue, List<T> value)
    {
        super(type, defaultValue, value);
    }

    protected ListSetting(SettingType type, List<T> defaultValue)
    {
        super(type, defaultValue);
    }

    protected ListSetting(ListSetting<T> setting)
    {
        super(setting);
    }

    public boolean add(T element)
    {
        return value.add(element);
    }

    public boolean remove(T element)
    {
        return value.remove(element);
    }

    public boolean addAll(Collection<T> elements)
    {
        return value.addAll(elements);
    }

    public void clear()
    {
        value.clear();
    }

    public boolean contains(T element)
    {
        return value.contains(element);
    }

    public int size()
    {
        return value.size();
    }
}
