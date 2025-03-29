package io.jonuuh.core.lib.util;

import net.minecraftforge.common.MinecraftForge;

public class StaticAssetRequester implements Runnable
{
    private final String assetPath;

    public StaticAssetRequester(String assetPath)
    {
        this.assetPath = assetPath;
    }

    @Override
    public void run()
    {
        String asset = StaticAssetUtils.getStaticHostedRawAsset(assetPath);
        MinecraftForge.EVENT_BUS.post(new StaticAssetRequestFinishedEvent(asset));
    }
}
