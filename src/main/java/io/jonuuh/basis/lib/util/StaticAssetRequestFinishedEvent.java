package io.jonuuh.basis.lib.util;

import net.minecraftforge.fml.common.eventhandler.Event;

public class StaticAssetRequestFinishedEvent extends Event
{
    public final String asset;

    public StaticAssetRequestFinishedEvent(String asset)
    {
        this.asset = asset;
    }
}
