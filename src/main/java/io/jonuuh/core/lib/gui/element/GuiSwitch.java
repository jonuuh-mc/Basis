package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;

public class GuiSwitch extends GuiToggle
{
    // TODO: make vertical option
    public GuiSwitch(String elementName, float xPos, float yPos, float width, float height, boolean switchState)
    {
        super(elementName, xPos, yPos, width, height, switchState);
    }

    public GuiSwitch(String elementName, float xPos, float yPos, boolean switchState)
    {
        this(elementName, xPos, yPos, DEFAULT_HEIGHT * 2, DEFAULT_HEIGHT, switchState);
    }

    public GuiSwitch(String elementName, float xPos, float yPos)
    {
        this(elementName, xPos, yPos, false);
    }

    public float getPointerSize()
    {
        return getHeight() - 2F;
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }

        super.onScreenDraw(mouseX, mouseY, partialTicks);

        float padding = ((getHeight() - getPointerSize()) / 2F);
        float pointerX = isToggled() ? (worldXPos() + getWidth() - getPointerSize() - padding) : worldXPos() + padding;
        Color trackColor = isToggled() ? getColor(GuiColorType.BASE) : getColor(GuiColorType.ACCENT2);

        // Track
        RenderUtils.drawRectangle(worldXPos(), worldYPos(), getWidth(), getHeight(), trackColor);
        // Pointer
        RenderUtils.drawRectangle(pointerX, worldYPos() + padding, getPointerSize(), getPointerSize(), getColor(GuiColorType.ACCENT1));
    }
}

