package io.jonuuh.basis.lib.gui.event.input;

import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.event.GuiTargetedEvent;
import io.jonuuh.basis.lib.gui.listener.input.InputListener;
import io.jonuuh.basis.lib.gui.listener.input.KeyInputListener;

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
        if (!((InputListener) element).isEnabled())
        {
//            this.stopPropagation();
            return;
        }

        if (element instanceof KeyInputListener)
        {
            ((KeyInputListener) element).onKeyTyped(this);
            lastCapture = element;
        }
    }
}
