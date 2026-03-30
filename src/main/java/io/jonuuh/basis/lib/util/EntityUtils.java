package io.jonuuh.basis.lib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import java.util.UUID;

public final class EntityUtils
{
    private static final Minecraft mc = Minecraft.getMinecraft();

    /** Prevents instantiation */
    private EntityUtils()
    {
    }

    public static NetworkPlayerInfo getPlayerInfo(EntityPlayer player)
    {
        UUID uuid = player.getUniqueID();
        return mc.getNetHandler().getPlayerInfo(uuid);
    }

    public static NetworkPlayerInfo getClientInfo()
    {
        return getPlayerInfo(mc.thePlayer);
    }

    public static boolean isClientNicked()
    {
        String clientName = mc.thePlayer.getName();

        // When nicked, client UUID will be mapped in the netHandler to a 'fake' NetworkPlayerInfo object.
        // The 'fake' NetworkPlayerInfo contains the nickname, team w/ formatting codes, etc
        NetworkPlayerInfo clientInfo = getClientInfo();

        if (clientInfo == null || clientInfo.getGameProfile() == null)
        {
            return false;
        }

        // If Entity's name != the NetworkPlayerInfo's name, the player is nicked
        return !clientName.equals(clientInfo.getGameProfile().getName());
    }

    public static ResourceLocation getPlayerSkin(String playerName)
    {
        NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(playerName);
        return getPlayerSkin(info);
    }

    public static ResourceLocation getPlayerSkin(EntityPlayer player)
    {
        return getPlayerSkin(getPlayerInfo(player));
    }

    public static ResourceLocation getPlayerSkin(NetworkPlayerInfo playerInfo)
    {
        return playerInfo != null ? playerInfo.getLocationSkin() : new ResourceLocation("missingno");
    }

    public static Vec3 getEntityPosForRender(EntityPlayer player, float partialTicks)
    {
        RenderManager renderManager = mc.getRenderManager();
        if (player == mc.thePlayer && renderManager.livingPlayer == mc.thePlayer)
        {
            // viewer pos (usually client player pos) is cached by render manager during each render pass
            return new Vec3(renderManager.viewerPosX, renderManager.viewerPosY, renderManager.viewerPosZ);
        }

        double x = (player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks);
        double y = (player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks);
        double z = (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks);
        return new Vec3(x, y, z);
    }
}
