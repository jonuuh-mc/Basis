package io.jonuuh.core.lib.config.gui.event;

import io.jonuuh.core.lib.config.gui.elements.GuiElement;

import java.util.ArrayList;
import java.util.List;

public class MouseDownEvent extends GuiEvent
{
    public final int mouseX;
    public final int mouseY;
    public final int mouseEventButton;
    public final List<GuiElement> mouseDownElements = new ArrayList<>();

    public MouseDownEvent(int mouseX, int mouseY, int mouseEventButton)
    {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.mouseEventButton = mouseEventButton;
    }

    public boolean hasHits()
    {
        return !mouseDownElements.isEmpty();
    }

    public void collectMouseDownElement(GuiElement element)
    {
        mouseDownElements.add(element);
    }
}
