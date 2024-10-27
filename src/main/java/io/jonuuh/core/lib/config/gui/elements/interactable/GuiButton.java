package io.jonuuh.core.lib.config.gui.elements.interactable;

import io.jonuuh.core.lib.config.gui.ISettingsGui;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiButton extends GuiInteractableElement
{
    public GuiButton(ISettingsGui parent, int xPos, int yPos, int width, int height, String tooltipStr)
    {
        super(parent, xPos, yPos, width, height, tooltipStr);
    }

    public GuiButton(ISettingsGui parent, int xPos, int yPos, int width, int height)
    {
        super(parent, xPos, yPos, width, height);
    }

    public GuiButton(ISettingsGui parent, int xPos, int yPos)
    {
        super(parent, xPos, yPos);
    }

    @Override
    public void onScreenDraw(Minecraft mc, int mouseX, int mouseY)
    {
        super.onScreenDraw(mc, mouseX, mouseY);
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, yPos, width, height, 3, colorMap.get("BASE"), true);

        String buttonText = "text";
        int strWidth = mc.fontRendererObj.getStringWidth(buttonText);
        int ellipsisWidth = mc.fontRendererObj.getStringWidth("...");
        if (strWidth > this.width - 6 && strWidth > ellipsisWidth)
        {
            buttonText = mc.fontRendererObj.trimStringToWidth(buttonText, this.width - 6 - ellipsisWidth).trim() + "...";
        }

        mc.fontRendererObj.drawString(buttonText, xPos + width / 2, yPos + (height - 8) / 2, (int) colorMap.get("BASE").toDecimalARGB());
    }

    @Override
    public void onMousePress(int mouseX, int mouseY)
    {
       super.onMousePress(mouseX, mouseY);
       yPos += 1; // affects hovered text color only?

        sendChangeToParent();
    }

    @Override
    public void onMouseRelease(int mouseX, int mouseY)
    {
        super.onMouseRelease(mouseX, mouseY);
        yPos = yPosInit;
    }
}
