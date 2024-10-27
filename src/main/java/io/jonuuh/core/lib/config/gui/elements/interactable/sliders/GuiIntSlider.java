package io.jonuuh.core.lib.config.gui.elements.interactable.sliders;

import io.jonuuh.core.lib.config.gui.ISettingsGui;
import io.jonuuh.core.lib.util.MathUtils;

import java.util.Arrays;

public class GuiIntSlider extends GuiSlider
{
    public GuiIntSlider(ISettingsGui parent, int xPos, int yPos, int width, int height, int min, int max, int[] startValues)
    {
        super(parent, xPos, yPos, width, height, min, max, Arrays.stream(startValues).asDoubleStream().toArray());
    }

    public GuiIntSlider(ISettingsGui parent, int xPos, int yPos, int width, int height, int min, int max, int startValue)
    {
        super(parent, xPos, yPos, width, height, min, max, startValue);
//        setDecimalFormat(new DecimalFormat("#"));
    }

    public GuiIntSlider(ISettingsGui parent, int xPos, int yPos, int min, int max, int[] startValues)
    {
        super(parent, xPos, yPos, min, max, Arrays.stream(startValues).asDoubleStream().toArray());
    }

    public GuiIntSlider(ISettingsGui parent, int xPos, int yPos, int min, int max, int startValue)
    {
        super(parent, xPos, yPos, min, max, startValue);
    }

    //    @Override
//    public double getValue()
//    {
//        return Math.round(normalizedVal * (max - min) + min);
//    }
//
//    public int getValueInt()
//    {
//        return (int) getValue();
//    }
//
//    // Normalize TODO: is this necessary? diff from super?
//    public void setValue(int value)
//    {
//        super.setValue(value);
//    }

    public int[] getIntValues()
    {
        int[] values = new int[normalPointerValues.length];

        for (int i = 0; i < values.length; i++)
        {
            values[i] = getIntValue(i);
        }
        return values;
    }

    public void setIntValues(int[] values)
    {
        Arrays.sort(values);

        for (int i = 0; i < normalPointerValues.length; i++)
        {
            normalPointerValues[i] = MathUtils.clamp(MathUtils.normalize(values[i], min, max));
        }
    }

    public int getIntValue(int pointerIndex)
    {
        return (int) MathUtils.denormalize(getNormalizedValue(pointerIndex), min, max);
    }

    public void setIntValue(int pointerIndex, int value)
    {
        setNormalizedValue(pointerIndex, MathUtils.normalize(value, min, max));
    }

//    public double getNormalizedValue(int pointerIndex)
//    {
//        return normalPointerValues[pointerIndex];
//    }

    @Override
    public void setNormalizedValue(int pointerIndex, double normalValue)
    {
        boolean hasLeftAdjacent = pointerIndex > 0;
        boolean hasRightAdjacent = pointerIndex < normalPointerValues.length - 1;

        // what prevents sliders from passing each other
        double minValue = hasLeftAdjacent ? (normalPointerValues[pointerIndex - 1]) : 0;
        double maxValue = hasRightAdjacent ? (normalPointerValues[pointerIndex + 1]) : 1;

        double value = MathUtils.clamp(normalValue, minValue, maxValue);

//        System.out.println("PRE:" + value);
        value = Math.round(value * max) / max; // round (up?) to multiple (ex: 20 -> 0.05, 10 -> 0.1)
//        System.out.println("POST:" + value);

        normalPointerValues[pointerIndex] = value;
    }

//    protected double denormalize(double normalizedValue)
//    {
//        return normalizedValue * (max - min) + min;
//    }

//    @Override
//    protected double clamp(double value, double min, double max)
//    {
////        System.out.println(value);
////        value = Math.round(value * max) / max; // round (up?) to multiple (ex: 20 -> 0.05, 10 -> 0.1)
//
////        System.out.println(value);
//
//        // Clamp value (min, max)
//        return Math.min((Math.max(value, min)), max);
//    }

//    @Override
//    protected void update(int mouseX)
//    {
//        normalizedVal = (mouseX - xPos) / (float) width;
//        normalizedVal = Math.round(normalizedVal * max) / max; // round (up?) to multiple (ex: 20 -> 0.05, 10 -> 0.1)
//        clamp();
//
//        updateTooltip();
//
//        sendChangeToParent();
//    }
}
