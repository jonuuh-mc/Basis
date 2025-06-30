package io.jonuuh.core.local;

import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.single.IntSetting;
import io.jonuuh.core.lib.config.setting.types.single.StringSetting;
import io.jonuuh.core.lib.gui.CoreGuiScreen;
import io.jonuuh.core.lib.gui.screen.MainGuiScreen;
import io.jonuuh.core.lib.util.logging.ChatLoggerManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.modID, version = Main.version, acceptedMinecraftVersions = "[1.8.9]")
public class Main
{
    public static final String modID = "core";
    public static final String modName = "Core";
    public static final String version = "0.0.1";

    @Mod.EventHandler
    public void FMLPreInit(FMLPreInitializationEvent event)
    {
        ChatLoggerManager.putLogger(modName, EnumChatFormatting.GOLD, EnumChatFormatting.WHITE);
//        ConfigManager.putAdapter(modID, event.getSuggestedConfigurationFile(), initMasterSettings());
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

//        Settings settings = ConfigManager.getAdapter(modID).getDefaultCategorySettings();
        CoreGuiScreen guiScreen = new MainGuiScreen(null);
//        MinecraftForge.EVENT_BUS.register(guiScreen);
        ClientCommandHandler.instance.registerCommand(new CommandOpenSettingsGui(modID, guiScreen));
    }

    private Settings initMasterSettings()
    {
        Settings settings = new Settings(modID);
        settings.put(LocalSettingKey.FLEX_DIRECTION, new StringSetting("ROW"));
        settings.put(LocalSettingKey.FLEX_JUSTIFY, new StringSetting("START"));
        settings.put(LocalSettingKey.FLEX_ALIGN, new StringSetting("START"));

        settings.put(LocalSettingKey.FLEX_GROW, new IntSetting(0));
        settings.put(LocalSettingKey.FLEX_SHRINK, new IntSetting(1));
        settings.put(LocalSettingKey.FLEX_ALIGN_SELF, new StringSetting("START"));

        settings.put(LocalSettingKey.FLEX_BASIS, new IntSetting(75));
//        settings.put(LocalSettingKey.DRAW_BACKGROUND, new BoolSetting());
//        settings.put(LocalSettingKey.BORDER_OPACITY, new DoubleSetting(75.0));
//        settings.put(LocalSettingKey.RENDER_RANGE, new IntArrSetting(new int[]{5,50}));
//        settings.put(LocalSettingKey.BACKGROUND_COLOR, new StringSetting("#FFAA00"));
//        settings.put(LocalSettingKey.MAX_DETECTION_ANGLE, new IntSetting(45));
        return settings;
    }
}
