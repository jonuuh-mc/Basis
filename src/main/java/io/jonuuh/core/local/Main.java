package io.jonuuh.core.local;

import io.jonuuh.core.lib.config.SettingsConfigurationAdapter;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.array.IntArrSetting;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.single.DoubleSetting;
import io.jonuuh.core.lib.config.setting.types.single.IntSetting;
import io.jonuuh.core.lib.config.setting.types.single.StringSetting;
import io.jonuuh.core.lib.gui.AbstractGuiScreen;
import io.jonuuh.core.lib.update.UpdateHandler;
import io.jonuuh.core.lib.util.ChatLogger;
import io.jonuuh.core.lib.util.Log4JLogger;
import io.jonuuh.core.lib.util.StaticAssetUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = Main.modID, version = Main.version, acceptedMinecraftVersions = "[1.8.9]")
public class Main
{
    public static final String modID = "core";
    public static final String version = "0.0.1";

    @Mod.EventHandler
    public void FMLPreInit(FMLPreInitializationEvent event)
    {
        Log4JLogger.createInstance(modID);

        EnumChatFormatting white = EnumChatFormatting.WHITE;
        EnumChatFormatting gold = EnumChatFormatting.GOLD;
        ChatLogger.createInstance(white + "[" + gold + modID + white + "] ");

        SettingsConfigurationAdapter.createInstance(event.getSuggestedConfigurationFile(), initMasterSettings());
    }

    @Mod.EventHandler
    public void FMLInit(FMLInitializationEvent event)
    {
        String asset = StaticAssetUtils.getStaticHostedAsset(modID + ".json");
        System.out.println("asset req finished: " + asset);

        // TODO: pass asset in here
        UpdateHandler.createInstance(modID, version);
//        UpdateHandler.INSTANCE.start();

        // TODO: is it okay for all this to be in here, dependant on github api call finish?
        //  try registering a keybind on some other key press for example (arbitrary time) - does it show up in controls menu?
        //  same for something like creating the gui and command to open the gui?
        KeyBinding keyBinding = new KeyBinding("<description>", Keyboard.KEY_NONE, modID);
        ClientRegistry.registerKeyBinding(keyBinding);
        MinecraftForge.EVENT_BUS.register(new Events(keyBinding));

//        System.out.println(Config.INSTANCE.getConfigCategorySettingsMap());

        AbstractGuiScreen guiScreen = new SettingsGuiScreen(SettingsConfigurationAdapter.INSTANCE.getDefaultCategorySettings());
        ClientCommandHandler.instance.registerCommand(new CommandOpenSettingsGui(modID, guiScreen));
    }

    private Settings initMasterSettings()
    {
        Settings settings = new Settings();
        settings.put("DRAW_BACKGROUND", new BoolSetting(true));
        settings.put("BACKGROUND_OPACITY", new DoubleSetting(66D));
        settings.put("RENDER_RANGE", new IntArrSetting(new int[]{1, 75}));
        settings.put("BACKGROUND_COLOR", new StringSetting("BLUE"));
        settings.put("TEST2", new IntSetting(30));
        return settings;
    }
}
