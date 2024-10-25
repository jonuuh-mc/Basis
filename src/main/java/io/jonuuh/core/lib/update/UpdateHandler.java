package io.jonuuh.core.lib.update;

import io.jonuuh.core.lib.config.Config;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.StringSetting;
import io.jonuuh.core.lib.util.ModLogger;
import io.jonuuh.core.lib.util.StaticFileUtils;

public class UpdateHandler
{
    public static UpdateHandler instance;

    private final String latestVersionStr;
    private final boolean isUpdateAvailable;

    public static void createInstance(String modID, String currentVersionStr)
    {
        if (instance != null)
        {
            ModLogger.INSTANCE.fatal("UpdateChecker instance has already been created");
            throw new IllegalStateException();
        }

        instance = new UpdateHandler(modID, currentVersionStr);
    }

    public static boolean isCreated()
    {
        return instance != null;
    }

    private UpdateHandler(String modID, String currentVersionStr)
    {
        this.latestVersionStr = StaticFileUtils.getStaticHostedAsset("versioning/" + modID + "/latest-version.txt");

        this.isUpdateAvailable = new Version(currentVersionStr).compareTo(new Version(latestVersionStr)) < 0;

        String message = "isUpdateAvailable = " + isUpdateAvailable + "; (current:" + currentVersionStr + ") " + (isUpdateAvailable ? "<" : ">=") + " (latest:" + latestVersionStr + ")";
        ModLogger.INSTANCE.info(message);

        if (true || isUpdateAvailable) // TODO:
        {
            // TODO: should this be in here or at top of constructor?
            Settings updateSettings = new Settings("update");
            updateSettings.put("LAST_NOTIFICATION_INSTANT", new StringSetting());
            updateSettings.put("LAST_LATEST_VERSION", new StringSetting(currentVersionStr));
            updateSettings.put("DISABLE_REMINDING_FOR_LATEST", new BoolSetting());
            Config.getInstance().putAndLoadSettings(updateSettings);

            new NotificationPoster(modID, updateSettings, latestVersionStr);
        }
    }

    public String getLatestVersionStr()
    {
        return latestVersionStr;
    }

    public boolean isUpdateAvailable()
    {
        return isUpdateAvailable;
    }
}
