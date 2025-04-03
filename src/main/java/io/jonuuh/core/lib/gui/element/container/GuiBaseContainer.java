package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class GuiBaseContainer extends GuiContainer
{
    public GuiBaseContainer(String elementName, float xPos, float yPos, float width, float height, float outerRadius, float innerRadius, Map<GuiColorType, Color> colorMap)
    {
        super(elementName, xPos, yPos, width, height, outerRadius, innerRadius, colorMap);
    }

    public GuiBaseContainer(String elementName, float xPos, float yPos, float width, float height, float outerRadius, float innerRadius)
    {
        this(elementName, xPos, yPos, width, height, outerRadius, innerRadius, null);
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), getWidth(), getHeight(), innerRadius, getColor(GuiColorType.BACKGROUND), true);
    }
}
