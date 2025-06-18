package io.jonuuh.core.lib.gui.event;

import io.jonuuh.core.lib.gui.element.GuiElement;

public abstract class GuiEvent
{
    public abstract void tryDispatchTo(GuiElement element);
}
