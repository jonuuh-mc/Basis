package io.jonuuh.basis.lib.gui.event.lifecycle;

import io.jonuuh.basis.lib.gui.event.GuiUntargetedEvent;

public class CloseGuiEvent extends GuiUntargetedEvent
{
    public CloseGuiEvent(boolean cancelable)
    {
        super(cancelable);
    }
}
