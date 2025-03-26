package io.jonuuh.core.lib.update;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.jonuuh.core.lib.config.Config;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.single.StringSetting;
import io.jonuuh.core.lib.util.Log4JLogger;
import io.jonuuh.core.lib.util.StaticAssetUtils;

import java.lang.reflect.Type;
import java.util.Map;

public class UpdateHandler extends Thread
{
    public static UpdateHandler INSTANCE;
    private final String modID;
    private final String currentVersionStr;
    private String latestVersionStr;
    private boolean isUpdateAvailable;

    public static void createInstance(String modID, String currentVersionStr)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new UpdateHandler(modID, currentVersionStr);
            return;
        }
        Log4JLogger.INSTANCE.error("UpdateHandler instance has already been created");
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

    // TODO: move all this to constructor?
    @Override
    public void run()
    {
        Settings updateSettings = new Settings("update");
//        updateSettings.put("LAST_NOTIFICATION_INSTANT", new StringSetting());
        updateSettings.put("LAST_LATEST_VERSION", new StringSetting(currentVersionStr));
        updateSettings.put("NOTIFY_AGAIN_FOR_LATEST", new BoolSetting());
        Config.INSTANCE.putAndLoadSettings(updateSettings);

        String versioningJson = StaticAssetUtils.getStaticHostedAsset("versioning/" + modID + "/" + modID + ".json");
        Map<String, String> versioningMap = parseJson(versioningJson);

        this.latestVersionStr = versioningMap.get("version");
        this.isUpdateAvailable = new Version(currentVersionStr).compareTo(new Version(latestVersionStr)) < 0;

        String message = "isUpdateAvailable = " + isUpdateAvailable + "; (current:" + currentVersionStr + ") " + (isUpdateAvailable ? "<" : ">=") + " (latest:" + latestVersionStr + ")";
        Log4JLogger.INSTANCE.info(message);

        if (true || isUpdateAvailable) // TODO:
        {
            new NotificationPoster(modID, updateSettings, versioningMap);
        }
        else
        {
            // TODO: why are we resetting here
            updateSettings.reset();
        }

//        MinecraftForge.EVENT_BUS.post(new ApiRequestFinishedEvent());
    }

    public Map<String, String> parseJson(String jsonStr)
    {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return gson.fromJson(jsonStr, type);
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
