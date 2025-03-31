package io.jonuuh.core.lib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.Collection;

public final class ChatLogger
{
    public static ChatLogger INSTANCE;
    private final Minecraft mc;
    private final EnumChatFormatting mainColor;
    private final EnumChatFormatting accentColor;
    private final IChatComponent header;

    public static void createInstance(String headerStr, EnumChatFormatting mainColor, EnumChatFormatting accentColor)
    {
        if (INSTANCE != null)
        {
            throw new IllegalStateException("ChatLogger instance has already been created");
        }
        INSTANCE = new ChatLogger(headerStr, mainColor, accentColor);
    }

    private ChatLogger(String headerStr, EnumChatFormatting mainColor, EnumChatFormatting accentColor)
    {
        this.mc = Minecraft.getMinecraft();
        this.mainColor = mainColor;
        this.accentColor = accentColor;
        this.header = new ChatComponentText(accentColor + "[" + mainColor + headerStr + accentColor + "] ");
    }

    public void addLog(String log)
    {
        addLog(log, EnumChatFormatting.WHITE);
    }

    public void addLog(String log, EnumChatFormatting color)
    {
        addLog(log, new ChatStyle().setColor(color));
    }

    public void addLog(String log, ChatStyle chatStyle)
    {
        addLog(new ChatComponentText(log).setChatStyle(chatStyle));
    }

    public void addLog(IChatComponent chatComponent)
    {
        addLog(chatComponent, true);
    }

    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////

    public void addCenteredLog(String log)
    {
        addCenteredLog(new ChatComponentText(log));
    }

    public void addCenteredLog(String log, ChatStyle logStyle)
    {
        addCenteredLog(new ChatComponentText(log).setChatStyle(logStyle));
    }

    public void addCenteredLog(IChatComponent chatComponent)
    {
        String padding = getPaddingToCenter(chatComponent.getUnformattedText(), ' ');
        addPaddedLog(chatComponent, new ChatComponentText(padding));
    }

    private void addPaddedLog(IChatComponent chatComponent, IChatComponent paddingComponent)
    {
        IChatComponent paddedComponent = paddingComponent.createCopy().appendSibling(chatComponent).appendSibling(paddingComponent);
        addLog(paddedComponent, false);
    }

    public void addTitleLog(String title, ChatStyle titleStyle, EnumChatFormatting barColor)
    {
        if (!barColor.isColor())
        {
            barColor = EnumChatFormatting.WHITE;
        }

        IChatComponent titleComponent = new ChatComponentText(title).setChatStyle(titleStyle.setStrikethrough(false));

        String padding = getPaddingToCenter(title, ' ');
        ChatStyle barStyle = new ChatStyle().setColor(barColor).setStrikethrough(true);
        IChatComponent barComponent = new ChatComponentText(padding).setChatStyle(barStyle);

        addPaddedLog(titleComponent, barComponent);
    }

    public void addBarLog(EnumChatFormatting barColor)
    {
        if (!barColor.isColor())
        {
            barColor = EnumChatFormatting.WHITE;
        }

        String padding = getPaddingToCenter("", ' ');
        ChatStyle barStyle = new ChatStyle().setColor(barColor).setStrikethrough(true);

        addLog(new ChatComponentText(padding + padding).setChatStyle(barStyle), false);
    }

    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////

    public void addStrLogs(Collection<String> logs)
    {
        logs.forEach(this::addLog);
    }

    public void addComponentLogs(Collection<IChatComponent> logs)
    {
        logs.forEach(this::addLog);
    }

    public void addCenteredStrLogs(Collection<String> logs)
    {
        logs.forEach(this::addCenteredLog);
    }

    public void addCenteredComponentLogs(Collection<IChatComponent> logs)
    {
        logs.forEach(this::addCenteredLog);
    }

    public void addFancyLogsBox(Collection<IChatComponent> content, String title)
    {
        addFancyLogsBox(content, title, mainColor, accentColor);
    }

    public void addFancyLogsBox(Collection<IChatComponent> content, String title, EnumChatFormatting titleColor, EnumChatFormatting barColor)
    {
        addTitleLog(title, new ChatStyle().setColor(titleColor), barColor);
        content.forEach(this::addCenteredLog);
        addBarLog(barColor);
    }

    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////

    private void addLog(IChatComponent chatComponent, boolean doHeader)
    {
        EntityPlayerSP player = mc.thePlayer;

        if (player != null)
        {
            player.addChatMessage(doHeader ? header.createCopy().appendSibling(chatComponent) : chatComponent);
        }
    }

    // TODO: doesn't work sometimes with even #'ed logs? nothing can be done?
    private String getPaddingToCenter(String text, Character paddingChar)
    {
        int chatWidth = mc.ingameGUI.getChatGUI().getChatWidth();
        int logWidth = mc.fontRendererObj.getStringWidth(text);
        int paddingCharWidth = mc.fontRendererObj.getStringWidth(String.valueOf(paddingChar));

        if (logWidth >= chatWidth)
        {
            return "";
        }

        char[] padding = new char[((chatWidth - logWidth) / paddingCharWidth) / 2];
        Arrays.fill(padding, paddingChar);
        return new String(padding);
    }
}
