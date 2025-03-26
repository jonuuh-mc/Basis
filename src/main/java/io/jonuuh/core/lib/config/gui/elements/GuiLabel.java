package io.jonuuh.core.lib.config.gui.elements;

import io.jonuuh.core.lib.config.gui.GuiColorType;
import io.jonuuh.core.lib.config.gui.elements.container.GuiContainer;

public class GuiLabel extends GuiElement
{
    protected String text;
    protected boolean centeredX;
    protected boolean centeredY;

    public GuiLabel(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, String text)
    {
        super(parent, elementName, xPos, yPos, width, height);
        this.text = text;
        setCenteredX(true).setCenteredY(true);
    }

    public GuiLabel(GuiContainer parent, String elementName, int xPos, int yPos, String text)
    {
        super(parent, elementName, xPos, yPos);
        this.text = text;
        setCenteredX(true).setCenteredY(true);
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

    public boolean isCenteredX()
    {
        return centeredX;
    }

    public GuiLabel setCenteredX(boolean centeredX)
    {
        this.centeredX = centeredX;
        return this;
    }

    public boolean isCenteredY()
    {
        return centeredY;
    }

    public GuiLabel setCenteredY(boolean centeredY)
    {
        this.centeredY = centeredY;
        return this;
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        // TODO: need to account for str width/height which will be damn near impossible given scaling of text with gl11
        //  drawing text is the sole reason i need to find a more mc idiomatic way to scale gui on resize
//        float x = centeredX ? xPos + width / 2F : xPos;
//        float y = centeredY ? yPos + height / 2F : xPos;

        drawScaledString(text, xPos, yPos, getColor(GuiColorType.BASE).toDecimalARGB(), true);
    }
}
