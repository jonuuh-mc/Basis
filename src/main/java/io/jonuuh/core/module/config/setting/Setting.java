package io.jonuuh.core.module.config.setting;

public abstract class Setting<T>
{
    protected final SettingType type;
    protected T defaultValue;
    protected T value;

    protected Setting(SettingType type, T defaultValue)
    {
        this.type = type;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public SettingType getType()
    {
        return type;
    }

    public T getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(T defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }

    public void reset()
    {
        this.value = defaultValue;
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "value=" + value + '}';
    }
}
