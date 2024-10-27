package io.jonuuh.core.lib.config.gui.elements;

import io.jonuuh.core.lib.util.RenderUtils;
import io.jonuuh.core.lib.config.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.core.lib.util.Color;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiTooltip
{
    private static GuiTooltip instance;
    private final Minecraft mc;
    public GuiInteractableElement currentElement;

    public String str;
    public Color color;
    public float posX;
    public float posY;

    public float padding;
    public float rectWidth;
    public float rectHeight;
    public float triWidth;
    public float triHeight;

    public static void createInstance(Minecraft mc)
    {
        if (instance != null)
        {
            throw new IllegalStateException("GuiTooltip instance has already been created");
        }
        instance = new GuiTooltip(mc);
    }

    public static GuiTooltip getInstance()
    {
        if (instance == null)
        {
            throw new NullPointerException("GuiTooltip instance has not been created");
        }
        return instance;
    }

    public static void claim(GuiInteractableElement currentElement)
    {
        instance.claimForElement(currentElement);
    }

    public static void draw()
    {
        instance.drawCurrent();
    }

    public static boolean isClaimed()
    {
        return instance.currentElement != null;
    }

    private GuiTooltip(Minecraft mc)
    {
        this.mc = mc;
    }

    private void claimForElement(GuiInteractableElement currentElement)
    {
        if (/*isClaimed() && */ currentElement.getTooltipStr().isEmpty())
        {
            return;
        }

//        System.out.println("SETTING tooltip for: " + currentElement);
        this.currentElement = currentElement;

        this.str = currentElement.getTooltipStr();

        int strWidth = mc.fontRendererObj.getStringWidth(str) - 1;

        this.padding = 2F;

        this.rectWidth = strWidth + (padding * 2);
        this.rectHeight = (mc.fontRendererObj.FONT_HEIGHT - 1) + (padding * 2);

        this.triWidth = 6;
        this.triHeight = 3;

        this.posX = currentElement.getXPos() - (strWidth / 2F) + (currentElement.getWidth() / 2F);
        this.posY = currentElement.getYPos() + padding - rectHeight - triHeight - 2; // -2 somewhat arbitrary

        this.color = currentElement.getColorMap().get("BASE") /*Color.getRandom()*/;
//        return true;
    }

    private void drawCurrent()
    {
//        System.out.println("drawing tooltip for: " + currentElement);

        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, posX - padding, posY - padding, rectWidth, rectHeight, 2, color, true);

        RenderUtils.drawTriangle(GL11.GL_POLYGON, posX - padding + (rectWidth / 2F) - (triWidth / 2F), posY - padding + rectHeight, triWidth, triHeight, color, 0);

        mc.fontRendererObj.drawString(str, posX, posY, -1, false);

        currentElement = null;
    }
}
