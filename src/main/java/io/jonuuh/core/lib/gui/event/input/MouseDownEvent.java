package io.jonuuh.core.lib.gui.event.input;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.event.GuiTargetedEvent;
import io.jonuuh.core.lib.gui.listener.input.MouseClickListener;

public class MouseDownEvent extends GuiTargetedEvent
{
    public final int mouseX;
    public final int mouseY;

    public MouseDownEvent(GuiElement target, int mouseX, int mouseY)
    {
        super(target);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public void tryDispatchTo(GuiElement element)
    {
        if (element instanceof MouseClickListener)
        {
            ((MouseClickListener) element).onMouseDown(this);
            lastCapture = element;
        }
    }
}
