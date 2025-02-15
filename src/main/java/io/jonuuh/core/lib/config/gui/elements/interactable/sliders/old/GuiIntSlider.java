package io.jonuuh.core.lib.config.gui.elements.interactable.sliders.old;

import io.jonuuh.core.lib.config.gui.elements.GuiContainer;
import io.jonuuh.core.lib.config.setting.types.array.IntArrSetting;
import io.jonuuh.core.lib.config.setting.types.single.IntSetting;
import io.jonuuh.core.lib.util.MathUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class GuiIntSlider extends AbstractGuiSlider<Integer>
{
    public GuiIntSlider(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, double min, double max, Integer[] startValues, boolean isVertical)
    {
        super(parent, elementName, xPos, yPos, width, height, min, max, startValues, isVertical);
    }

    public GuiIntSlider(GuiContainer parent, String elementName, int xPos, int yPos, double min, double max, Integer[] startValues)
    {
        super(parent, elementName, xPos, yPos, min, max, startValues);
    }

    public GuiIntSlider(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, double min, double max, Integer startValue, boolean isVertical)
    {
        this(parent, elementName, xPos, yPos, width, height, min, max, new Integer[]{startValue}, isVertical);
    }

    public GuiIntSlider(GuiContainer parent, String elementName, int xPos, int yPos, double min, double max, Integer startValue)
    {
        this(parent, elementName, xPos, yPos, min, max, new Integer[]{startValue});
    }

    @Override
    public Integer getValue(int pointerIndex)
    {
        return Math.round((float) MathUtils.denormalize(getNormalizedValue(pointerIndex), min, max));
    }

    @Override
    public void setValue(int pointerIndex, Integer value)
    {
        setNormalizedValue(pointerIndex, MathUtils.normalize(value, min, max));
    }

    @Override
    public Integer[] getValues()
    {
        Integer[] values = new Integer[normalPointerValues.length];

        for (int i = 0; i < values.length; i++)
        {
            values[i] = getValue(i);
        }
        return values;
    }

    @Override
    public void setValues(Integer[] values)
    {
        normalPointerValues = new double[values.length];

        Arrays.sort(values);
        for (int i = 0; i < values.length; i++)
        {
            double value = MathUtils.clamp(MathUtils.normalize(values[i], min, max));
            normalPointerValues[i] = roundNormalValue(value);
        }
    }

    @Override
    public void setNormalizedValue(int pointerIndex, double normalValue)
    {
        double value = clampBetweenAdjacents(pointerIndex, normalValue);
        normalPointerValues[pointerIndex] = roundNormalValue(value);
    }

    private double roundNormalValue(double normalValue)
    {
        //            float d = (float) (normalValue * max); // TODO: test with float (round will return int)
//            System.out.println("PRE:" + normalValue);
        normalValue = Math.round(normalValue * max) / max; // round (up?) to multiple (ex: 20 -> 0.05, 10 -> 0.1)
//            System.out.println("POST:" + normalValue);
        return normalValue;
    }

    @Override
    protected void updateSetting()
    {
        if (getNumPointers() == 1)
        {
            ((IntSetting) associatedSetting).setValue(getValue(0));
        }
        else
        {
            // TODO: bad design, dont know how to fix (generics cant be primitive, making the generic a primitive array makes getvalue/getvalues problematic)
            ((IntArrSetting) associatedSetting).setValue(ArrayUtils.toPrimitive(getValues()));
        }
    }
}
