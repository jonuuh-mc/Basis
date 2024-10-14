//package net.jonuuh.bnt.config;
//
//import net.jonuuh.bnt.BNT;
//import net.jonuuh.bnt.config.gui.ConfigGui;
//import net.jonuuh.bnt.config.setting.BoolSetting;
//import net.jonuuh.bnt.config.setting.CycleSetting;
//import net.jonuuh.bnt.config.setting.IntSetting;
//import net.jonuuh.bnt.config.setting.Setting;
//import net.jonuuh.bnt.config.setting.Settings;
//import net.jonuuh.bnt.util.ChatLogger;
//import net.minecraft.util.EnumChatFormatting;
//import net.minecraftforge.common.config.Configuration;
//import net.minecraftforge.common.config.Property;
//
//import java.io.File;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ConfigHandler
//{
//    private static ConfigHandler instance;
//    private final File configDir;
//    private final Settings masterSettings;
//    private final Configuration masterCfg;
//    private final Map<String, Group> groupMap;
//    private final Map<String, Configuration> groupCfgMap;
//    private final List<String> reservedGroups = Arrays.asList("create", "delete", "master");
//
//    public static void createInstance(File configDir)
//    {
//        if (instance == null)
//        {
//            instance = new ConfigHandler(configDir);
//        }
//    }
//
//    public static ConfigHandler getInstance()
//    {
//        if (instance == null)
//        {
//            throw new NullPointerException("ConfigFileHandler instance has not been created");
//        }
//
//        return instance;
//    }
//
//    private ConfigHandler(File configDir)
//    {
//        if (!configDir.exists())
//        {
//            configDir.mkdir();
//        }
//
//        this.configDir = configDir;
//        this.masterSettings = new Settings();
//        this.masterCfg = new Configuration(new File(configDir, "master.cfg"), BNT.version);
//        this.groupMap = new HashMap<>();
//        this.groupCfgMap = new HashMap<>();
//
//        // Load master settings (read data from master Configuration, write it into masterSettings)
//        loadSettings(masterCfg, masterSettings, "master");
//
//        // Create Configuration(s) and Group(s) for every group config file in the directory
//        File[] files = configDir.listFiles();
//        if (files != null)
//        {
//            for (File file : files)
//            {
//                String name = trimExt(file.getName());
//
//                if (!name.equals("master"))
//                {
//                    groupCfgMap.put(name, new Configuration(file, BNT.version));
//                    groupMap.put(name, new Group(new ConfigGui(new Settings(), name), 0));
//                }
//            }
//        }
//
//        // Load the Settings & members list for each Group (read data from each Configuration, write it into each Group)
//        for (String groupName : groupMap.keySet())
//        {
//            Group group = groupMap.get(groupName);
//
//            // Load group members list
//            group.addAll(Arrays.asList(getGroupMembersProperty(groupName).getStringList()));
//
//            // Load group settings
//            loadSettings(groupCfgMap.get(groupName), group.getSettings(), groupName);
//        }
//
//        System.out.println("[BNT] Loaded config: " + masterSettings + " --- " + groupMap);
//    }
//
//    public Configuration getMasterCfg()
//    {
//        return masterCfg;
//    }
//
//    public Settings getMasterSettings()
//    {
//        return masterSettings;
//    }
//
//    public Map<String, Group> getGroupMap()
//    {
//        return groupMap;
//    }
//
//    public Map<String, Configuration> getGroupCfgMap()
//    {
//        return groupCfgMap;
//    }
//
//    public File getConfigDir()
//    {
//        return configDir;
//    }
//
//    public void saveByKey(String key)
//    {
//        if (key.equals("master"))
//        {
//            saveSettings(masterCfg, masterSettings, "master");
//            return;
//        }
//
//        // Save the Settings & members list for a Group (read data from each Group, write it into each Configuration)
//        Group group = groupMap.get(key);
//
//        // Save group members list
//        getGroupMembersProperty(key).setValues(group.toArray(new String[0]));
//
//        // Save group settings
//        saveSettings(groupCfgMap.get(key), group.getSettings(), key);
//    }
//
//    // Create group & add it to groupMap, configMap, and create config file
//    public boolean createGroup(String groupKey)
//    {
//        if (reservedGroups.contains(groupKey))
//        {
//            return false;
//        }
//
//        File file = new File(configDir, groupKey + ".cfg");
//
//        if (groupMap.get(groupKey) == null && groupCfgMap.get(groupKey) == null && !file.exists())
//        {
//            groupMap.put(groupKey, new Group(new ConfigGui(new Settings(), groupKey), 0));
//            groupCfgMap.put(groupKey, new Configuration(file, BNT.version));
//            saveByKey(groupKey);
//            return true;
//        }
//        return false;
//    }
//
//    // Remove group from groupMap, configMap, and delete config file
//    public boolean deleteGroup(String groupKey)
//    {
//        if (reservedGroups.contains(groupKey))
//        {
//            return false;
//        }
//
//        File file = new File(configDir, groupKey + ".cfg");
//
//        if (groupMap.get(groupKey) != null && groupCfgMap.get(groupKey) != null && file.exists())
//        {
//            boolean removedGroup = groupMap.remove(groupKey) != null;
//            boolean removedConfig = groupCfgMap.remove(groupKey) != null;
//            boolean deletedFile = file.delete();
//            return (removedGroup && removedConfig && deletedFile);
//        }
//        return false;
//    }
//
//    private void loadSettings(Configuration cfg, Settings settings, String category)
//    {
//        for (Setting setting : settings.settingsMap.values())
//        {
//            if (setting instanceof BoolSetting)
//            {
//                BoolSetting boolSetting = (BoolSetting) setting;
//                boolSetting.setEnabled(getBoolProperty(cfg, settings, boolSetting, category).getBoolean());
//            }
//            else if (setting instanceof IntSetting)
//            {
//                IntSetting intSetting = (IntSetting) setting;
//                intSetting.setInt(getIntProperty(cfg, settings, intSetting, category).getInt());
//            }
//            else if (setting instanceof CycleSetting)
//            {
//                CycleSetting cycleSetting = (CycleSetting) setting;
//                cycleSetting.setVal(getCycleProperty(cfg, settings, cycleSetting, category).getInt());
//            }
//        }
//    }
//
//    private void saveSettings(Configuration cfg, Settings settings, String category)
//    {
//        for (Setting setting : settings.settingsMap.values())
//        {
//            if (setting instanceof BoolSetting)
//            {
//                BoolSetting boolSetting = (BoolSetting) setting;
//                getBoolProperty(cfg, settings, boolSetting, category).setValue(boolSetting.isEnabled());
//            }
//            else if (setting instanceof IntSetting)
//            {
//                IntSetting intSetting = (IntSetting) setting;
//                getIntProperty(cfg, settings, intSetting, category).setValue(intSetting.getInt());
//            }
//            else if (setting instanceof CycleSetting)
//            {
//                CycleSetting cycleSetting = (CycleSetting) setting;
//                getCycleProperty(cfg, settings, cycleSetting, category).setValue(cycleSetting.getVal());
//            }
//        }
//
//        if (cfg.hasChanged())
//        {
//            cfg.save();
//            ChatLogger.addLog("CONFIG SAVED: " + cfg.getConfigFile().getName(), EnumChatFormatting.GREEN);
//        }
//    }
//
//    private Property getBoolProperty(Configuration cfg, Settings settings, BoolSetting setting, String category)
//    {
//        return cfg.get(category, settings.getNameBySetting(setting), setting.isEnabled());
//    }
//
//    private Property getIntProperty(Configuration cfg, Settings settings, IntSetting setting, String category)
//    {
//        return cfg.get(category, settings.getNameBySetting(setting), setting.getInt());
//    }
//
//    private Property getCycleProperty(Configuration cfg, Settings settings, CycleSetting setting, String category)
//    {
//        return cfg.get(category, settings.getNameBySetting(setting), setting.getVal());
//    }
//
//    private Property getGroupMembersProperty(String groupName)
//    {
//        return groupCfgMap.get(groupName).get(groupName, "!members", new String[0]);
//    }
//
//    private String trimExt(String fileName)
//    {
//        return fileName.replace(fileName.substring(fileName.indexOf('.')), "");
//    }
//}
