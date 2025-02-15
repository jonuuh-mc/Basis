package io.jonuuh.core.lib.update;

import io.jonuuh.core.lib.config.Config;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.single.StringSetting;
import io.jonuuh.core.lib.util.Log4JLogger;
import io.jonuuh.core.lib.util.StaticFileUtils;

public class UpdateHandler extends Thread
{
    public static UpdateHandler INSTANCE;
    private final String modID;
    private final String currentVersionStr;
    private String latestVersionStr;
    private boolean isUpdateAvailable;

    public static void createInstance(String modID, String currentVersionStr)
    {
        if (INSTANCE != null)
        {
            Log4JLogger.INSTANCE.fatal("UpdateHandler instance has already been created");
            throw new IllegalStateException();
        }
        INSTANCE = new UpdateHandler(modID, currentVersionStr);
    }

    public static boolean isCreated()
    {
        return INSTANCE != null;
    }

    private UpdateHandler(String modID, String currentVersionStr)
    {
        this.modID = modID;
        this.currentVersionStr = currentVersionStr;
    }

    public void run()
    {
        Settings updateSettings = new Settings("update");
        updateSettings.put("LAST_NOTIFICATION_INSTANT", new StringSetting());
        updateSettings.put("LAST_LATEST_VERSION", new StringSetting(currentVersionStr));
        updateSettings.put("DISABLE_REMINDING_FOR_LATEST", new BoolSetting());
        Config.INSTANCE.putAndLoadSettings(updateSettings);

        this.latestVersionStr = StaticFileUtils.getStaticHostedAsset("versioning/" + modID + "/latest-version.txt");
        this.isUpdateAvailable = new Version(currentVersionStr).compareTo(new Version(latestVersionStr)) < 0;

        String message = "isUpdateAvailable = " + isUpdateAvailable + "; (current:" + currentVersionStr + ") " + (isUpdateAvailable ? "<" : ">=") + " (latest:" + latestVersionStr + ")";
        Log4JLogger.INSTANCE.info(message);

        if (true || isUpdateAvailable) // TODO:
        {
            new NotificationPoster(modID, updateSettings, latestVersionStr);
        }
        else
        {
            updateSettings.reset();
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
