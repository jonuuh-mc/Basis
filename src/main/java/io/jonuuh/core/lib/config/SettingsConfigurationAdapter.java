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
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.BufferedWriter;
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
     * @param file The file used for the Configuration, usually the {@link FMLPreInitializationEvent#getSuggestedConfigurationFile() suggested} file
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
     * @param file     The file used for the Configuration, usually the {@link FMLPreInitializationEvent#getSuggestedConfigurationFile() suggested} file
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
            loadSettingsDefaultValues(settings);
            loadSettingsCurrentValues(settings);
        }
    }

    public Settings getDefaultCategorySettings()
    {
        return configCategorySettingsMap.get(DEFAULT_CATEGORY);
    }

    public void putAndLoadSettings(Settings settings)
    {
        configCategorySettingsMap.put(settings.configurationCategory, settings);
        loadSettingsDefaultValues(settings);
        loadSettingsCurrentValues(settings);
    }

    /**
     * Saves the forge Configuration to file.
     * <p>
     * Writes each category of the Configuration and it's properties to the given file
     *
     * @see ConfigCategory#write(BufferedWriter, int)
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
     * WRITE INTO: A Configuration Property corresponding to the field
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
     * WRITE INTO: A Configuration Property corresponding to the field
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
            Property property = getProperty(settings, settingName, (valueType.toString() + settingName));
            transferSetting(setting, property, valueType, transferType);
        }
    }

    // TODO: this function really needs to be shortened, it's hard to make anything generic though;
    //  ultimately there are 8*2*2=32 cases, each of which *must* have a unique function call
    /**
     * Transfer a Setting field to or from a forge Configuration Property
     * <p>
     * {@link TransferType#LOAD}: Read from Property -> Write into Setting
     * <p>
     * {@link TransferType#SAVE}: Read from Setting -> Write into Property
     * <p>
     * {@link ValueType#DEFAULT}: Operate on the Setting's defaultValue field
     * <p>
     * {@link ValueType#CURRENT}: Operate on the Setting's currentValue field
     */
    private void transferSetting(Setting<?> setting, Property property, ValueType valueType, TransferType transferType)
    {
        switch (transferType)
        {
            case LOAD:
                if (setting instanceof BoolSetting)
                {
                    BoolSetting boolSetting = (BoolSetting) setting;
                    boolean prop = property.getBoolean();

                    if (valueType == ValueType.DEFAULT)
                    {
                        boolSetting.setDefaultValue(prop);
                    }
                    else
                    {
                        boolSetting.setCurrentValue(prop);
                    }
                }
                else if (setting instanceof DoubleSetting)
                {
                    DoubleSetting doubleSetting = (DoubleSetting) setting;
                    double prop = property.getDouble();

                    if (valueType == ValueType.DEFAULT)
                    {
                        doubleSetting.setDefaultValue(prop);
                    }
                    else
                    {
                        doubleSetting.setCurrentValue(prop);
                    }
                }
                else if (setting instanceof IntSetting)
                {
                    IntSetting intSetting = (IntSetting) setting;
                    int prop = property.getInt();

                    if (valueType == ValueType.DEFAULT)
                    {
                        intSetting.setDefaultValue(prop);
                    }
                    else
                    {
                        intSetting.setCurrentValue(prop);
                    }
                }
                else if (setting instanceof StringSetting)
                {
                    StringSetting stringSetting = (StringSetting) setting;
                    String prop = property.getString();

                    if (valueType == ValueType.DEFAULT)
                    {
                        stringSetting.setDefaultValue(prop);
                    }
                    else
                    {
                        stringSetting.setCurrentValue(prop);
                    }
                }
                else if (setting instanceof BoolArrSetting)
                {
                    BoolArrSetting boolArrSetting = ((BoolArrSetting) setting);
                    boolean[] prop = property.getBooleanList();

                    if (valueType == ValueType.DEFAULT)
                    {
                        boolArrSetting.setDefaultValue(prop);
                    }
                    else
                    {
                        boolArrSetting.setCurrentValue(prop);
                    }
                }
                else if (setting instanceof DoubleArrSetting)
                {
                    DoubleArrSetting doubleArrSetting = ((DoubleArrSetting) setting);
                    double[] prop = property.getDoubleList();

                    if (valueType == ValueType.DEFAULT)
                    {
                        doubleArrSetting.setDefaultValue(prop);
                    }
                    else
                    {
                        doubleArrSetting.setCurrentValue(prop);
                    }
                }
                else if (setting instanceof IntArrSetting)
                {
                    IntArrSetting intArrSetting = ((IntArrSetting) setting);
                    int[] prop = property.getIntList();

                    if (valueType == ValueType.DEFAULT)
                    {
                        intArrSetting.setDefaultValue(prop);
                    }
                    else
                    {
                        intArrSetting.setCurrentValue(prop);
                    }
                }
                else if (setting instanceof StringArrSetting)
                {
                    StringArrSetting stringArrSetting = ((StringArrSetting) setting);
                    String[] prop = property.getStringList();

                    if (valueType == ValueType.DEFAULT)
                    {
                        stringArrSetting.setDefaultValue(prop);
                    }
                    else
                    {
                        stringArrSetting.setCurrentValue(prop);
                    }
                }
                break;

            case SAVE:
                if (setting instanceof BoolSetting)
                {
                    BoolSetting boolSetting = (BoolSetting) setting;
                    property.setValue((valueType == ValueType.DEFAULT) ? boolSetting.getDefaultValue() : boolSetting.getCurrentValue());
                }
                else if (setting instanceof DoubleSetting)
                {
                    DoubleSetting doubleSetting = (DoubleSetting) setting;
                    property.setValue((valueType == ValueType.DEFAULT) ? doubleSetting.getDefaultValue() : doubleSetting.getCurrentValue());
                }
                else if (setting instanceof IntSetting)
                {
                    IntSetting intSetting = (IntSetting) setting;
                    property.setValue((valueType == ValueType.DEFAULT) ? intSetting.getDefaultValue() : intSetting.getCurrentValue());
                }
                else if (setting instanceof StringSetting)
                {
                    StringSetting stringSetting = (StringSetting) setting;
                    property.setValue((valueType == ValueType.DEFAULT) ? stringSetting.getDefaultValue() : stringSetting.getCurrentValue());
                }
                else if (setting instanceof BoolArrSetting)
                {
                    BoolArrSetting boolArrSetting = (BoolArrSetting) setting;
                    property.setValues((valueType == ValueType.DEFAULT) ? boolArrSetting.getDefaultValue() : boolArrSetting.getCurrentValue());
                }
                else if (setting instanceof DoubleArrSetting)
                {
                    DoubleArrSetting doubleArrSetting = (DoubleArrSetting) setting;
                    property.setValues((valueType == ValueType.DEFAULT) ? doubleArrSetting.getDefaultValue() : doubleArrSetting.getCurrentValue());
                }
                else if (setting instanceof IntArrSetting)
                {
                    IntArrSetting intArrSetting = (IntArrSetting) setting;
                    property.setValues((valueType == ValueType.DEFAULT) ? intArrSetting.getDefaultValue() : intArrSetting.getCurrentValue());
                }
                else if (setting instanceof StringArrSetting)
                {
                    StringArrSetting stringArrSetting = (StringArrSetting) setting;
                    property.setValues((valueType == ValueType.DEFAULT) ? stringArrSetting.getDefaultValue() : stringArrSetting.getCurrentValue());
                }
                break;
        }
    }

    /**
     * Retrieve a Property in the Configuration category corresponding to the given Settings.
     * <p>
     * The Property corresponds to a field of a Setting within the Settings.
     * <p>
     * The Property key should have a prefix ({@link ValueType#DEFAULT} or {@link ValueType#CURRENT}
     * denoting which of the two properties corresponding to the two fields of a Setting is returned.
     * <p>
     * If the Property DOES exist in the file: read and return it.
     * <p>
     * If the Property DOES NOT exist in the file: return a new Property with the current value of the field.
     *
     * @param settings    The Settings whose category to search in
     * @param settingName The name of the Setting whose field is retrieved
     * @param propKey     The Property's key in the Configuration category
     * @see Configuration#get(String, String, String, String, Property.Type)
     */
    private Property getProperty(Settings settings, String settingName, String propKey)
    {
        String category = settings.configurationCategory;
        Setting<?> setting = settings.get(settingName);

        if (setting instanceof BoolSetting)
        {
            return configuration.get(category, propKey, getFallbackValue(propKey, (BoolSetting) setting));
        }
        else if (setting instanceof DoubleSetting)
        {
            return configuration.get(category, propKey, getFallbackValue(propKey, (DoubleSetting) setting));
        }
        else if (setting instanceof IntSetting)
        {
            return configuration.get(category, propKey, getFallbackValue(propKey, (IntSetting) setting));
        }
        else if (setting instanceof StringSetting)
        {
            return configuration.get(category, propKey, getFallbackValue(propKey, (StringSetting) setting));
        }

        else if (setting instanceof BoolArrSetting)
        {
            return configuration.get(category, propKey, getFallbackValue(propKey, (BoolArrSetting) setting));
        }
        else if (setting instanceof DoubleArrSetting)
        {
            return configuration.get(category, propKey, getFallbackValue(propKey, (DoubleArrSetting) setting));
        }
        else if (setting instanceof IntArrSetting)
        {
            return configuration.get(category, propKey, getFallbackValue(propKey, (IntArrSetting) setting));
        }
        else if (setting instanceof StringArrSetting)
        {
            return configuration.get(category, propKey, getFallbackValue(propKey, (StringArrSetting) setting));
        }

        return null;
    }

    /**
     * The fallback value to use for the value of a Property when trying to retrieve it from the Configuration.
     * <p>
     * The property key should have a prefix denoting which of the two Setting fields is used as a fallback value.
     *
     * @param <T>     The return type, which is the same as the internal type of the given Setting
     * @param propKey The key of the Property corresponding to the field of the Setting
     * @param setting The setting whose field to use as a fallback
     * @return The fallback value
     */
    private <T> T getFallbackValue(String propKey, Setting<T> setting)
    {
        return propKey.startsWith(ValueType.CURRENT.toString()) ? setting.getCurrentValue() : setting.getDefaultValue();
    }

    private enum TransferType
    {
        LOAD, SAVE;
    }

    private enum ValueType
    {
        DEFAULT("default-"), CURRENT("current-");

        private final String keyPrefix;

        ValueType(String keyPrefix)
        {
            this.keyPrefix = keyPrefix;
        }

        @Override
        public String toString()
        {
            return keyPrefix;
        }
    }
}
