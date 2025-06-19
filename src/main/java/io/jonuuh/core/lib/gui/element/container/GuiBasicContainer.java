package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;

import java.util.Map;

public class GuiBasicContainer extends GuiContainer
{
    public GuiBasicContainer(String elementName, float xPos, float yPos, float width, float height, Map<GuiColorType, Color> colorMap)
    {
        super(elementName, xPos, yPos, width, height, colorMap);
    }

    public GuiBasicContainer(String elementName, float xPos, float yPos, float width, float height)
    {
        this(elementName, xPos, yPos, width, height, null);
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        RenderUtils.drawRectangle(worldXPos(), worldYPos(), getWidth(), getHeight(), getColor(GuiColorType.BACKGROUND));
    }

    public static class Builder extends GuiContainer.AbstractBuilder<Builder, GuiBasicContainer>
    {
        public Builder(String elementName)
        {
            super(elementName);
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiBasicContainer build()
        {
            return new GuiBasicContainer(this);
        }
    }
}
