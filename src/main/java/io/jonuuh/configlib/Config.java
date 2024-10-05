package io.jonuuh.configlib;

import io.jonuuh.configlib.setting.BoolSetting;
import io.jonuuh.configlib.setting.DoubleSetting;
import io.jonuuh.configlib.setting.IntSetting;
import io.jonuuh.configlib.setting.Setting;
import io.jonuuh.configlib.setting.SettingDefinition;
import io.jonuuh.configlib.setting.SettingType;
import io.jonuuh.configlib.setting.Settings;
import io.jonuuh.configlib.setting.StringSetting;
import io.jonuuh.configlib.util.ChatLogger;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.Collections;
import java.util.Map;

public class Config
{
    private static Config instance;
    private final Configuration configuration;
    private final Map<String, Settings> configCategorySettingsMap;

    public static void createInstance(File file, Map<String, Settings> configCategorySettingsMap)
    {
        if (instance != null)
        {
            throw new IllegalStateException("Config instance has already been created");
        }

        instance = new Config(file, configCategorySettingsMap);
    }

    public static void createInstance(File file, Settings masterSettings)
    {
        if (instance != null)
        {
            throw new IllegalStateException("Config instance has already been created");
        }

        instance = new Config(file, masterSettings);
    }

    public static Config getInstance()
    {
        if (instance == null)
        {
            throw new NullPointerException("Config instance has not been created");
        }

        return instance;
    }

    private Config(File file, Map<String, Settings> configCategorySettingsMap)
    {
        this.configuration = new Configuration(file);
        this.configCategorySettingsMap = configCategorySettingsMap;

        for (String category : configCategorySettingsMap.keySet())
        {
            loadSettings(category);
        }
    }

    private Config(File file, Settings masterSettings)
    {
        this(file, Collections.singletonMap("master", masterSettings));
    }

//    private Config(File file)
//    {
//        this.configuration = new Configuration(file);
//        this.configCategorySettingsMap = new HashMap<>();
//
//        this.configCategorySettingsMap.put("master", new Settings("master", Arrays.asList(
//                new SettingDefinition("DRAW_BACKGROUND", SettingType.BOOLEAN),
//                new SettingDefinition("BACKGROUND_OPACITY", SettingType.DOUBLE),
//                new SettingDefinition("RENDER_RANGE_MIN", SettingType.INTEGER))));
//
////        this.configCategorySettingNamePropertyMap = new HashMap<>();
//
////        for (String category : configCategorySettingsMap.keySet())
////        {
////            this.configCategorySettingNamePropertyMap.put(category, new HashMap<>());
////        }
//
////        // Initialize property maps
////        for (Map.Entry<String, Settings> entry : configCategorySettingsMap.entrySet())
////        {
////            String category = entry.getKey();
////            Settings settings = entry.getValue();
////
////            Map<String, Property> propertyMap = new HashMap<>();
////
////            for (String settingName : settings.settingMap.keySet())
////            {
////                propertyMap.put(settingName, getProperty(category, settings.get(settingName), settingName));
////            }
////
////            this.configCategorySettingNamePropertyMap.put(category, propertyMap);
////
////        }
//
//        for (String category : configCategorySettingsMap.keySet())
//        {
//            loadSettings(category);
//        }
//    }

    public Settings getSettings(String category)
    {
        return configCategorySettingsMap.get(category);
    }
//
//    public void saveDefault()
//    {
//        saveSettings("master");
//    }

//    private void initPropertyMap(Configuration cfg, Settings settings)
//    {
//        for (String settingName : settings.settingMap.keySet())
//        {
//            configCategorySettingNamePropertyMap.put(settingName, getProperty(cfg, settings.configurationCategory, settings.get(settingName), settingName));
//        }
//    }

    // Load settings (read from Configuration category, write into Settings)
    public void loadSettings(String category)
    {
        Settings settings = configCategorySettingsMap.get(category);
        settings.setConfigurationCategory(category);

        for (String settingName : settings.settingMap.keySet())
        {
            Property property = getProperty(category, settings.get(settingName), settingName);
            loadSetting(property, settings.get(settingName));
        }
    }

    // Save settings (read from Settings, write into Configuration category)
    public void saveSettings(String category)
    {
        Settings settings = configCategorySettingsMap.get(category);

        for (String settingName : settings.settingMap.keySet())
        {
            Property property = getProperty(category, settings.get(settingName), settingName);
            saveSetting(property, settings.get(settingName));
        }

        configuration.save();
        ChatLogger.addLog("CONFIG SAVED: " + configuration.getConfigFile().getName() + "/" + category, EnumChatFormatting.GREEN);
    }

    private void loadSetting(Property property, Setting<?> setting)
    {
        switch (setting.getType())
        {
            case BOOLEAN:
                ((BoolSetting) setting).setValue(property.getBoolean());
                return;
            case DOUBLE:
                ((DoubleSetting) setting).setValue(property.getDouble());
                return;
            case INTEGER:
                ((IntSetting) setting).setValue(property.getInt());
                return;
            case STRING:
                ((StringSetting) setting).setValue(property.getString());
        }
    }

    private void saveSetting(Property property, Setting<?> setting)
    {
        switch (setting.getType())
        {
            case BOOLEAN:
                property.setValue(((BoolSetting) setting).getValue());
                return;
            case DOUBLE:
                property.setValue(((DoubleSetting) setting).getValue());
                return;
            case INTEGER:
                property.setValue(((IntSetting) setting).getValue());
                return;
            case STRING:
                property.setValue(((StringSetting) setting).getValue());
        }
    }

    private Property getProperty(String category, Setting<?> setting, String settingName)
    {
        switch (setting.getType())
        {
            case BOOLEAN:
                return configuration.get(category, settingName, ((BoolSetting) setting).getValue());
            case DOUBLE:
                return configuration.get(category, settingName, ((DoubleSetting) setting).getValue());
            case INTEGER:
                return configuration.get(category, settingName, ((IntSetting) setting).getValue());
            case STRING:
                return configuration.get(category, settingName, ((StringSetting) setting).getValue());
            default:
                return null;
        }
    }

//    private Property getGroupMembersProperty(String groupName)
//    {
//        return groupCfgMap.get(groupName).get(groupName, "!members", new String[0]);
//    }

//    private String trimExt(String fileName)
//    {
//        return fileName.replace(fileName.substring(fileName.indexOf('.')), "");
//    }
}
