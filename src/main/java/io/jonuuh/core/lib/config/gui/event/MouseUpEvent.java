package io.jonuuh.core.lib.config.gui.event;

public class MouseUpEvent extends GuiEvent
{
    public final int mouseX;
    public final int mouseY;
    public final int mouseEventButton;

    public MouseUpEvent(int mouseX, int mouseY, int mouseEventButton)
    {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.mouseEventButton = mouseEventButton;
    }
}
