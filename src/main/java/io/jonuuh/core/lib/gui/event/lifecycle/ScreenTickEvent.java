package io.jonuuh.core.lib.gui.event.lifecycle;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.event.GuiEvent;
import io.jonuuh.core.lib.gui.listener.lifecycle.ScreenTickListener;

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
