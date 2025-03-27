package io.jonuuh.core.lib.gui.event;

public class ScreenDrawEvent extends GuiEvent
{
    public final int mouseX;
    public final int mouseY;
    public final float partialTicks;

    public ScreenDrawEvent(int mouseX, int mouseY, float partialTicks)
    {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.partialTicks = partialTicks;
    }
}
