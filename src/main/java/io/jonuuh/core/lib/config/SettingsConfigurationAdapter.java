package io.jonuuh.core.lib.config;

import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.config.setting.types.array.BoolArrSetting;
import io.jonuuh.core.lib.config.setting.types.array.DoubleArrSetting;
import io.jonuuh.core.lib.config.setting.types.array.IntArrSetting;
import io.jonuuh.core.lib.config.setting.types.array.StringArrSetting;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.single.DoubleSetting;
import io.jonuuh.core.lib.config.setting.types.single.IntSetting;
import io.jonuuh.core.lib.config.setting.types.single.StringSetting;
import io.jonuuh.core.lib.util.ChatLogger;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A bridge between Settings objects and a forge Configuration object, which is itself a bridge to a mod's literal external config file.
 */
public final class SettingsConfigurationAdapter
{
    public static final String DEFAULT_CATEGORY = "master";
    public static SettingsConfigurationAdapter INSTANCE;
    public final Configuration configuration;
    public final Map<String, Settings> configCategorySettingsMap;

    /**
     * Initialize the SettingsConfigurationAdapter singleton with many Settings objects
     *
     * @param file The file used for the Configuration, usually the {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent#getSuggestedConfigurationFile() suggested} file
     * @param settingsList A list of settings to use
     */
    public static void createInstance(File file, List<Settings> settingsList)
    {
        if (INSTANCE != null)
        {
            throw new IllegalStateException("Config instance has already been created");
        }
        INSTANCE = new SettingsConfigurationAdapter(file, settingsList);
    }

    /**
     * Initialize the SettingsConfigurationAdapter singleton with one Settings object
     *
     * @param file The file used for the Configuration, usually the {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent#getSuggestedConfigurationFile() suggested} file
     * @param settings The settings to use
     */
    public static void createInstance(File file, Settings settings)
    {
        createInstance(file, Collections.singletonList(settings));
    }

    private SettingsConfigurationAdapter(File file, List<Settings> settingsList)
    {
        this.configuration = new Configuration(file);
        this.configCategorySettingsMap = new HashMap<>();

        for (Settings settings : settingsList)
        {
            this.configCategorySettingsMap.put(settings.configurationCategory, settings);
        }

        for (Settings settings : configCategorySettingsMap.values())
        {
            loadSettingsDefaultValues(settings); // TODO: don't load these by default?
            loadSettingsCurrentValues(settings);
        }
    }

    public Settings getSettings(String category)
    {
        return configCategorySettingsMap.get(category);
    }

    public Settings getDefaultCategorySettings()
    {
        return getSettings(DEFAULT_CATEGORY);
    }

    public void putAndLoadSettings(Settings settings)
    {
        configCategorySettingsMap.put(settings.configurationCategory, settings);
        loadSettingsDefaultValues(settings); // TODO: don't load these by default?
        loadSettingsCurrentValues(settings);
    }

    /**
     * Saves the forge Configuration to file.
     * <p>
     * Writes each category of the Configuration and it's properties to the given file
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
     * Load the default value field for each Setting in the given Settings.
     * <p>
     * READ FROM: A Configuration Property corresponding to the field
     * <p>
     * WRITE INTO: The default value field of a Setting
     */
    public void loadSettingsDefaultValues(Settings settings)
    {
        transferSettingsValues(settings, ValueType.DEFAULT, TransferType.LOAD);
    }

    /**
     * Save the default value field for each Setting in the given Settings.
     * <p>
     * READ FROM: The default value field of a Setting
     * <p>
     * WRITE INTO: A Configuration Property corresponding to the field, then write the properties to file
     *
     * @see SettingsConfigurationAdapter#saveConfiguration()
     */
    public void saveSettingsDefaultValues(Settings settings)
    {
        transferSettingsValues(settings, ValueType.DEFAULT, TransferType.SAVE);
        saveConfiguration();
    }

