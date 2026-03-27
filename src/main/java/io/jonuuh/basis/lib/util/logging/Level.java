package io.jonuuh.basis.lib.util.logging;

public enum Level
{
    NONE(0),
    ERROR(1),
    INFO(2),
    DEBUG(3);

    public final int intLevel;

    Level(int intLevel)
    {
        this.intLevel = intLevel;
    }
}
