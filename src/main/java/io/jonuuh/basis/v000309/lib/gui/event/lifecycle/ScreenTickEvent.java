package io.jonuuh.basis.v000309.lib.gui.event.lifecycle;

import io.jonuuh.basis.v000309.lib.gui.element.GuiElement;
import io.jonuuh.basis.v000309.lib.gui.event.GuiEvent;
import io.jonuuh.basis.v000309.lib.gui.listener.lifecycle.ScreenTickListener;

public class ScreenTickEvent extends GuiEvent
{
    @Override
    public void tryDispatchTo(GuiElement element)
    {
        if (element instanceof ScreenTickListener)
        {
            ((ScreenTickListener) element).onScreenTick(this);
        }
    }
}
