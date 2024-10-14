//package io.jonuuh.updatechecker;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.event.ClickEvent;
//import net.minecraft.util.ChatComponentText;
//import net.minecraft.util.ChatStyle;
//import net.minecraft.util.EnumChatFormatting;
//import net.minecraft.util.IChatComponent;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.event.entity.EntityJoinWorldEvent;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class UpdateNotificationHandler
//{
//    private static UpdateNotificationHandler instance;
//    private final Minecraft mc;
////    private final Settings settings;
//    private final UpdateChecker updateChecker;
//    private final List<IChatComponent> newUpdateNotification;
//
//    public static UpdateNotificationHandler createInstance()
//    {
//        if (instance == null)
//        {
//            instance = new UpdateNotificationHandler();
//            return instance;
//        }
//        else
//        {
//            throw new IllegalStateException("UpdateNotificationHandler instance has already been created");
//        }
//    }
//
//    private UpdateNotificationHandler()
//    {
//        this.mc = Minecraft.getMinecraft();
////        this.settings = Settings.getInstance();
//        this.updateChecker = UpdateChecker.getInstance();
//        this.newUpdateNotification = new ArrayList<>();
//
//        IChatComponent openUrlChatComponent = new ChatComponentText("[Download]");
//        ClickEvent clickEventOpenUrl = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/jonuuh/MWCompass/releases/latest");
//        ChatStyle chatStyleOpenUrl = new ChatStyle().setChatClickEvent(clickEventOpenUrl).setColor(EnumChatFormatting.BLUE).setUnderlined(true);
//        openUrlChatComponent.setChatStyle(chatStyleOpenUrl);
//
//        IChatComponent remindMeChatComponent = new ChatComponentText("      [Don't remind me again]");
//        ClickEvent clickEventCmd = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mwc ignoreUpdate true");
//        ChatStyle chatStyleCmd = new ChatStyle().setChatClickEvent(clickEventCmd).setColor(EnumChatFormatting.GRAY).setUnderlined(false).setItalic(true);
//        remindMeChatComponent.setChatStyle(chatStyleCmd);
//
//        IChatComponent mainComponent = new ChatComponentText("Update " + updateChecker.getLatestVersionStr() + " available").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
//
//        this.newUpdateNotification.add(mainComponent);
//        this.newUpdateNotification.add(openUrlChatComponent.appendSibling(remindMeChatComponent));
//    }
//
//    @SubscribeEvent
//    public void onEntityJoinWorld(EntityJoinWorldEvent event)
//    {
//        if (event.entity != mc.thePlayer)
//        {
//            return;
//        }
//
//        // This class does nothing after player joins the world once (can't be g.collected bc static?)
//        MinecraftForge.EVENT_BUS.unregister(instance);
////        ChatLogger.addLog("First world join of session");
//
//        String trueLatestVersion = updateChecker.getLatestVersionStr();
//
//        if (updateChecker.isUpdateAvailable())
//        {
//            // If the true latest version is different from the last recorded latest version (must be a newer version since updateIsAvailable):
//            // Reset join counter to 1 (incremented from 0 for current join), record true latest version, and post the new update notification
//            // Therefor the first join after every released update will post the new update notification
//            if (!trueLatestVersion.equals(settings.getLastRecordedLatestVersion()))
//            {
//                settings.setLastRecordedLatestVersion(trueLatestVersion);
//                settings.setFirstWorldJoinsCounter(1);
//                ChatLogger.addFancyLogsBox(newUpdateNotification, " MWCompass ", EnumChatFormatting.GRAY, EnumChatFormatting.DARK_GRAY);
//                return;
//            }
//
//            // If the player didn't click "Don't remind me again", post the notification again every 5 first joins
//            if (!settings.doIgnoreLatestUpdate())
//            {
//                if (settings.getFirstWorldJoinsCounter() == 5)
//                {
//                    settings.setFirstWorldJoinsCounter(0);
//                    ChatLogger.addFancyLogsBox(newUpdateNotification, " MWCompass ",EnumChatFormatting.GRAY, EnumChatFormatting.DARK_GRAY);
//                }
//                settings.setFirstWorldJoinsCounter(settings.getFirstWorldJoinsCounter() + 1);
//            }
//        }
//    }
//}
//
