package io.jonuuh.basis.lib.gui.event.input;

import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.event.GuiTargetedEvent;

public abstract class InputEvent extends GuiTargetedEvent
{
    protected InputEvent(GuiElement target, boolean bubbles, boolean cancelable)
    {
        super(target, bubbles, cancelable);
    }
}
