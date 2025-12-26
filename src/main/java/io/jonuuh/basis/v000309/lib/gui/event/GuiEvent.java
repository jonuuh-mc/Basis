package io.jonuuh.basis.v000309.lib.gui.event;

import io.jonuuh.basis.v000309.lib.gui.element.GuiElement;

public abstract class GuiEvent
{
    public abstract void tryDispatchTo(GuiElement element);
}
