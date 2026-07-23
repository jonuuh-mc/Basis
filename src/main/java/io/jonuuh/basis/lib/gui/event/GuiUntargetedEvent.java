package io.jonuuh.basis.lib.gui.event;

public abstract class GuiUntargetedEvent extends GuiEvent
{
    protected GuiUntargetedEvent(boolean cancelable)
    {
        super(cancelable);
    }
}
