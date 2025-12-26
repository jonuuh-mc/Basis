package io.jonuuh.basis.v000309.lib.util.logging;

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
    private final Minecraft mc;
    private final IChatComponent header;
    public final EnumChatFormatting mainColor;
    public final EnumChatFormatting accentColor;
    private Level level;

    ChatLogger(String headerStr, EnumChatFormatting mainColor, EnumChatFormatting accentColor)
    {
        this.mc = Minecraft.getMinecraft();
        this.mainColor = mainColor;
        this.accentColor = accentColor;
        this.header = new ChatComponentText(accentColor + "[" + mainColor + headerStr + accentColor + "] ");
        setLevel(Level.ERROR);
    }

    ChatLogger(String headerStr)
    {
        this(headerStr, EnumChatFormatting.WHITE, EnumChatFormatting.GRAY);
    }

    public Level getLevel()
    {
        return level;
    }

    public void setLevel(Level level)
    {
        this.level = level;
    }

    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////

    public void addLog(int level, String log)
    {
        addLog(level, log, EnumChatFormatting.WHITE);
    }

    public void addSuccessLog(int level, String log)
    {
        addLog(level, log, EnumChatFormatting.GREEN);
    }

    public void addFailureLog(int level, String log)
    {
        addLog(level, log, EnumChatFormatting.RED);
    }

    public void addLog(int level, String log, EnumChatFormatting color)
    {
        addLog(level, new ChatComponentText(color + log));
    }

    public void addLog(int level, IChatComponent chatComponent)
    {
        addLog(level, chatComponent, true);
    }

    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////

    public void addCenteredLog(int level, String log)
    {
        addCenteredLog(level, new ChatComponentText(log));
    }

    public void addCenteredLog(int level, String log, EnumChatFormatting color)
    {
        addCenteredLog(level, new ChatComponentText(color + log));
    }

    public void addCenteredLog(int level, IChatComponent chatComponent)
    {
        String padding = getPaddingToCenter(chatComponent.getUnformattedText(), ' ');
        addPaddedLog(level, chatComponent, new ChatComponentText(padding));
    }

    public void addTitleLog(int level, String title, ChatStyle titleStyle, EnumChatFormatting barColor)
    {
        if (!barColor.isColor())
        {
            barColor = EnumChatFormatting.WHITE;
        }

        IChatComponent titleComponent = new ChatComponentText(title).setChatStyle(titleStyle.setStrikethrough(false));

        String padding = getPaddingToCenter(title, ' ');
        ChatStyle barStyle = new ChatStyle().setColor(barColor).setStrikethrough(true);
        IChatComponent barComponent = new ChatComponentText(padding).setChatStyle(barStyle);

        addPaddedLog(level, titleComponent, barComponent);
    }

    public void addBarLog(int level, EnumChatFormatting barColor)
    {
        if (!barColor.isColor())
        {
            barColor = EnumChatFormatting.WHITE;
        }

        String padding = getPaddingToCenter("", ' ');
        ChatStyle barStyle = new ChatStyle().setColor(barColor).setStrikethrough(true);
        IChatComponent barComponent = new ChatComponentText(padding).setChatStyle(barStyle);

        addPaddedLog(level, new ChatComponentText(""), barComponent);
    }

    private void addPaddedLog(int level, IChatComponent chatComponent, IChatComponent paddingComponent)
    {
        // This and the +4 to logWidth in getPaddingToCenter is for some minor padding to the left side of the log
        // e.g. for bar logs the bar will not be touching the left edge of the chat
        IChatComponent paddedComponent = new ChatComponentText(" ");

        paddedComponent.appendSibling(paddingComponent.createCopy()).appendSibling(chatComponent).appendSibling(paddingComponent);

//        int logWidth = mc.fontRendererObj.getStringWidth(chatComponent.getUnformattedText());
//////        int chatWidth = mc.ingameGUI.getChatGUI().getChatWidth();
////
//        System.out.println(chatComponent.getUnformattedText() + " :" + logWidth
//                + " " + mc.fontRendererObj.getStringWidth(paddingComponent.getUnformattedText()));

//        paddedComponent.appendSibling(paddingComponent);

//        if (logWidth % 2 == 0)
//        {
//            paddedComponent.appendSibling(new ChatComponentText(paddingComponent.getUnformattedText().substring(1))
//                .setChatStyle(paddingComponent.getChatStyle()));
//        }

//        // If the chatComponent could not be perfectly centered in the chat (e.g. chatComponent is an odd # of chars),
//        // Add additional padding chars to the right side of the log so the padding (e.g. a bar log) covers the whole chat width
//        while (logWidth < chatWidth)
//        {
//            String singleCharPadding = String.valueOf(paddingComponent.getUnformattedText().charAt(0));
//            paddedComponent.appendSibling(new ChatComponentText(singleCharPadding).setChatStyle(paddingComponent.getChatStyle()));
//            logWidth = mc.fontRendererObj.getStringWidth(paddedComponent.getUnformattedText());
//        }

        addLog(level, paddedComponent, false);
    }

    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////

    public void addStrLogs(int level, Collection<String> logs)
    {
        logs.forEach(log -> addLog(level, log));
    }

    public void addComponentLogs(int level, Collection<IChatComponent> logs)
    {
        logs.forEach(log -> addLog(level, log));
    }

    public void addCenteredStrLogs(int level, Collection<String> logs)
    {
        logs.forEach(log -> addCenteredLog(level, log));
    }

    public void addCenteredComponentLogs(int level, Collection<IChatComponent> logs)
    {
        logs.forEach(log -> addCenteredLog(level, log));
    }

    public void addFancyLogsBox(int level, Collection<IChatComponent> content, String title)
    {
        addFancyLogsBox(level, content, title, mainColor, accentColor);
    }

    /**
     * Ideally the title's number of default width (5) chars should be divisible by 4 for its component to render as the exact same width as the bottom bar of the box.
     * <br>Trying to explain exactly why this is the case is really hard because I don't know EXACTLY why this is myself.
     * <br>Also if the title contains any non-default width chars e.g. an '!' or '@' this all goes out the window.
     * <br>Would be really convenient if all characters could somehow be treated as having the same width.
     */
    public void addFancyLogsBox(int level, Collection<IChatComponent> content, String title, EnumChatFormatting titleColor, EnumChatFormatting barColor)
    {
        addTitleLog(level, title, new ChatStyle().setColor(titleColor), barColor);
        content.forEach(log -> addCenteredLog(level, log));
        addBarLog(level, barColor);
    }

    //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ////

    public void addLog(int level, IChatComponent chatComponent, boolean doHeader)
    {
        EntityPlayerSP player = mc.thePlayer;

        if (player != null && level <= this.level.intLevel)
        {
            player.addChatMessage(doHeader ? header.createCopy().appendSibling(chatComponent) : chatComponent);
        }
    }

    private String getPaddingToCenter(String text, Character paddingChar)
    {
        int chatWidth = mc.ingameGUI.getChatGUI().getChatWidth();
        int logWidth = mc.fontRendererObj.getStringWidth(text) + 4;
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
