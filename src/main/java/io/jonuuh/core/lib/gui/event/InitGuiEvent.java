package io.jonuuh.core.lib.gui.event;

public class InitGuiEvent extends GuiEvent
{
    public final int guiScreenWidth;
    public final int guiScreenHeight;

    public InitGuiEvent(int guiScreenWidth, int guiScreenHeight)
    {
        this.guiScreenWidth = guiScreenWidth;
        this.guiScreenHeight = guiScreenHeight;
    }
}
