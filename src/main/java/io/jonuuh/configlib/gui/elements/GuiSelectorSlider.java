package io.jonuuh.configlib.gui.elements;

import io.jonuuh.configlib.gui.ISettingsGui;
import net.minecraft.client.Minecraft;

import java.util.List;

public class GuiSelectorSlider<T> extends GuiIntSlider
{
    protected final List<T> selectorElements;

    public GuiSelectorSlider(ISettingsGui parent, int xPos, int yPos, int width, int height, float radius, List<T> selectorElements, T startIndex)
    {
        super(parent, xPos, yPos, width, height, radius, 0, selectorElements.size() - 1, 0);
        this.selectorElements = selectorElements;
        setSelectedElement(startIndex);
        updateTooltip();
    }

    public GuiSelectorSlider(ISettingsGui parent, int xPos, int yPos, List<T> selectorElements, T startIndex)
    {
        super(parent, xPos, yPos, 0, selectorElements.size() - 1, 0);
        this.selectorElements = selectorElements;
        setSelectedElement(startIndex);
        updateTooltip();
    }

    public GuiSelectorSlider(ISettingsGui parent, int xPos, int yPos, List<T> selectorElements)
    {
        super(parent, xPos, yPos, 0, selectorElements.size() - 1);
        this.selectorElements = selectorElements;
        updateTooltip();
    }

//    @Override
//    public void setValue(double value)
//    {
//        normalizedVal = (value - min) / (max - min);
//        clamp();
//
//        if (this.selectorElements != null)
//        {
//            updateTooltip();
//        }
//    }

    public T getSelectedElement()
    {
        return selectorElements.get(getValueInt());
    }

    public void setSelectedElement(T element)
    {
        int index = selectorElements.indexOf(element);

        if (index != -1)
        {
            super.setValue(index);
        }
    }

    @Override
    protected void updateTooltip()
    {
        if (this.selectorElements == null)
        {
            return;
        }

        String str = getSelectedElement().toString();
        int strWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(str) - 1;

        tooltip = new Tooltip(str);
        tooltip.x = getPointerXCenter() - (strWidth / 2F);
    }
}
