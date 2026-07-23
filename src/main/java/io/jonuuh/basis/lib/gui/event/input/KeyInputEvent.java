package io.jonuuh.basis.lib.gui.event.input;

import io.jonuuh.basis.lib.gui.element.GuiElement;

public class KeyInputEvent extends InputEvent
{
    public final char typedChar;
    public final int keyCode;

    public KeyInputEvent(GuiElement target, boolean bubbles, boolean cancelable, char typedChar, int keyCode)
    {
        super(target, bubbles, cancelable);
        this.typedChar = typedChar;
        this.keyCode = keyCode;
    }
}