    /**
     * Load the current value field for each Setting in the given Settings.
     * <p>
     * READ FROM: A Configuration Property corresponding to the field
     * <p>
     * WRITE INTO: The current value field of a Setting
     */
    public void loadSettingsCurrentValues(Settings settings)
    {
        transferSettingsValues(settings, ValueType.CURRENT, TransferType.LOAD);
    }

    /**
     * Save the current value field for each Setting in the given Settings.
     * <p>
     * READ FROM: The current value field of a Setting
     * <p>
     * WRITE INTO: A Configuration Property corresponding to the field, then write the properties to file
     *
     * @see SettingsConfigurationAdapter#saveConfiguration()
     */
    public void saveSettingsCurrentValues(Settings settings)
    {
        transferSettingsValues(settings, ValueType.CURRENT, TransferType.SAVE);
        saveConfiguration();
    }

    private void transferSettingsValues(Settings settings, ValueType valueType, TransferType transferType)
    {
        for (Map.Entry<String, Setting<?>> settingEntry : settings.entrySet())
        {
            String settingName = settingEntry.getKey();
            Setting<?> setting = settingEntry.getValue();
            Property property = getProperty(settings, settingName, valueType);
            transferSetting(setting, property, valueType, transferType);
        }
    }

    /**
     * Transfer a Setting field to or from a forge Configuration Property
     * <p>
     * {@link ValueType#DEFAULT DEFAULT}: Operate on the Setting's defaultValue field
     * <p>
     * {@link ValueType#CURRENT CURRENT}: Operate on the Setting's currentValue field
     * <p>
     * {@link TransferType#LOAD LOAD}: Read from Property -> Write into Setting
     * <p>
     * {@link TransferType#SAVE SAVE}: Read from Setting -> Write into Property
     */
    private void transferSetting(Setting<?> setting, Property property, ValueType valueType, TransferType transferType)
    {
        switch (transferType)
        {
            case LOAD:
                if (setting instanceof BoolSetting)
                {
                    setSettingValue((BoolSetting) setting, property.getBoolean(), valueType);
                }
                else if (setting instanceof DoubleSetting)
                {
                    setSettingValue((DoubleSetting) setting, property.getDouble(), valueType);
                }
                else if (setting instanceof IntSetting)
                {
                    setSettingValue((IntSetting) setting, property.getInt(), valueType);
                }
                else if (setting instanceof StringSetting)
                {
                    setSettingValue((StringSetting) setting, property.getString(), valueType);
                }
                else if (setting instanceof BoolArrSetting)
                {
                    setSettingValue((BoolArrSetting) setting, property.getBooleanList(), valueType);
                }
                else if (setting instanceof DoubleArrSetting)
                {
                    setSettingValue((DoubleArrSetting) setting, property.getDoubleList(), valueType);
                }
                else if (setting instanceof IntArrSetting)
                {
                    setSettingValue((IntArrSetting) setting, property.getIntList(), valueType);
                }
                else if (setting instanceof StringArrSetting)
                {
                    setSettingValue((StringArrSetting) setting, property.getStringList(), valueType);
                }
                break;

            case SAVE:
                if (setting instanceof BoolSetting)
                {
                    property.setValue(getSettingValue((BoolSetting) setting, valueType));
                }
                else if (setting instanceof DoubleSetting)
                {
                    property.setValue(getSettingValue((DoubleSetting) setting, valueType));
                }
                else if (setting instanceof IntSetting)
                {
                    property.setValue(getSettingValue((IntSetting) setting, valueType));
                }
                else if (setting instanceof StringSetting)
                {
                    property.setValue(getSettingValue((StringSetting) setting, valueType));
                }
                else if (setting instanceof BoolArrSetting)
                {
                    property.setValues(getSettingValue((BoolArrSetting) setting, valueType));
                }
                else if (setting instanceof DoubleArrSetting)
                {
                    property.setValues(getSettingValue((DoubleArrSetting) setting, valueType));
                }
                else if (setting instanceof IntArrSetting)
                {
                    property.setValues(getSettingValue((IntArrSetting) setting, valueType));
                }
                else if (setting instanceof StringArrSetting)
                {
                    property.setValues(getSettingValue((StringArrSetting) setting, valueType));
                }
                break;
        }
    }

