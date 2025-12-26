package io.jonuuh.basis.v000309.lib.gui.event.input;

import io.jonuuh.basis.v000309.lib.gui.element.GuiElement;
import io.jonuuh.basis.v000309.lib.gui.event.GuiTargetedEvent;
import io.jonuuh.basis.v000309.lib.gui.listener.input.InputListener;
import io.jonuuh.basis.v000309.lib.gui.listener.input.MouseDragListener;

public class MouseDragEvent extends GuiTargetedEvent
{
    public final int mouseX;
    public final int mouseY;
    public final int clickedMouseButton;
    public final long msHeld;

    public MouseDragEvent(GuiElement target, int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
        super(target);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.clickedMouseButton = clickedMouseButton;
        this.msHeld = msHeld;
    }

    @Override
    public void tryDispatchTo(GuiElement element)
    {
        if (!((InputListener) element).isEnabled())
        {
//            this.stopPropagation();
            return;
        }

        if (element instanceof MouseDragListener)
        {
            ((MouseDragListener) element).onMouseDrag(this);
            lastCapture = element;
        }
    }
}
