package io.jonuuh.core.local;

import io.jonuuh.core.lib.gui.AbstractGuiScreen;
import io.jonuuh.core.lib.util.ChatLogger;
import io.jonuuh.core.lib.util.Log4JLogger;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.modID, version = Main.version, acceptedMinecraftVersions = "[1.8.9]")
public class Main
{
    public static final String modID = "core";
    public static final String version = "0.0.1";
    public static final String modName = "Core";

    @Mod.EventHandler
    public void FMLPreInit(FMLPreInitializationEvent event)
    {
        Log4JLogger.createInstance(modID);
        ChatLogger.createInstance(modName, EnumChatFormatting.GOLD, EnumChatFormatting.WHITE);

//        SettingsConfigurationAdapter.createInstance(event.getSuggestedConfigurationFile(), new ArrayList<>());
    }

    @Mod.EventHandler
    public void FMLInit(FMLInitializationEvent event)
    {
//        JsonObject jsonObject = StaticAssetUtils.getStaticHostedAssetAsJsonObject(modID + ".json");
//        System.out.println("asset req finished: " + jsonObject.toString());
//
//        new UpdateHandler(modID, modName, version, jsonObject);
//        ClientCommandHandler.instance.registerCommand(new CommandCore(modID));

//        KeyBinding keyBinding = new KeyBinding("", Keyboard.KEY_NONE, modName);
//        ClientRegistry.registerKeyBinding(keyBinding);
//        MinecraftForge.EVENT_BUS.register(new Events(keyBinding));

//        Settings settings = SettingsConfigurationAdapter.INSTANCE.getDefaultCategorySettings();
        AbstractGuiScreen guiScreen = new GuiScreenImpl(null);
//        MinecraftForge.EVENT_BUS.register(guiScreen);
        ClientCommandHandler.instance.registerCommand(new CommandOpenSettingsGui(modID, guiScreen));
    }

//    private Settings initMasterSettings()
//    {
//        Settings settings = new Settings();
//        settings.put(LocalSettingKey.FLEX_DIRECTION_MAIN, new BoolSetting());
//        settings.put(LocalSettingKey.FLEX_DIRECTION_REVERSE, new BoolSetting());
//        settings.put(LocalSettingKey.FLEX_BASIS, new IntSetting(75));
////        settings.put(LocalSettingKey.DRAW_BACKGROUND, new BoolSetting());
////        settings.put(LocalSettingKey.BORDER_OPACITY, new DoubleSetting(75.0));
////        settings.put(LocalSettingKey.RENDER_RANGE, new IntArrSetting(new int[]{5,50}));
////        settings.put(LocalSettingKey.BACKGROUND_COLOR, new StringSetting("#FFAA00"));
////        settings.put(LocalSettingKey.MAX_DETECTION_ANGLE, new IntSetting(45));
//        return settings;
//    }
}
