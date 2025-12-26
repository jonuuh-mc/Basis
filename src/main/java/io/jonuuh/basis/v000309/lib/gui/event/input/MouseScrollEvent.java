package io.jonuuh.basis.v000309.lib.gui.event.input;

import io.jonuuh.basis.v000309.lib.gui.element.GuiElement;
import io.jonuuh.basis.v000309.lib.gui.event.GuiTargetedEvent;
import io.jonuuh.basis.v000309.lib.gui.listener.input.InputListener;
import io.jonuuh.basis.v000309.lib.gui.listener.input.MouseScrollListener;

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
        if (!((InputListener) element).isEnabled())
        {
//            this.stopPropagation();
            return;
        }

        if (element instanceof MouseScrollListener)
        {
            ((MouseScrollListener) element).onMouseScroll(this);
            lastCapture = element;
        }
    }
}
