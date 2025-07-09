package io.jonuuh.basis.lib.gui.event.input;

import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.event.GuiTargetedEvent;
import io.jonuuh.basis.lib.gui.listener.input.InputListener;
import io.jonuuh.basis.lib.gui.listener.input.MouseScrollListener;

public class MouseScrollEvent extends GuiTargetedEvent
{
    public final int wheelDelta;

    public MouseScrollEvent(GuiElement target, int wheelDelta)
    {
        super(target);
        this.wheelDelta = wheelDelta;
    }

    @Override
    public void tryDispatchTo(GuiElement element)
    {
        if (element instanceof MouseScrollListener)
        {
            if (!((InputListener) element).isEnabled())
            {
                this.stopPropagation();
            }

            ((MouseScrollListener) element).onMouseScroll(this);
            lastCapture = element;
        }
    }
}
