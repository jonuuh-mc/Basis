package io.jonuuh.core.lib.config.setting.types;

public abstract class Setting<T>
{
    protected T defaultValue;
    protected T currentValue;

    protected Setting(T defaultValue, T currentValue)
    {
        this.defaultValue = defaultValue;
        this.currentValue = currentValue;
    }

    protected Setting(T defaultValue)
    {
        this(defaultValue, defaultValue);
    }

    protected Setting(Setting<T> setting)
    {
        this(setting.defaultValue, setting.currentValue);
    }

    public abstract Setting<T> copy();

    public T getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(T defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public T getCurrentValue()
    {
        return currentValue;
    }

    public void setCurrentValue(T currentValue)
    {
        this.currentValue = currentValue;
    }

    /**
     * Set the current value back to the default value
     */
    public void reset()
    {
        this.currentValue = defaultValue;
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "currentValue=" + currentValue + '}';
    }
}
