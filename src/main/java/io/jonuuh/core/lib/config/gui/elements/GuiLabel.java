package io.jonuuh.core.lib.config.gui.elements;

public class GuiLabel extends GuiElement
{
    protected String text;

    protected GuiLabel(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, boolean visible, String text, String tooltipStr)
    {
        super(parent, elementName, xPos, yPos, width, height, visible, tooltipStr);
        this.text = text;
    }

    @Override
    protected void drawElement(int mouseX, int mouseY, float partialTicks)
    {
        mc.fontRendererObj.drawString(text, xPos, yPos, -1, false);
    }
}
