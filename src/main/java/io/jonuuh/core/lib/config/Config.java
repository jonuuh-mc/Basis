package io.jonuuh.core.lib.config;

import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.config.setting.types.array.BoolArrSetting;
import io.jonuuh.core.lib.config.setting.types.array.DoubleArrSetting;
import io.jonuuh.core.lib.config.setting.types.array.IntArrSetting;
import io.jonuuh.core.lib.config.setting.types.array.StringArrSetting;
import io.jonuuh.core.lib.config.setting.types.list.BoolListSetting;
import io.jonuuh.core.lib.config.setting.types.list.DoubleListSetting;
import io.jonuuh.core.lib.config.setting.types.list.IntListSetting;
import io.jonuuh.core.lib.config.setting.types.list.StringListSetting;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.single.DoubleSetting;
import io.jonuuh.core.lib.config.setting.types.single.IntSetting;
import io.jonuuh.core.lib.config.setting.types.single.StringSetting;
import io.jonuuh.core.lib.util.ChatLogger;
import io.jonuuh.core.lib.util.Log4JLogger;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config
{
    public static Config INSTANCE;
    private final Configuration configuration;
    private final Map<String, Settings> configCategorySettingsMap;
    private final String MASTER_CATEGORY = "master";

    public static void createInstance(File file, List<Settings> settingsList)
    {
        if (INSTANCE != null)
        {
            throw new IllegalStateException("Config instance has already been created");
        }
        INSTANCE = new Config(file, settingsList);
    }

    public static void createInstance(File file, Settings masterSettings)
    {
        createInstance(file, Collections.singletonList(masterSettings));
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
            Log4JLogger.INSTANCE.warn("Settings list contains multiple instances with the same category ({}). For each duplicate, the last in the list will be used.", categories);
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

    public void putAndLoadSettings(Settings settings)
    {
        configCategorySettingsMap.put(settings.configurationCategory, settings);
        loadSettings(settings.configurationCategory);
    }

    /**
     * Saves the actual forge configuration <p> (writes configuration's properties to mod's .cfg file)
     *
     * @see net.minecraftforge.common.config.ConfigCategory#write(java.io.BufferedWriter, int)
     */
    public void saveConfiguration()
    {
        configuration.save();

        if (ChatLogger.INSTANCE != null)
        {
            ChatLogger.INSTANCE.addLog("CONFIG SAVED: " + configuration.getConfigFile().getName(), EnumChatFormatting.GREEN);
        }
    }

    /**
     * Load the properties for some Configuration category into their respective Settings
     * <p>
     * Read each Configuration property for this category -> Write value into each Setting
     */
    public void loadSettings(String category)
    {
        Settings settings = getSettings(category);

        for (String settingName : settings.keySet())
        {
            Property property = getProperty(category, settings.get(settingName), settingName);
            loadSetting(property, settings.get(settingName));
        }
    }

    /**
     * Save the properties for some Configuration category to the mod's .cfg file
     * <p>
     * Read value from each Setting -> Write values into respective Configuration properties -> Write Configuration properties to file
     */
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

            case BOOLEAN_ARRAY:
                ((BoolArrSetting) setting).setValue(property.getBooleanList());
                return;
            case DOUBLE_ARRAY:
                ((DoubleArrSetting) setting).setValue(property.getDoubleList());
                return;
            case INTEGER_ARRAY:
                ((IntArrSetting) setting).setValue(property.getIntList());
                return;
            case STRING_ARRAY:
                ((StringArrSetting) setting).setValue(property.getStringList());
                return;

            case BOOLEAN_LIST:
                ((BoolListSetting) setting).setValueFromArr(property.getBooleanList());
                return;
            case DOUBLE_LIST:
                ((DoubleListSetting) setting).setValueFromArr(property.getDoubleList());
                return;
            case INTEGER_LIST:
                ((IntListSetting) setting).setValueFromArr(property.getIntList());
                return;
            case STRING_LIST:
                ((StringListSetting) setting).setValueFromArr(property.getStringList());
                return;

            default:
        }
    }

    /**
     * Save a setting into a configuration property.
     * <p>
     * Read from Setting -> Write into Configuration property
     */
    private Property saveSetting(Property property, Setting<?> setting)
    {
        switch (setting.getType())
        {
            case BOOLEAN:
                return property.setValue(((BoolSetting) setting).getValue());
            case DOUBLE:
                return property.setValue(((DoubleSetting) setting).getValue());
            case INTEGER:
                return property.setValue(((IntSetting) setting).getValue());
            case STRING:
                return property.setValue(((StringSetting) setting).getValue());

            case BOOLEAN_ARRAY:
                return property.setValues(((BoolArrSetting) setting).getValue());
            case DOUBLE_ARRAY:
                return property.setValues(((DoubleArrSetting) setting).getValue());
            case INTEGER_ARRAY:
                return property.setValues(((IntArrSetting) setting).getValue());
            case STRING_ARRAY:
                return property.setValues(((StringArrSetting) setting).getValue());

            case BOOLEAN_LIST:
                return property.setValues(((BoolListSetting) setting).getValueAsArr());
            case DOUBLE_LIST:
                return property.setValues(((DoubleListSetting) setting).getValueAsArr());
            case INTEGER_LIST:
                return property.setValues(((IntListSetting) setting).getValueAsArr());
            case STRING_LIST:
                return property.setValues(((StringListSetting) setting).getValueAsArr());

            default:
                return null;
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

            case BOOLEAN_ARRAY:
                return configuration.get(category, settingName, ((BoolArrSetting) setting).getValue());
            case DOUBLE_ARRAY:
                return configuration.get(category, settingName, ((DoubleArrSetting) setting).getValue());
            case INTEGER_ARRAY:
                return configuration.get(category, settingName, ((IntArrSetting) setting).getValue());
            case STRING_ARRAY:
                return configuration.get(category, settingName, ((StringArrSetting) setting).getValue());

            case BOOLEAN_LIST:
                return configuration.get(category, settingName, ((BoolListSetting) setting).getValueAsArr());
            case DOUBLE_LIST:
                return configuration.get(category, settingName, ((DoubleListSetting) setting).getValueAsArr());
            case INTEGER_LIST:
                return configuration.get(category, settingName, ((IntListSetting) setting).getValueAsArr());
            case STRING_LIST:
                return configuration.get(category, settingName, ((StringListSetting) setting).getValueAsArr());

            default:
                return null;
        }
    }
}
