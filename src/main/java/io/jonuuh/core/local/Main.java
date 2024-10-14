package io.jonuuh.core.local;

import io.jonuuh.core.module.config.Config;
import io.jonuuh.core.module.config.command.CommandOpenSettingsGui;
import io.jonuuh.core.module.config.setting.SettingDefinition;
import io.jonuuh.core.module.config.setting.SettingType;
import io.jonuuh.core.module.config.setting.Settings;
import io.jonuuh.core.module.config.setting.types.BoolSetting;
import io.jonuuh.core.module.config.setting.types.DoubleListSetting;
import io.jonuuh.core.module.config.setting.types.DoubleSetting;
import io.jonuuh.core.module.config.setting.types.IntListSetting;
import io.jonuuh.core.module.config.setting.types.StringSetting;
import io.jonuuh.core.module.update.NotificationPoster;
import io.jonuuh.core.module.update.UpdateChecker;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(
        modid = Main.modID,
        version = "1.0.0",
        acceptedMinecraftVersions = "[1.8.9]"
)
public class Main
{
    public static final String modID = "modid";

    @Mod.EventHandler
    public void FMLPreInit(FMLPreInitializationEvent event)
    {
        Config.createInstance(event.getSuggestedConfigurationFile(), initSettings());

        UpdateChecker.createInstance(modID, "1.0.0");
    }

    @Mod.EventHandler
    public void FMLInit(FMLInitializationEvent event)
    {
        KeyBinding keyBinding = new KeyBinding("<description>", Keyboard.KEY_NONE, modID);
        ClientRegistry.registerKeyBinding(keyBinding);
        MinecraftForge.EVENT_BUS.register(new Events(keyBinding));

        MinecraftForge.EVENT_BUS.register(NotificationPoster.createInstance());

        ClientCommandHandler.instance.registerCommand(new CommandOpenSettingsGui(modID, new SettingsGuiImpl("master")));
    }

    private Settings initSettings()
    {
        Settings settings = new Settings("master");

        settings.put("DRAW_BACKGROUND", new BoolSetting(true));
        settings.put("BACKGROUND_OPACITY", new DoubleSetting(66D));
        settings.put("RENDER_RANGE", new IntListSetting(new int[]{1, 75}));
        settings.put("FAT_SLIDER", new DoubleListSetting(new double[]{10D, 15D, 20D, 5D, 30D, 45D, 1000D}));
        settings.put("BACKGROUND_COLOR", new StringSetting("BLUE"));

        return settings;
    }

//    private Map<String, Settings> initSettingsMap()
//    {
//        Map<String, Settings> configCategorySettingsMap = new HashMap<>();
//
//        Settings settings = new Settings("master");
//
//        settings.put("DRAW_BACKGROUND", new BoolSetting(true));
//        settings.put("BACKGROUND_OPACITY", new DoubleSetting(66D));
//        settings.put("RENDER_RANGE", new IntListSetting(new int[]{1, 75}));
//        settings.put("FAT_SLIDER", new DoubleListSetting(new double[]{10D, 15D, 20D, 5D, 30D, 45D, 1000D}));
//        settings.put("BACKGROUND_COLOR", new StringSetting("BLUE"));
//
//        configCategorySettingsMap.put("master", settings);
//
//        return configCategorySettingsMap;
//    }
}
