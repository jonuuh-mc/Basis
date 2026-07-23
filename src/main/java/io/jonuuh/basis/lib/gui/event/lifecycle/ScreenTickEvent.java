package io.jonuuh.basis.lib.gui.event.lifecycle;

import io.jonuuh.basis.lib.gui.event.GuiUntargetedEvent;

public class ScreenTickEvent extends GuiUntargetedEvent
{
    public ScreenTickEvent(boolean cancelable)
    {
        super(cancelable);
    }
}
