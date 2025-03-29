package io.jonuuh.core.lib.update;

import com.google.gson.JsonObject;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.single.StringSetting;
import io.jonuuh.core.lib.util.ChatLogger;
import io.jonuuh.core.lib.util.StaticAssetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Base64;

final class NotificationPoster
{
    private final String modID;
    private final String modName;
    private final Settings updateSettings;
    private final JsonObject jsonObject;
    private final String trueLatestVersionStr;
    private final StringSetting lastLatestVersionSetting;
    private final BoolSetting repeatNotifySetting;

    NotificationPoster(String modID, String modName, Settings updateSettings, JsonObject jsonObject, String latestVersionStr)
    {
        this.modID = modID;
        this.modName = modName;
        this.updateSettings = updateSettings;
        this.jsonObject = jsonObject;
        this.trueLatestVersionStr = latestVersionStr;

        this.lastLatestVersionSetting = (StringSetting) updateSettings.get(UpdateSettingsData.LAST_LATEST_VERSION.name());
        this.repeatNotifySetting = (BoolSetting) updateSettings.get(UpdateSettingsData.REPEAT_NOTIFY.name());

        // If last recorded latest version (written to file via updateSettings) is less than the true latest version,
        // a new update was released so a notification should always be posted and repeat notifying should be reset for this version
        if (isNewUpdateAvailable())
        {
            MinecraftForge.EVENT_BUS.register(this);
            return;
        }

        if (repeatNotifySetting.getCurrentValue())
        {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    private boolean isNewUpdateAvailable()
    {
        String lastRecordedLatestVersion = lastLatestVersionSetting.getCurrentValue();
        boolean isNewUpdateAvailable = new Version(trueLatestVersionStr).compareTo(new Version(lastRecordedLatestVersion)) > 0;

        if (isNewUpdateAvailable)
        {
            // Update the last recorded latest version setting, reset repeat notifying to the default (true), then save to file
            // Note: repeat notifying default could be changed by editing the config file directly but if someone knew to do that more power to em
            // This could be fixed by only loading current values for updateSettings to force defaults to always be the true hard coded defaults
            lastLatestVersionSetting.setCurrentValue(trueLatestVersionStr);
            // TODO: default vers has no meaning, just making them equal to prevent them from being out of date in cfg file
            // TODO: make more fine grained control over writing default values to file
            lastLatestVersionSetting.setDefaultValue(trueLatestVersionStr);
            repeatNotifySetting.reset();
            updateSettings.saveCurrentValues();
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

        ChatLogger.INSTANCE.addLog(getNotificationComponent());
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    private IChatComponent getNotificationComponent()
    {
        IChatComponent mainComp = new ChatComponentText("Version " + trueLatestVersionStr + " is available");

        IChatComponent latestReleaseComp = new ChatComponentText(" [Latest release]");
        ClickEvent openUrlEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/jonuuh-mc/" + modName + "/releases/latest");
        ChatStyle openUrlStyle = new ChatStyle().setChatClickEvent(openUrlEvent).setColor(EnumChatFormatting.BLUE);
        latestReleaseComp.setChatStyle(openUrlStyle);

        IChatComponent changelogComp = new ChatComponentText(" [Changelog]");
        HoverEvent hoverChangelogEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(getChangelogStr()));
        ChatStyle hoverChangelogStyle = new ChatStyle().setChatHoverEvent(hoverChangelogEvent).setColor(EnumChatFormatting.AQUA);
        changelogComp.setChatStyle(hoverChangelogStyle);

        IChatComponent suggestCommandComp = new ChatComponentText(" [Don't remind me again]");
        String command = "/" + modID + "$core " + UpdateSettingsData.REPEAT_NOTIFY.toString() + " false";
        ClickEvent suggestCommandEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command);
        ChatStyle suggestCommandStyle = new ChatStyle().setChatClickEvent(suggestCommandEvent).setColor(EnumChatFormatting.DARK_GRAY);
        suggestCommandComp.setChatStyle(suggestCommandStyle);

        return mainComp.appendSibling(latestReleaseComp).appendSibling(changelogComp).appendSibling(suggestCommandComp);
    }

    private String getChangelogStr()
    {
        String encodedChangelog = StaticAssetUtils.parseMemberAsString(jsonObject, "changelog");
        return (encodedChangelog != null) ? new String(Base64.getDecoder().decode(encodedChangelog)) : "";
    }
}

