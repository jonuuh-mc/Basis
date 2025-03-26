package io.jonuuh.core.lib.update;

import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.single.StringSetting;
import io.jonuuh.core.lib.util.Log4JLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.Map;

class NotificationPoster
{
    private final String modID;
    private final Settings updateSettings;
    private final Map<String, String> versioningMap;
    private final String latestVersionStr;

    NotificationPoster(String modID, Settings updateSettings, Map<String, String> versioningMap)
    {
        this.modID = modID;
        this.updateSettings = updateSettings;
        this.versioningMap = versioningMap;
        this.latestVersionStr = versioningMap.get("version");

        // if last recorded latest version (written to file via updateSettings) is less than the true latest version,
        // a new update was released so the notif should be posted regardless of time since last post
        if (isNewUpdateAvailable())
        {
            MinecraftForge.EVENT_BUS.register(this);
            return;
        }

        if (updateSettings.get("NOTIFY_AGAIN_FOR_LATEST", BoolSetting.class).getValue())
        {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    private boolean isNewUpdateAvailable()
    {
        Version lastRecordedLatestVersion = new Version(updateSettings.get("LAST_LATEST_VERSION", StringSetting.class).getValue());
        Version trueLatestVersion = new Version(latestVersionStr);

        boolean isNewUpdateAvailable = lastRecordedLatestVersion.compareTo(trueLatestVersion) < 0;

        if (isNewUpdateAvailable)
        {
            updateSettings.get("LAST_LATEST_VERSION", StringSetting.class).setValue(latestVersionStr);
        }

        return isNewUpdateAvailable;
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.entity != Minecraft.getMinecraft().thePlayer)
        {
            return;
        }

        IChatComponent notificationComponent = getNotificationComponent();
        Minecraft.getMinecraft().thePlayer.addChatMessage(notificationComponent);

        // TODO:
//        updateSettings.get("LAST_NOTIFICATION_INSTANT", StringSetting.class).setValue(instantNow.toString());
        updateSettings.save(); // save "LAST_NOTIFICATION_INSTANT" and if applicable, "LAST_LATEST_VERSION"

        MinecraftForge.EVENT_BUS.unregister(this);
    }

    private IChatComponent getNotificationComponent()
    {
        byte[] decodedBytes = Base64.getDecoder().decode(versioningMap.get("changelog"));
        String changelogStr = new String(decodedBytes);

        IChatComponent hoverComponent = new ChatComponentText("[Changelog]");
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(changelogStr));
        ChatStyle chatStyle = new ChatStyle().setChatHoverEvent(hoverEvent).setColor(EnumChatFormatting.BLUE);
        hoverComponent.setChatStyle(chatStyle);

        return new ChatComponentText("First world join of session ").appendSibling(hoverComponent);
    }
}

