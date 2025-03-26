package io.jonuuh.core.lib.config.gui.elements.container;

import io.jonuuh.core.lib.config.gui.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class GuiBaseContainer extends GuiContainer
{
    public GuiBaseContainer(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, float outerRadius, float innerRadius, Map<GuiColorType, Color> colorMap)
    {
        super(parent, elementName, xPos, yPos, width, height, outerRadius, innerRadius, colorMap);
    }

    public GuiBaseContainer(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, float outerRadius, float innerRadius)
    {
        super(parent, elementName, xPos, yPos, width, height, outerRadius, innerRadius);
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, yPos, width, height, innerRadius, getColor(GuiColorType.BACKGROUND), true);
    }
}
