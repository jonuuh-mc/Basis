package io.jonuuh.basis.lib.gui.event.lifecycle;

import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.event.GuiEvent;
import io.jonuuh.basis.lib.gui.listener.lifecycle.InitGuiListener;
import net.minecraft.client.gui.ScaledResolution;

public class InitGuiEvent extends GuiEvent
{
    public final ScaledResolution sr;

    public InitGuiEvent(ScaledResolution sr)
    {
        this.sr = sr;
    }

    @Override
    public void tryDispatchTo(GuiElement element)
    {
        if (element instanceof InitGuiListener)
        {
            ((InitGuiListener) element).onInitGui(this);
        }
    }
}