    /**
     * Retrieve a Property in the Configuration category corresponding to the given Settings.
     * <p>
     * The Property corresponds to a field of a Setting within the Settings.
     * <p>
     * If the Property DOES exist in the file: read and return it.
     * <p>
     * If the Property DOES NOT exist in the file: return a new Property with the current value of the field.
     *
     * @param settings The Settings whose category to search in
     * @param settingName The name of the Setting whose field is retrieved
     * @param valueType Denotes which of the two properties corresponding to the two fields of a Setting is returned.
     * @see Configuration#get(String, String, String, String, Property.Type)
     */
    private Property getProperty(Settings settings, String settingName, ValueType valueType)
    {
        String category = settings.configurationCategory;
        String propKey = getPropKey(settingName, valueType);
        Setting<?> setting = settings.get(settingName);

        if (setting instanceof BoolSetting)
        {
            return configuration.get(category, propKey, getSettingValue((BoolSetting) setting, valueType));
        }
        else if (setting instanceof DoubleSetting)
        {
            return configuration.get(category, propKey, getSettingValue((DoubleSetting) setting, valueType));
        }
        else if (setting instanceof IntSetting)
        {
            return configuration.get(category, propKey, getSettingValue((IntSetting) setting, valueType));
        }
        else if (setting instanceof StringSetting)
        {
            return configuration.get(category, propKey, getSettingValue((StringSetting) setting, valueType));
        }

        else if (setting instanceof BoolArrSetting)
        {
            return configuration.get(category, propKey, getSettingValue((BoolArrSetting) setting, valueType));
        }
        else if (setting instanceof DoubleArrSetting)
        {
            return configuration.get(category, propKey, getSettingValue((DoubleArrSetting) setting, valueType));
        }
        else if (setting instanceof IntArrSetting)
        {
            return configuration.get(category, propKey, getSettingValue((IntArrSetting) setting, valueType));
        }
        else if (setting instanceof StringArrSetting)
        {
            return configuration.get(category, propKey, getSettingValue((StringArrSetting) setting, valueType));
        }

        return null;
    }

    /**
     * Set the value of a setting using a ValueType.
     *
     * @param <T> The literal value type, which is the same as the internal type of the given Setting
     * @param setting The setting whose value to set
     * @param value A value that is set
     * @param valueType The ValueType corresponding to one of the two fields of the Setting
     */
    private <T> void setSettingValue(Setting<T> setting, T value, ValueType valueType)
    {
        if (valueType == ValueType.DEFAULT)
        {
            setting.setDefaultValue(value);
        }
        else
        {
            setting.setCurrentValue(value);
        }
    }

    /**
     * Retrieve the value of a setting using a ValueType.
     * <p>
     * The ValueType denotes which of the two Setting fields is returned.
     *
     * @param <T> The return type, which is the same as the internal type of the given Setting
     * @param valueType The ValueType corresponding to one of the two fields of the Setting
     * @param setting The setting whose field to return
     * @return A setting value
     */
    private <T> T getSettingValue(Setting<T> setting, ValueType valueType)
    {
        return valueType == ValueType.DEFAULT ? setting.getDefaultValue() : setting.getCurrentValue();
    }

    /**
     * Constructs a property key string as the settingName with a ValueType suffix
     */
    private String getPropKey(String settingName, ValueType valueType)
    {
        return settingName + valueType.toString();
    }

    private enum TransferType
    {
        LOAD, SAVE
    }

    private enum ValueType
    {
        DEFAULT("-default"), CURRENT("-current");

        private final String keySuffix;

        ValueType(String keySuffix)
        {
            this.keySuffix = keySuffix;
        }

        @Override
        public String toString()
        {
            return keySuffix;
        }
    }
}
