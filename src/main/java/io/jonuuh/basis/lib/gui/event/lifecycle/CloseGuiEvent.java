package io.jonuuh.basis.lib.gui.event.lifecycle;

import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.event.GuiEvent;
import io.jonuuh.basis.lib.gui.listener.lifecycle.CloseGuiListener;

public class CloseGuiEvent extends GuiEvent
{
    @Override
    public void tryDispatchTo(GuiElement element)
    {
        if (element instanceof CloseGuiListener)
        {
            ((CloseGuiListener) element).onCloseGui(this);
        }
    }
}
