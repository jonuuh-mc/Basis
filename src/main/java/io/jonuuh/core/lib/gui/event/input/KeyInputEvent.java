package io.jonuuh.core.lib.gui.event.input;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.event.GuiTargetedEvent;
import io.jonuuh.core.lib.gui.listener.input.KeyInputListener;

public class KeyInputEvent extends GuiTargetedEvent
{
    public final char typedChar;
    public final int keyCode;

    public KeyInputEvent(GuiElement target, char typedChar, int keyCode)
    {
        super(target);
        this.typedChar = typedChar;
        this.keyCode = keyCode;
    }

    @Override
    public void tryDispatchTo(GuiElement element)
    {
        if (element instanceof KeyInputListener)
        {
            ((KeyInputListener) element).onKeyTyped(this);
            lastCapture = element;
        }
    }
}
