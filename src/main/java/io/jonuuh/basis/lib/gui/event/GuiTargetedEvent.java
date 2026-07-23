package io.jonuuh.basis.lib.gui.event;

import io.jonuuh.basis.lib.gui.element.GuiElement;

public abstract class GuiTargetedEvent extends GuiEvent
{
    public final GuiElement target;
    private final boolean bubbles;

    protected GuiTargetedEvent(GuiElement target, boolean bubbles, boolean cancelable)
    {
        super(cancelable);
        this.target = target;
        this.bubbles = bubbles;
    }

    public boolean bubbles()
    {
        return bubbles;
    }
}
