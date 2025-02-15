package io.jonuuh.core.lib.config.gui.elements.interactable;

import io.jonuuh.core.lib.config.gui.elements.GuiContainer;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

public abstract class GuiButton extends GuiInteractableElement
{
    public GuiButton(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, String tooltipStr)
    {
        super(parent, elementName, xPos, yPos, width, height, tooltipStr);
    }

    public GuiButton(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height)
    {
        super(parent, elementName, xPos, yPos, width, height);
    }

    public GuiButton(GuiContainer parent, String elementName, int xPos, int yPos)
    {
        super(parent, elementName, xPos, yPos);
    }

    @Override
    protected void drawElement(int mouseX, int mouseY, float partialTicks)
    {
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, yPos, width, height, 3, baseColor, true);

        String buttonText = RenderUtils.trimStringToWidthWithEllipsis("text", this.width);
        mc.fontRendererObj.drawString(buttonText, xPos + width / 2, yPos + (height - 8) / 2, (int) parent.getBaseColor().toDecimalARGB());
    }

    @Override
    public void onMousePress(int mouseX, int mouseY)
    {
        super.onMousePress(mouseX, mouseY);
        yPos += 1;
    }

    @Override
    public void onMouseRelease(int mouseX, int mouseY)
    {
        super.onMouseRelease(mouseX, mouseY);
        yPos = yPosInit;
    }
}
