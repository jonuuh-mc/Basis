package io.jonuuh.basis.v000309.lib.gui.event.input;

import io.jonuuh.basis.v000309.lib.gui.element.GuiElement;
import io.jonuuh.basis.v000309.lib.gui.event.GuiTargetedEvent;
import io.jonuuh.basis.v000309.lib.gui.listener.input.InputListener;
import io.jonuuh.basis.v000309.lib.gui.listener.input.MouseClickListener;

public class MouseUpEvent extends GuiTargetedEvent
{
    public final int mouseX;
    public final int mouseY;

    public MouseUpEvent(GuiElement target, int mouseX, int mouseY)
    {
        super(target);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public void tryDispatchTo(GuiElement element)
    {
        if (!((InputListener) element).isEnabled())
        {
//            this.stopPropagation();
            return;
        }

        if (element instanceof MouseClickListener)
        {
            ((MouseClickListener) element).onMouseUp(this);
            lastCapture = element;
        }
    }
}
