package io.jonuuh.core.module.config;

import io.jonuuh.core.module.config.setting.Setting;
import io.jonuuh.core.module.config.setting.Settings;
import io.jonuuh.core.module.config.setting.types.BoolListSetting;
import io.jonuuh.core.module.config.setting.types.BoolSetting;
import io.jonuuh.core.module.config.setting.types.DoubleListSetting;
import io.jonuuh.core.module.config.setting.types.DoubleSetting;
import io.jonuuh.core.module.config.setting.types.IntListSetting;
import io.jonuuh.core.module.config.setting.types.IntSetting;
import io.jonuuh.core.module.config.setting.types.StringListSetting;
import io.jonuuh.core.module.config.setting.types.StringSetting;
import io.jonuuh.core.module.util.ChatLogger;
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
        this(file, Collections.singletonMap(masterSettings.configurationCategory, masterSettings));
    }

    public Settings getSettings(String category)
    {
        return configCategorySettingsMap.get(category);
    }

    // Load settings (read from Configuration category, write into Settings)
    public void loadSettings(String category)
    {
        Settings settings = getSettings(category);

        for (String settingName : settings.keySet())
        {
            Property property = getProperty(category, settings.get(settingName), settingName);
            loadSetting(property, settings.get(settingName));
        }
    }

    // Save settings (read from Settings, write into Configuration category)
    public void saveSettings(String category)
    {
        Settings settings = getSettings(category);

        for (String settingName : settings.keySet())
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
                return;

            case BOOLEAN_LIST:
                ((BoolListSetting) setting).setValue(property.getBooleanList());
                return;
            case DOUBLE_LIST:
                ((DoubleListSetting) setting).setValue(property.getDoubleList());
                return;
            case INTEGER_LIST:
                ((IntListSetting) setting).setValue(property.getIntList());
                return;
            case STRING_LIST:
                ((StringListSetting) setting).setValue(property.getStringList());
                return;
            default:
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
                return;

            case BOOLEAN_LIST:
                property.setValues(((BoolListSetting) setting).getValue());
                return;
            case DOUBLE_LIST:
                property.setValues(((DoubleListSetting) setting).getValue());
                return;
            case INTEGER_LIST:
                property.setValues(((IntListSetting) setting).getValue());
                return;
            case STRING_LIST:
                property.setValues(((StringListSetting) setting).getValue());
                return;
            default:
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

            case BOOLEAN_LIST:
                return configuration.get(category, settingName, ((BoolListSetting) setting).getValue());
            case DOUBLE_LIST:
                return configuration.get(category, settingName, ((DoubleListSetting) setting).getValue());
            case INTEGER_LIST:
                return configuration.get(category, settingName, ((IntListSetting) setting).getValue());
            case STRING_LIST:
                return configuration.get(category, settingName, ((StringListSetting) setting).getValue());
            default:
                return null;
        }
    }
}
