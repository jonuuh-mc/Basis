package io.jonuuh.core.lib.update;

import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.StringSetting;
import io.jonuuh.core.lib.util.Log4JLogger;
import io.jonuuh.core.lib.util.StaticFileUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;

class NotificationPoster
{
    private final String modID;
    private final Settings updateSettings;
    private final String latestVersionStr;
    private final Instant instantNow;

    NotificationPoster(String modID, Settings updateSettings, String latestVersionStr)
    {
        this.modID = modID;
        this.updateSettings = updateSettings;
        this.latestVersionStr = latestVersionStr;
        this.instantNow = Instant.now();

        // if last recorded latest version (written to file via updateSettings) is less than the true latest version,
        // a new update was released so the notif should be posted regardless of time since last post
        if (isNewUpdateAvailable())
        {
            MinecraftForge.EVENT_BUS.register(this);
            return;
        }

        Instant lastNotifInstant = getLastNotifInstant();

        // if lastNotifInstant is null, it should be the first ever login (or instant cfg property was corrupted)
        if (lastNotifInstant == null)
        {
            MinecraftForge.EVENT_BUS.register(this);
            return;
        }

        Log4JLogger.INSTANCE.info("durationSinceLastNotif: " + Duration.between(lastNotifInstant, instantNow).toString());
        Log4JLogger.INSTANCE.info("daysSinceLastNotif: " + Duration.between(lastNotifInstant, instantNow).toDays());

        if (Duration.between(lastNotifInstant, instantNow).toDays() >= 7 && !updateSettings.getBoolSetting("DISABLE_REMINDING_FOR_LATEST").getValue())
        {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    private boolean isNewUpdateAvailable()
    {
        Version lastRecordedLatestVersion = new Version(updateSettings.getStringSetting("LAST_LATEST_VERSION").getValue());
        Version trueLatestVersion = new Version(latestVersionStr);

        boolean isNewUpdateAvailable = lastRecordedLatestVersion.compareTo(trueLatestVersion) < 0;

        if (isNewUpdateAvailable)
        {
            updateSettings.getStringSetting("LAST_LATEST_VERSION").setValue(latestVersionStr);
        }

        return isNewUpdateAvailable;
    }

    /**
     * Try to parse the instant from the setting after loading its property from the mod's .cfg file.
     *
     * @return If the property existed, return whatever instant was in the file (actual last notif instant).
     * Else return null if the property couldn't be parsed (didn't exist - never posted a notification before, or was corrupted)
     */
    private Instant getLastNotifInstant()
    {
        try
        {
            return Instant.parse(updateSettings.getStringSetting("LAST_NOTIFICATION_INSTANT").getValue());
        }
        catch (DateTimeParseException e)
        {
            Log4JLogger.INSTANCE.warn("Returning null last notif instant");
            return null;
        }
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

        updateSettings.getStringSetting("LAST_NOTIFICATION_INSTANT").setValue(instantNow.toString());
        updateSettings.save(); // save "LAST_NOTIFICATION_INSTANT" and if applicable, "LAST_LATEST_VERSION"

        MinecraftForge.EVENT_BUS.unregister(this);
    }

    private IChatComponent getNotificationComponent()
    {
        String changelogStr = StaticFileUtils.getStaticHostedAsset("versioning/" + modID + "/latest-changelog.txt");

        IChatComponent hoverComponent = new ChatComponentText("[Changelog]");
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(changelogStr));
        ChatStyle chatStyle = new ChatStyle().setChatHoverEvent(hoverEvent).setColor(EnumChatFormatting.BLUE);
        hoverComponent.setChatStyle(chatStyle);

        return new ChatComponentText("First world join of session ").appendSibling(hoverComponent);
    }
}

