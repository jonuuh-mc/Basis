package io.jonuuh.basis.lib.gui.event.input;

import io.jonuuh.basis.lib.gui.element.GuiElement;

public class MouseScrollEvent extends InputEvent
{
    public final int wheelDelta;

    public MouseScrollEvent(GuiElement target, boolean bubbles, boolean cancelable, int wheelDelta)
    {
        super(target, bubbles, cancelable);
        this.wheelDelta = wheelDelta;
    }
}
