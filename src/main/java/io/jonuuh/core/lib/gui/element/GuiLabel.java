package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import net.minecraft.client.gui.FontRenderer;

public class GuiLabel extends GuiElement
{
    protected final FontRenderer fontRenderer;
    protected String text;
    protected boolean centerStrInWidth;
    protected boolean centerStrInHeight;
    protected boolean fitWidthToStr;
    protected boolean fitHeightToStr;
    protected int textXPos;
    protected int textYPos;

    public GuiLabel(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, String text)
    {
        super(parent, elementName, xPos, yPos, width, height);
        this.fontRenderer = mc.fontRendererObj;
        this.text = text;
        centerStrInWidth = centerStrInHeight = true;
    }

    public GuiLabel(GuiContainer parent, String elementName, int xPos, int yPos, String text)
    {
        super(parent, elementName, xPos, yPos);
        this.fontRenderer = mc.fontRendererObj;
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public GuiLabel setText(String text)
    {
        this.text = text;
        return this;
    }

    protected void onInitGui(int guiScreenWidth, int guiScreenHeight)
    {
        textXPos = worldXPos();
        textYPos = worldYPos();

        if (centerStrInWidth)
        {
            textXPos = worldXPos() + (width / 2) - (fontRenderer.getStringWidth(text) / 2);
        }
        else if (fitWidthToStr)
        {
            width = fontRenderer.getStringWidth(text);
        }

        if (centerStrInHeight)
        {
            textYPos = worldYPos() + (height / 2) - ((fontRenderer.FONT_HEIGHT - 1) / 2);
        }
        else if (fitHeightToStr)
        {
            height = fontRenderer.FONT_HEIGHT - 1;
        }
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        fontRenderer.drawString(text, textXPos, textYPos, getColor(GuiColorType.BASE).toPackedARGB(), true);
    }
}
