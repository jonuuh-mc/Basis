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
import io.jonuuh.core.lib.util.logging.ChatLogger;
import io.jonuuh.core.lib.util.logging.ChatLoggerManager;
import io.jonuuh.core.lib.util.logging.Level;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.BufferedWriter;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A bridge between {@link Settings} objects and a Forge {@link Configuration}.
 * <p>
 * This singleton manages the transferral pipeline to and from {@link Setting} fields (current or default),
 * {@link Property properties}, {@link ConfigCategory categories},
 * a {@link Configuration}, and an external config file.
 *
 * <ul>
 *   <li>A {@link Configuration} directly reads and writes to an external config file</li>
 *   <li>Each {@link Settings} is associated 1 to 1 with a {@link ConfigCategory ConfigCategory}</li>
 * </ul>
 *
 * @see Configuration
 * @see Settings
 * @see Setting
 */
public final class SettingsConfigurationAdapter
{
    private final Configuration configuration;
    private final Map<String, Settings> configCategorySettingsMap;
    // TODO: allow for multiple cfg files for one instance

    SettingsConfigurationAdapter(File file, List<Settings> settingsList)
    {
        this.configuration = new Configuration(file);
        this.configCategorySettingsMap = new HashMap<>();

        for (Settings settings : settingsList)
        {
            this.configCategorySettingsMap.put(settings.configurationCategory, settings);
        }

        for (Settings settings : configCategorySettingsMap.values())
        {
            /**
             * TODO: don't always load defaults? should avoid writing them to file if they're unused.
             *  look into passing null as the configuration's default value for configuration.get()?
             * {@link SettingsConfigurationAdapter#loadSettingsValues(Settings, ValueType)}
             */
            loadSettingsDefaultValues(settings);
            loadSettingsCurrentValues(settings);
        }
    }

    /**
     * Retrieve the Settings associated with a given configuration category
     *
     * @param category The category key
     * @return The associated settings
     */
    public Settings getSettings(String category)
    {
        return configCategorySettingsMap.get(category);
    }

    public Settings getDefaultCategorySettings()
    {
        return getSettings(ConfigManager.DEFAULT_CATEGORY);
    }

    public void putSettings(Settings settings)
    {
        configCategorySettingsMap.put(settings.configurationCategory, settings);
    }

    public void putAndLoadSettings(Settings settings)
    {
        putSettings(settings);
        loadSettingsDefaultValues(settings);
        loadSettingsCurrentValues(settings);
    }

    /**
     * Saves the forge Configuration to file.
     * <p>
     * Writes each category of the Configuration and its properties to the given file
     *
     * @see ConfigCategory#write(BufferedWriter, int)
     */
    public void saveConfiguration()
    {
        configuration.save();

        ChatLogger chatLogger = ChatLoggerManager.getLogger("core");
        if (chatLogger != null)
        {
            String log = String.format("Config file '%s' saved", configuration.getConfigFile().getName());
            chatLogger.addSuccessLog(Level.DEBUG.intLevel, log);
        }
    }

    /**
     * Load the default value field for each Setting in the given Settings.
     *
     * @see SettingsConfigurationAdapter#loadSettingsValues(Settings, ValueType)
     */
    public void loadSettingsDefaultValues(Settings settings)
    {
        loadSettingsValues(settings, ValueType.DEFAULT);
    }

    /**
     * Load the current value field for each Setting in the given Settings.
     *
     * @see SettingsConfigurationAdapter#loadSettingsValues(Settings, ValueType)
     */
    public void loadSettingsCurrentValues(Settings settings)
    {
        loadSettingsValues(settings, ValueType.CURRENT);
    }

    /**
     * Save the default value field for each Setting in the given Settings, then write to file.
     *
     * @see SettingsConfigurationAdapter#saveSettingsValues(Settings, ValueType)
     * @see SettingsConfigurationAdapter#saveConfiguration()
     */
    public void saveSettingsDefaultValues(Settings settings)
    {
        saveSettingsValues(settings, ValueType.DEFAULT);
        saveConfiguration();
    }

    /**
     * Save the current value field for each Setting in the given Settings, then write to file.
     *
     * @see SettingsConfigurationAdapter#saveSettingsValues(Settings, ValueType)
     * @see SettingsConfigurationAdapter#saveConfiguration()
     */
    public void saveSettingsCurrentValues(Settings settings)
    {
        saveSettingsValues(settings, ValueType.CURRENT);
        saveConfiguration();
    }

    /**
     * Save the default value field for each Setting in the given Settings.
     *
     * @see SettingsConfigurationAdapter#saveSettingsValues(Settings, ValueType)
     */
    public void saveSettingsDefaultValuesWithoutFileWrite(Settings settings)
    {
        saveSettingsValues(settings, ValueType.DEFAULT);
    }

    /**
     * Save the current value field for each Setting in the given Settings.
     *
     * @see SettingsConfigurationAdapter#saveSettingsValues(Settings, ValueType)
     */
    public void saveSettingsCurrentValuesWithoutFileWrite(Settings settings)
    {
        saveSettingsValues(settings, ValueType.CURRENT);
    }


    /**
     * Load a value field for each Setting in the given Settings.
     * <p>
     * TODO: This can create {@link Configuration#get(String, String, String, String, Property.Type) new properties}
     * in the Configuration if they didn't already exist, which can cause unexpected writing to file on
     * {@link SettingsConfigurationAdapter#saveConfiguration() save}. For example attempting to load one list of
     * settings from file that didn't exist in file, then saving some other list of settings that did exist in
     * file will cause both sets of settings to be written to file.
     * <p>
     * READ FROM: A Configuration Property corresponding to the field
     * <p>
     * WRITE INTO: The value field of a Setting denoted by the valueType
     */
    private void loadSettingsValues(Settings settings, ValueType valueType)
    {
        transferSettingsValues(settings, valueType, TransferType.LOAD);
    }

    /**
     * Save a value field for each Setting in the given Settings.
     * <p>
     * READ FROM: The value field of a Setting denoted by the valueType
     * <p>
     * WRITE INTO: A Configuration Property corresponding to the field
     */
    private void saveSettingsValues(Settings settings, ValueType valueType)
    {
        transferSettingsValues(settings, valueType, TransferType.SAVE);
    }

    private void transferSettingsValues(Settings settings, ValueType valueType, TransferType transferType)
    {
        for (Map.Entry<String, Setting<?>> settingEntry : settings.getEntrySet())
        {
            Property property = getProperty(settings, settingEntry, valueType);
            transferSetting(settingEntry.getValue(), property, valueType, transferType);
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
     * If the Property DOES NOT exist in the file AND the current value of the field is not null:
     * create a new Property with the current value of the field, add it to the Configuration, and return it
     *
     * @param settings The Settings whose category to search in
     * @param settingEntry TODO:
     * @param valueType Denotes which of the two properties corresponding to the two fields of a Setting is returned.
     * @see Configuration#get(String, String, String, String, Property.Type)
     */
    private Property getProperty(Settings settings, Map.Entry<String, Setting<?>> settingEntry, ValueType valueType)
    {
        String category = settings.configurationCategory;
        String propKey = getPropKey(settingEntry.getKey(), valueType);
        Setting<?> setting = settingEntry.getValue();

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
     * Constructs a property key string as the setting key with a ValueType suffix
     */
    private String getPropKey(String settingKey, ValueType valueType)
    {
        return settingKey + valueType.toString();
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
