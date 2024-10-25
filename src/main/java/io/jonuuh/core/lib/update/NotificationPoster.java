package io.jonuuh.core.lib.update;

import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.StringSetting;
import io.jonuuh.core.lib.util.ModLogger;
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

        if (isNewUpdateAvailable())
        {
            MinecraftForge.EVENT_BUS.register(this);
            return;
        }

        Instant lastNotifInstant = getLastNotifInstant();

        // if instant now is the same as the instant parsed from the setting, it should be the first ever login (or instant cfg property was corrupted)
        // (loading the instant property from the cfg file into the setting resulted in the exact same instant, therefore the property did not exist in the cfg file)
        if (instantNow.equals(lastNotifInstant))
        {
//            updateSettings.save("LAST_UPDATE_NOTIFICATION_INSTANT"); // TODO: last in progress right here
            MinecraftForge.EVENT_BUS.register(this);
            return;
        }

        ModLogger.INSTANCE.info("durationSinceLastNotif: " + Duration.between(lastNotifInstant, instantNow).toString());
        ModLogger.INSTANCE.info("daysSinceLastNotif: " + Duration.between(lastNotifInstant, instantNow).toDays());

        if (Duration.between(lastNotifInstant, instantNow).toDays() >= 7 && !updateSettings.getBoolSetting("DISABLE_REMINDING_FOR_LATEST").getValue())
        {
//            instantSetting.setValue(instantNow.toString());
//            updateSettings.save("LAST_UPDATE_NOTIFICATION_INSTANT");
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    private boolean isNewUpdateAvailable()
    {
        StringSetting lastRecordedLatestVersion = updateSettings.getStringSetting("LAST_LATEST_VERSION");

        if (lastRecordedLatestVersion.getValue().isEmpty())
        {
            return false;
        }

        // last recorded latest version is less than the true latest version
        boolean isNewUpdateAvailable = new Version(lastRecordedLatestVersion.getValue()).compareTo(new Version(latestVersionStr)) > 0;

        if (isNewUpdateAvailable)
        {
            updateSettings.getStringSetting("LAST_LATEST_VERSION").setValue(latestVersionStr);
            updateSettings.save("LAST_LATEST_VERSION");
        }

        return isNewUpdateAvailable;
    }

    /**
     * Try to parse the instant from the setting after loading its property from the mod's .cfg file.
     *
     * @return If the property existed, return whatever instant was in the file (actual last notif instant).
     * Else return instantNow if the property couldn't be parsed (didn't exist - never posted a notification before, or was corrupted)
     */
    private Instant getLastNotifInstant()
    {
        try
        {
            return Instant.parse(updateSettings.getStringSetting("LAST_NOTIFICATION_INSTANT").getValue());
        }
        catch (DateTimeParseException e)
        {
            ModLogger.INSTANCE.warn("Returning default last notif instant {}", instantNow);
//            instantSetting.setValue(instantNow.toString()); // set the value but don't save yet until entityJoinWorld event
            return instantNow;
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
        updateSettings.save("LAST_NOTIFICATION_INSTANT");

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

