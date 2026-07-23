package io.jonuuh.basis.lib.gui.event.input;

import io.jonuuh.basis.lib.gui.element.GuiElement;

public class MouseDragEvent extends InputEvent
{
    public final int mouseX;
    public final int mouseY;
    public final int clickedMouseButton;
    public final long msHeld;

    public MouseDragEvent(GuiElement target, boolean bubbles, boolean cancelable, int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
        super(target, bubbles, cancelable);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.clickedMouseButton = clickedMouseButton;
        this.msHeld = msHeld;
    }
}
