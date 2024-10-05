package io.jonuuh.configlib.setting;

public abstract class Setting<T>
{
    protected SettingType type;
    protected T value;

    public abstract T getValue();

    public abstract void setValue(T value);

    public SettingType getType()
    {
        return this.type;
    }
}
