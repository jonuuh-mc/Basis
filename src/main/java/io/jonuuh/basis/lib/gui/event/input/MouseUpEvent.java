package io.jonuuh.basis.lib.gui.event.input;

import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.event.GuiTargetedEvent;
import io.jonuuh.basis.lib.gui.listener.input.InputListener;
import io.jonuuh.basis.lib.gui.listener.input.MouseClickListener;

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
        if (element instanceof MouseClickListener)
        {
            if (!((InputListener) element).isEnabled())
            {
                this.stopPropagation();
                return;
            }

            ((MouseClickListener) element).onMouseUp(this);
            lastCapture = element;
        }
    }
}
