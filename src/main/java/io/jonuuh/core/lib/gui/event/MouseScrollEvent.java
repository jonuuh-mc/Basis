package io.jonuuh.core.lib.gui.event;

public class MouseScrollEvent extends GuiEvent
{
    public final int wheelDelta;

    public MouseScrollEvent(int wheelDelta)
    {
        this.wheelDelta = wheelDelta;
    }
}
