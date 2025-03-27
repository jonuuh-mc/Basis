package io.jonuuh.core.lib.gui.event;

public class MouseDragEvent extends GuiEvent
{
    public final int mouseX;
    public final int mouseY;
    public final int clickedMouseButton;
    public final long msHeld;

    public MouseDragEvent(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.clickedMouseButton = clickedMouseButton;
        this.msHeld = msHeld;
    }
}
