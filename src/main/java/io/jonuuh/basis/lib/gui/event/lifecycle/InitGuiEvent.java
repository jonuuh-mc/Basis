package io.jonuuh.basis.lib.gui.event.lifecycle;

import io.jonuuh.basis.lib.gui.event.GuiUntargetedEvent;
import net.minecraft.client.gui.ScaledResolution;

public class InitGuiEvent extends GuiUntargetedEvent
{
    public final ScaledResolution sr;

    public InitGuiEvent(boolean cancelable, ScaledResolution sr)
    {
        super(cancelable);
        this.sr = sr;
    }
}
