package io.jonuuh.core.lib.config;

import io.jonuuh.core.lib.config.setting.Setting;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.BoolListSetting;
import io.jonuuh.core.lib.config.setting.types.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.DoubleListSetting;
import io.jonuuh.core.lib.config.setting.types.DoubleSetting;
import io.jonuuh.core.lib.config.setting.types.IntListSetting;
import io.jonuuh.core.lib.config.setting.types.IntSetting;
import io.jonuuh.core.lib.config.setting.types.StringListSetting;
import io.jonuuh.core.lib.config.setting.types.StringSetting;
import io.jonuuh.core.lib.util.ChatLogger;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.BufferedWriter;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config
{
    private static Config instance;
    private final Configuration configuration;
    private final Map<String, Settings> configCategorySettingsMap;
    private final String MASTER_CATEGORY = "master";

    public static void createInstance(File file, List<Settings> settingsList)
    {
        if (instance != null)
        {
            throw new IllegalStateException("Config instance has already been created");
        }

        instance = new Config(file, settingsList);
    }

    public static void createInstance(File file, Settings masterSettings)
    {
        if (instance != null)
        {
            throw new IllegalStateException("Config instance has already been created");
        }

        instance = new Config(file, Collections.singletonList(masterSettings));
    }

    public static Config getInstance()
    {
        if (instance == null)
        {
            throw new NullPointerException("Config instance has not been created");
        }

        return instance;
    }

    private Config(File file, List<Settings> settingsList)
    {
        List<String> categories = settingsList.stream().map(settings -> settings.configurationCategory).collect(Collectors.toList());

        if (!categories.contains(MASTER_CATEGORY))
        {
            throw new IllegalArgumentException("Settings list must contain an instance with category \"" + MASTER_CATEGORY + "\". (!" + categories + ".contains(\"" + MASTER_CATEGORY + "\")");
        }
//        else if (categories.indexOf(MASTER_CATEGORY) != categories.lastIndexOf(MASTER_CATEGORY))
//        {
//            System.out.println("WARNING: Settings list contains multiple instances with category \"" + MASTER_CATEGORY + "\", the last in the list will be used.");
//        }

        // TODO: finish this
        if (categories.size() != new HashSet<>(categories).size())
        {
            System.out.println("WARNING: Settings list contains multiple instances with the same category. For each duplicate, the last in the list will be used.");
        }

        this.configuration = new Configuration(file);
        this.configCategorySettingsMap = new HashMap<>();

        for (Settings settings : settingsList)
        {
            this.configCategorySettingsMap.put(settings.configurationCategory, settings);
        }

        for (String category : configCategorySettingsMap.keySet())
        {
            loadSettings(category);
        }
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }

    public Map<String, Settings> getConfigCategorySettingsMap()
    {
        return configCategorySettingsMap;
    }

    public Settings getSettings(String category)
    {
        return configCategorySettingsMap.get(category);
    }

    public Settings getMasterSettings()
    {
        return getSettings(MASTER_CATEGORY);
    }

    // TODO: finish this
    public void putAndLoadSettings(Settings settings)
    {
        configCategorySettingsMap.put(settings.configurationCategory, settings);
        loadSettings(settings.configurationCategory);
    }

    /**
     * Saves the actual forge configuration <p> (writes configuration's properties to mod's .cfg file)
     *
     * @see net.minecraftforge.common.config.ConfigCategory#write(BufferedWriter, int)
     */
    public void saveConfiguration()
    {
        configuration.save();

        if (ChatLogger.INSTANCE != null)
        {
            ChatLogger.INSTANCE.addLog("CONFIG SAVED: " + configuration.getConfigFile().getName(), EnumChatFormatting.GREEN);
        }
    }

    // Load settings (read from Configuration category, write into respective Settings)
    public void loadSettings(String category)
    {
        Settings settings = getSettings(category);

        for (String settingName : settings.keySet())
        {
            Property property = getProperty(category, settings.get(settingName), settingName);
            loadSetting(property, settings.get(settingName));
        }
    }

    // Save settings (read from Settings, write into respective Configuration category)
    public void saveSettings(String category)
    {
        Settings settings = getSettings(category);

        for (String settingName : settings.keySet())
        {
            Property property = getProperty(category, settings.get(settingName), settingName);
            saveSetting(property, settings.get(settingName));
        }

        saveConfiguration();
    }

    public void loadSetting(String category, String settingName)
    {
        Setting<?> setting = getSettings(category).get(settingName);
        Property property = getProperty(category, setting, settingName);
        loadSetting(property, setting);
    }

    // TODO: this might not be any more efficient than saving all settings (check how forge saves to file, all properties vs only changed ones?)
    public void saveSetting(String category, String settingName)
    {
        Setting<?> setting = getSettings(category).get(settingName);
        Property property = getProperty(category, setting, settingName);
        saveSetting(property, setting);

        saveConfiguration();
    }

    /**
     * Load a configuration property into a setting.
     * <p>
     * Read from Configuration property -> Write into Setting
     *
     * @param property the property
     * @param setting  the setting
     */
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

    /**
     * Save a setting into a configuration property.
     * <p>
     * Read from Setting -> Write into Configuration property
     *
     * @param property the property
     * @param setting  the setting
     */
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

    /**
     * Gets a configuration property from the instance's .cfg file.
     * <p>
     * If the property DOES exist in the file: reads and returns it.
     * <p>
     * If the property DOES NOT exist in the file: returns a new property with the setting's current value.
     *
     * @param category    the configuration category in which to look for the property
     * @param setting     the setting whose value to use for the property if it does not exist
     * @param settingName the property's key (name) in the configuration
     * @return the property
     * @see net.minecraftforge.common.config.Configuration#get(java.lang.String, java.lang.String, java.lang.String, java.lang.String, net.minecraftforge.common.config.Property.Type)
     */
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
