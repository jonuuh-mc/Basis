package io.jonuuh.basis.v000309.lib.config.setting.types;

import io.jonuuh.basis.v000309.lib.config.setting.SettingEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Some persistent piece of data for a mod.
 * <p>
 * Has two values: default and current, both of which can be made persistent.
 * <p>
 * The current value should be the data 'actively' used in the mod, and the default value should be
 * what the current value is set to if the Setting is reset.
 * <p>
 * Supported types by subclasses: boolean, double, integer, string, boolean array, double array, integer array, string array.
 * (probably should not be extended any further; forge Configurations only have get methods for these 8 types)
 *
 * @param <T> The object type stored as this Setting's values (e.g. Boolean for BoolSetting).
 * @see io.jonuuh.basis.lib.config.setting.Settings
 * @see io.jonuuh.basis.lib.config.SettingsConfigurationAdapter
 */
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
        MinecraftForge.EVENT_BUS.post(new SettingEvent<>(this, SettingEvent.Type.SET_DEFAULT, this.defaultValue, defaultValue));
    }

    public T getCurrentValue()
    {
        return currentValue;
    }

    public void setCurrentValue(T currentValue)
    {
        this.currentValue = currentValue;
        MinecraftForge.EVENT_BUS.post(new SettingEvent<>(this, SettingEvent.Type.SET_CURRENT, this.currentValue, currentValue));
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
