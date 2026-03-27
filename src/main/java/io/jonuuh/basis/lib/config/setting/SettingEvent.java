package io.jonuuh.basis.lib.config.setting;

import io.jonuuh.basis.lib.config.setting.types.Setting;
import net.minecraftforge.fml.common.eventhandler.Event;

// TODO: this class probably should be removed, was just for experimentation
public final class SettingEvent<T> extends Event
{
    public final Setting<T> setting;
    public final Type type;
    public final T lastValue;
    public final T newValue;

    public SettingEvent(Setting<T> setting, Type type, T lastValue, T newValue)
    {
        this.setting = setting;
        this.type = type;
        this.lastValue = lastValue;
        this.newValue = newValue;
    }

    public enum Type
    {
        SET_DEFAULT, SET_CURRENT
    }
}
