package io.jonuuh.core.lib.config.gui.elements.interactable.sliders.old;

import io.jonuuh.core.lib.config.gui.elements.GuiContainer;
import io.jonuuh.core.lib.config.setting.types.array.DoubleArrSetting;
import io.jonuuh.core.lib.config.setting.types.single.DoubleSetting;
import io.jonuuh.core.lib.util.MathUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class GuiDoubleSlider extends AbstractGuiSlider<Double>
{
    public GuiDoubleSlider(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, double min, double max, Double[] startValues, boolean isVertical)
    {
        super(parent, elementName, xPos, yPos, width, height, min, max, startValues, isVertical);
    }

    public GuiDoubleSlider(GuiContainer parent, String elementName, int xPos, int yPos, double min, double max, Double[] startValues)
    {
        super(parent, elementName, xPos, yPos, min, max, startValues);
    }

    public GuiDoubleSlider(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, double min, double max, Double startValue, boolean isVertical)
    {
        this(parent, elementName, xPos, yPos, width, height, min, max, new Double[]{startValue}, isVertical);
    }

    public GuiDoubleSlider(GuiContainer parent, String elementName, int xPos, int yPos, double min, double max, Double startValue)
    {
        this(parent, elementName, xPos, yPos, min, max, new Double[]{startValue});
    }

    @Override
    public Double getValue(int pointerIndex)
    {
        return MathUtils.denormalize(getNormalizedValue(pointerIndex), min, max);
    }

    @Override
    public void setValue(int pointerIndex, Double value)
    {
        setNormalizedValue(pointerIndex, MathUtils.normalize(value, min, max));
    }

    @Override
    public Double[] getValues()
    {
        Double[] values = new Double[normalPointerValues.length];

        for (int i = 0; i < values.length; i++)
        {
            values[i] = getValue(i);
        }
        return values;
    }

    @Override
    public void setValues(Double[] values)
    {
        normalPointerValues = new double[values.length];

        Arrays.sort(values);
        for (int i = 0; i < values.length; i++)
        {
            normalPointerValues[i] = MathUtils.clamp(MathUtils.normalize(values[i], min, max));
        }
    }

    @Override
    protected void updateSetting()
    {
        if (getNumPointers() == 1)
        {
            ((DoubleSetting) associatedSetting).setValue(getValue(0));
        }
        else
        {
            // TODO: bad design, dont know how to fix (generics cant be primitive, making the generic a primitive array makes getvalue/getvalues problematic)
            ((DoubleArrSetting) associatedSetting).setValue(ArrayUtils.toPrimitive(getValues()));
        }
    }
}
