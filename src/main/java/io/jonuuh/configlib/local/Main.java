package io.jonuuh.configlib.local;

import io.jonuuh.configlib.Config;
import io.jonuuh.configlib.command.CommandOpenSettingsGui;
import io.jonuuh.configlib.setting.SettingDefinition;
import io.jonuuh.configlib.setting.SettingType;
import io.jonuuh.configlib.setting.Settings;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;

@Mod(
        modid = Main.ModID,
        version = "1.0.0",
        acceptedMinecraftVersions = "[1.8.9]"
)
public class Main
{
    public static final String ModID = "configlib";

    @Mod.EventHandler
    public void FMLPreInit(FMLPreInitializationEvent event)
    {
        Settings masterSettings = new Settings(Arrays.asList(
                new SettingDefinition("DRAW_BACKGROUND", SettingType.BOOLEAN),
                new SettingDefinition("BACKGROUND_OPACITY", SettingType.DOUBLE),
                new SettingDefinition("RENDER_RANGE_MIN", SettingType.INTEGER),
                new SettingDefinition("BACKGROUND_COLOR", SettingType.STRING)));

        Config.createInstance(event.getSuggestedConfigurationFile(), masterSettings);
    }

    @Mod.EventHandler
    public void FMLInit(FMLInitializationEvent event)
    {
//        KeyBinding keyBinding = new KeyBinding("<description>", Keyboard.KEY_NONE, ModID);
//        ClientRegistry.registerKeyBinding(keyBinding);
//        MinecraftForge.EVENT_BUS.register(new Events(keyBinding));

        ClientCommandHandler.instance.registerCommand(new CommandOpenSettingsGui(ModID, new SettingsGuiImpl()));
    }
}
