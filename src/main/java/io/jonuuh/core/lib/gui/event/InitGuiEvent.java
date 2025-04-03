package io.jonuuh.core.lib.gui.event;

import net.minecraft.client.gui.ScaledResolution;

public class InitGuiEvent extends GuiEvent
{
    public final ScaledResolution scaledResolution;

    public InitGuiEvent(ScaledResolution scaledResolution)
    {
        this.scaledResolution = scaledResolution;
    }
}
