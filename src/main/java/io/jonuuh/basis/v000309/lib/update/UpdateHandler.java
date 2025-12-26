package io.jonuuh.basis.v000309.lib.update;

import com.google.gson.JsonObject;
import io.jonuuh.basis.v000309.lib.config.ConfigManager;
import io.jonuuh.basis.v000309.lib.config.setting.Settings;
import io.jonuuh.basis.v000309.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.basis.v000309.lib.config.setting.types.single.StringSetting;
import io.jonuuh.basis.v000309.lib.util.StaticAssetUtils;
import io.jonuuh.basis.v000309.lib.util.logging.ChatLogger;

public final class UpdateHandler
{
    public static final String configurationCategory = "update";
    public final String latestVersionStr;
    public final boolean isUpdateAvailable;

    public UpdateHandler(String modID, String modName, String currentVersionStr, JsonObject jsonObject, ChatLogger chatLogger)
    {
        if (jsonObject == null)
        {
            System.out.println("Null JsonObject while creating UpdateHandler");
            latestVersionStr = null;
            isUpdateAvailable = false;
            return;
        }

        this.latestVersionStr = StaticAssetUtils.parseMemberAsString(jsonObject, "version");
        this.isUpdateAvailable = new Version(latestVersionStr).compareTo(new Version(currentVersionStr)) > 0;

        String s = "isUpdateAvailable = " + isUpdateAvailable + "; (current:" + currentVersionStr + ") " +
                (isUpdateAvailable ? "<" : ">=") + " (latest:" + latestVersionStr + ")";

        if (true || isUpdateAvailable) // TODO:
        {
            Settings updateSettings = new Settings(configurationCategory);
            updateSettings.put(UpdateSettingKey.LAST_LATEST_VERSION, new StringSetting(currentVersionStr));
            updateSettings.put(UpdateSettingKey.REPEAT_NOTIFY, new BoolSetting(true));
            ConfigManager.getAdapter(modID).putAndLoadSettings(updateSettings);

            new NotificationPoster(modID, modName, updateSettings, jsonObject, latestVersionStr, chatLogger);
        }
    }
}
