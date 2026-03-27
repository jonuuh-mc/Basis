package io.jonuuh.basis.lib.gui.event;

import io.jonuuh.basis.lib.gui.element.GuiElement;

public abstract class GuiEvent
{
    public abstract void tryDispatchTo(GuiElement element);
}
