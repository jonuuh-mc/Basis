package io.jonuuh.core.lib.gui.event;

public class KeyInputEvent extends GuiEvent
{
    public final char typedChar;
    public final int keyCode;

    public KeyInputEvent(char typedChar, int keyCode)
    {
        this.typedChar = typedChar;
        this.keyCode = keyCode;
    }
}
