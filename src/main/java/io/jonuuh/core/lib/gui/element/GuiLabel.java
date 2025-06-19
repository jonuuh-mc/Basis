package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.properties.GuiColorType;
import net.minecraft.client.gui.FontRenderer;

public class GuiLabel extends GuiElement
{
    protected final FontRenderer fontRenderer;
    protected String text;
    protected boolean centerStrInWidth;
    protected boolean centerStrInHeight;
    protected boolean fitWidthToStr;
    protected boolean fitHeightToStr;
    protected float textXPos;
    protected float textYPos;

    public GuiLabel(String elementName, float xPos, float yPos, float width, float height, String text)
    {
        super(elementName, xPos, yPos, width, height);
        this.fontRenderer = mc.fontRendererObj;
        this.text = text;
        centerStrInWidth = centerStrInHeight = true;
    }

    public GuiLabel(String elementName, float xPos, float yPos, String text)
    {
        super(elementName, xPos, yPos);
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

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        textXPos = worldXPos();
        textYPos = worldYPos();

        if (centerStrInWidth)
        {
            textXPos = worldXPos() + (getWidth() / 2) - (fontRenderer.getStringWidth(text) / 2F);
        }
        else if (fitWidthToStr)
        {
            setWidth(fontRenderer.getStringWidth(text));
        }

        if (centerStrInHeight)
        {
            textYPos = worldYPos() + (getHeight() / 2) - ((fontRenderer.FONT_HEIGHT - 1) / 2F);
        }
        else if (fitHeightToStr)
        {
            setHeight(fontRenderer.FONT_HEIGHT - 1);
        }

        fontRenderer.drawString(text, textXPos, textYPos, getColor(GuiColorType.BASE).toPackedARGB(), true);
    }

    public static class Builder extends GuiElement.AbstractBuilder<Builder, GuiLabel>
    {
        protected String text;

        public Builder(String elementName)
        {
            super(elementName);
        }

        public Builder text(String text)
        {
            this.text = text;
            return self();
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiLabel build()
        {
            return new GuiLabel(this);
        }
    }
}
