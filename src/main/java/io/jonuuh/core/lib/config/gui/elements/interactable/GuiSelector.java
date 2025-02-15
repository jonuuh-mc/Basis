package io.jonuuh.core.lib.config.gui.elements.interactable;

import io.jonuuh.core.lib.config.gui.elements.GuiContainer;
import io.jonuuh.core.lib.config.gui.elements.GuiScrollContainer;
import io.jonuuh.core.lib.util.Color;

import java.util.Map;

public class GuiSelector extends GuiScrollContainer
{
    public GuiSelector(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, Map<String, Color> colorMap, float outerRadius, float innerRadius, String tooltipStr, int sliderWindowHeight)
    {
        super(parent, elementName, xPos, yPos, width, height, colorMap, outerRadius, innerRadius, tooltipStr, sliderWindowHeight);
    }
}
