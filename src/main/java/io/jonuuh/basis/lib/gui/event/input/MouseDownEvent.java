package io.jonuuh.basis.lib.gui.event.input;

import io.jonuuh.basis.lib.gui.element.GuiElement;

public class MouseDownEvent extends InputEvent
{
    public final int mouseX;
    public final int mouseY;

    public MouseDownEvent(GuiElement target, boolean bubbles, boolean cancelable, int mouseX, int mouseY)
    {
        super(target, bubbles, cancelable);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
}
