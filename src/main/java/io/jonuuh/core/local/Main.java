package io.jonuuh.core.local;

import io.jonuuh.core.lib.config.Config;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.array.DoubleArrSetting;
import io.jonuuh.core.lib.config.setting.types.array.IntArrSetting;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.single.DoubleSetting;
import io.jonuuh.core.lib.config.setting.types.single.IntSetting;
import io.jonuuh.core.lib.config.setting.types.single.StringSetting;
import io.jonuuh.core.lib.util.ChatLogger;
import io.jonuuh.core.lib.util.Log4JLogger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
        Initializer initializer = new Initializer(modID, version);
        MinecraftForge.EVENT_BUS.register(initializer);
        initializer.start();
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
