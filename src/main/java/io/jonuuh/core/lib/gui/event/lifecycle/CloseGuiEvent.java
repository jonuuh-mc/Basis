package io.jonuuh.core.lib.gui.event.lifecycle;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.event.GuiEvent;
import io.jonuuh.core.lib.gui.listener.lifecycle.CloseGuiListener;

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
