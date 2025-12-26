package io.jonuuh.basis.v000309.lib.gui.event;

import io.jonuuh.basis.v000309.lib.gui.element.GuiElement;

public abstract class GuiTargetedEvent extends GuiEvent
{
    public final GuiElement target;
    protected GuiElement lastCapture;
    private boolean propagationStopped;

    protected GuiTargetedEvent(GuiElement target)
    {
        this.target = target;
        this.propagationStopped = false;
    }

    public GuiElement getLastCapture()
    {
        return lastCapture;
    }

    public void stopPropagation()
    {
        propagationStopped = true;
    }

    public boolean hasPropagationStopped()
    {
        return propagationStopped;
    }
}
