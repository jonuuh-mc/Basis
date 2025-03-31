package io.jonuuh.core.lib.update;

import com.google.gson.JsonObject;
import io.jonuuh.core.lib.config.SettingsConfigurationAdapter;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.single.StringSetting;
import io.jonuuh.core.lib.util.Log4JLogger;
import io.jonuuh.core.lib.util.StaticAssetUtils;

public final class UpdateHandler
{
    public static final String configurationCategory = "update";
    public final String latestVersionStr;
    public final boolean isUpdateAvailable;

    public UpdateHandler(String modID, String modName, String currentVersionStr, JsonObject jsonObject)
    {
        if (jsonObject == null)
        {
            Log4JLogger.INSTANCE.error("Null JsonObject while creating UpdateHandler");
            latestVersionStr = null;
            isUpdateAvailable = false;
            return;
        }

        this.latestVersionStr = StaticAssetUtils.parseMemberAsString(jsonObject, "version");
        this.isUpdateAvailable = new Version(latestVersionStr).compareTo(new Version(currentVersionStr)) > 0;

        String s = "isUpdateAvailable = " + isUpdateAvailable + "; (current:" + currentVersionStr + ") " +
                (isUpdateAvailable ? "<" : ">=") + " (latest:" + latestVersionStr + ")";
        Log4JLogger.INSTANCE.info(s);

        if (true || isUpdateAvailable) // TODO:
        {
            Settings updateSettings = new Settings(configurationCategory);
            updateSettings.put(UpdateSettingKey.LAST_LATEST_VERSION, new StringSetting(currentVersionStr));
            updateSettings.put(UpdateSettingKey.REPEAT_NOTIFY, new BoolSetting(true));
            SettingsConfigurationAdapter.INSTANCE.putAndLoadSettings(updateSettings);

            new NotificationPoster(modID, modName, updateSettings, jsonObject, latestVersionStr);
        }
    }
}
