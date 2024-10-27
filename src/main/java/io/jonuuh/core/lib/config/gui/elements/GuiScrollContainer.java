package io.jonuuh.core.lib.config.gui.elements;

import java.util.List;

public class GuiScrollContainer extends GuiElement
{
    List<GuiElement> children;

    public GuiScrollContainer(int xPos, int yPos, int width, int height, List<GuiElement> children)
    {
        super(xPos, yPos, width, height, true);
        this.children = children;
    }
}
