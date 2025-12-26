package io.jonuuh.basis.v000309.lib.gui.event.lifecycle;

import io.jonuuh.basis.v000309.lib.gui.element.GuiElement;
import io.jonuuh.basis.v000309.lib.gui.event.GuiEvent;
import io.jonuuh.basis.v000309.lib.gui.listener.lifecycle.CloseGuiListener;

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
