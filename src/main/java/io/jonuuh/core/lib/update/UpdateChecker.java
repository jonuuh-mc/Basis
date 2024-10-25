//package io.jonuuh.core.lib.update;
//
//import io.jonuuh.core.lib.util.ModLogger;
//import io.jonuuh.core.lib.util.StaticFileUtils;
//
//public class UpdateChecker
//{
//    private static UpdateChecker instance;
//    private static String modID;
//    private final String latestVersionStr;
//    private final boolean isUpdateAvailable;
//
//    public static void createInstance(String modID, String currentVersionStr)
//    {
//        if (instance != null)
//        {
//            throw new IllegalStateException("[" + modID + "] UpdateChecker instance has already been created");
//        }
//
//        instance = new UpdateChecker(modID, currentVersionStr);
//    }
//
//    public static UpdateChecker getInstance()
//    {
//        if (instance == null)
//        {
//            throw new NullPointerException("[" + modID + "] UpdateChecker instance has not been created");
//        }
//
//        return instance;
//    }
//
//    private UpdateChecker(String modID, String currentVersionStr)
//    {
//        UpdateChecker.modID = modID;
//
//        String assetPath = "versionin/" + modID + "/latest-version.txt";
//        this.latestVersionStr = StaticFileUtils.getStaticHostedAsset(assetPath, "refs/heads/master/" + assetPath);
//
//        Version current = new Version(currentVersionStr);
//        Version latest = new Version(latestVersionStr);
//
//        this.isUpdateAvailable = current.compareTo(latest) < 0;
//        String sign = isUpdateAvailable ? "<" : ">=";
//        String message = "isUpdateAvailable = " + isUpdateAvailable + "; (current:" + current + ") " + sign + " (latest:" + latest + ")";
//        ModLogger.INSTANCE.info(message);
//    }
//
//    public String getLatestVersionStr()
//    {
//        return latestVersionStr;
//    }
//
//    public boolean isUpdateAvailable()
//    {
//        return isUpdateAvailable;
//    }
//}
//
