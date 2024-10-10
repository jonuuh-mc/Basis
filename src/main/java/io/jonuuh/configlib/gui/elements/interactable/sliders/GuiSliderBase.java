package io.jonuuh.configlib.gui.elements.interactable.sliders;

import io.jonuuh.configlib.gui.GuiUtils;
import io.jonuuh.configlib.gui.ISettingsGui;
import io.jonuuh.configlib.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.configlib.gui.elements.GuiTooltip;
import io.jonuuh.configlib.util.Color;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiSliderBase/*<T>*/ extends GuiInteractableElement
{
    protected final double min;
    protected final double max;
    protected final double[] normalPointerValues;
    protected final List<Color> colors;
    protected DecimalFormat decimalFormat;
    protected int lastHeldPointer;
    protected int pointerSize;

    public GuiSliderBase(ISettingsGui parent, int xPos, int yPos, int width, int height, /*boolean isVertical,*/ double min, double max, List<Double> startValues)
    {
        super(parent, xPos, yPos, width, height);
        this.pointerSize = height;
//        this.radius = Math.min(radius, (height / 2F)); // width if isVertical
        this.min = min;
        this.max = max;

        this.decimalFormat = new DecimalFormat("#.###");

        // TODO: temp
        this.colors = new ArrayList<>();
        startValues.forEach(pointer -> colors.add(Color.getRandom(/*0.5F*/)));

        this.normalPointerValues = new double[startValues.size()];

        Collections.sort(startValues);

        for (int i = 0; i < startValues.size(); i++)
        {
            normalPointerValues[i] = clamp(normalize(startValues.get(i)));
        }
    }

    public GuiSliderBase(ISettingsGui parent, int xPos, int yPos, double min, double max, List<Double> startValues)
    {
        this(parent, xPos, yPos, 200, 16, min, max, startValues);
    }

    public double getValue(int pointerIndex)
    {
        return denormalize(getNormalizedValue(pointerIndex));
    }

    public void setValue(int pointerIndex, double value)
    {
        setNormalizedValue(pointerIndex, normalize(value));
    }

    public double getNormalizedValue(int pointerIndex)
    {
        return normalPointerValues[pointerIndex];
    }

    public void setNormalizedValue(int pointerIndex, double normalValue)
    {
        boolean hasLeftAdjacent = pointerIndex > 0;
        boolean hasRightAdjacent = pointerIndex < normalPointerValues.length - 1;

        // what prevents sliders from passing each other
        double minValue = hasLeftAdjacent ? (normalPointerValues[pointerIndex - 1]) : 0;
        double maxValue = hasRightAdjacent ? (normalPointerValues[pointerIndex + 1]) : 1;

        normalPointerValues[pointerIndex] = clamp(normalValue, minValue, maxValue);
    }

    public DecimalFormat getDecimalFormat()
    {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat)
    {
        this.decimalFormat = decimalFormat;
    }

    @Override
    public boolean onScreenDraw(Minecraft mc, int mouseX, int mouseY)
    {
        boolean wasDrawn = super.onScreenDraw(mc, mouseX, mouseY);

        if (wasDrawn)
        {
            drawSlider();

            if (mouseDown)
            {
                setNormalizedValue(lastHeldPointer, getSliderValueAtMouseX(mouseX));

                // TODO: don't check for pre-claim? assumes no slider overlap (drag two sliders once)
                claimTooltipForPointer(lastHeldPointer);

                sendChangeToParent();
            }
        }

        return wasDrawn;
    }

    @Override
    public void onMousePress(int mouseX, int mouseY)
    {
        super.onMousePress(mouseX, mouseY);
        lastHeldPointer = getClosestPointerToMouse(mouseX);
    }

//    @Override
//    public void onMouseRelease(int mouseX, int mouseY)
//    {
//        mouseDown = false;
//    }

    protected void drawSlider()
    {
        float trackHeight = (height / 3F);

        // Draw track(s)
        for (int i = 0; i < normalPointerValues.length; i++)
        {
            float currPointerXCenter = getPointerXCenter(i);
//            float prevPointerXCenter = getPointerXCenter(i - 1);

            float trackX = i != 0 ? getPointerXCenter(i - 1) : xPos;
            float trackWidth = i != 0 ? currPointerXCenter - getPointerXCenter(i - 1) : currPointerXCenter - xPos;

            // Left side track for each pointer
            GuiUtils.drawRoundedRect(GL11.GL_POLYGON, trackX, yPos + trackHeight, trackWidth, trackHeight, parent.getOuterRadius(), colors.get(i), true);

            if (i == normalPointerValues.length - 1)
            {
                // Right side track for last pointer
                GuiUtils.drawRoundedRect(GL11.GL_POLYGON, currPointerXCenter, yPos + trackHeight, width - (currPointerXCenter - xPos), trackHeight, parent.getOuterRadius(), colorMap.get("ACCENT"), true);
            }
        }

        // Draw pointer(s)
        for (int i = 0; i < normalPointerValues.length; i++)
        {
//            if (i == lastHeldPointer)
//            {
//                continue;
//            }
            GuiUtils.drawRoundedRect(GL11.GL_POLYGON, getPointerXCenter(i) - (pointerSize / 2F), yPos, pointerSize, pointerSize, parent.getInnerRadius(), colors.get(i), true);
        }

//        GuiUtils.drawRoundedRect(GL11.GL_POLYGON, getPointerXCenter(lastHeldPointer) - (pointerSize / 2F), yPosition /*+ 5*/, pointerSize, pointerSize, radius * 3, colors.get(lastHeldPointer), true);
    }

    // can't really move by <1 pixel; normalized val is restricted by mouseX which is an int
    // technically all sliders are int sliders bc of this?
    public float getPointerXCenter(int pointerIndex)
    {
        float x = (float) (xPos + (getNormalizedValue(pointerIndex) * width));
        GuiUtils.drawRectangle(GL11.GL_LINE_LOOP, x, yPos, 3, 3, new Color());

        // prevents sliders from going half off the start/end of the track
        x = (float) clamp(x, xPos + (pointerSize / 2D), xPos + width - (pointerSize / 2D));

        return x;
    }

    public int getClosestPointerToMouse(int mouseX)
    {
        double mouseXSliderVal = getSliderValueAtMouseX(mouseX);

        // find the closest pointer to the mouse
        int leftmostClosestPointer = 0;
        double leastDistance = 1.0;
        for (int i = 0; i < normalPointerValues.length; i++)
        {
            double distance = Math.abs(normalPointerValues[i] - mouseXSliderVal);

            if (distance < leastDistance) // < means always favoring leftmost pointer in a tie, <= would favor rightmost (pointers sorted ascending: left to right)
            {
                leastDistance = distance;
                leftmostClosestPointer = i;
            }
        }

        // find the furthest-right pointer overlapping the closest pointer to the mouse, if it exists
        // (because pointers can never pass across each other higher indices in the array are further to the right)
        int farRightOverlappedPointer = 0;
        for (int i = leftmostClosestPointer + 1; i < normalPointerValues.length; i++)
        {
            // same value = overlapped pointers
            if (normalPointerValues[i] == normalPointerValues[leftmostClosestPointer])
            {
                farRightOverlappedPointer = i;
            }
        }

        // if there exists an overlapping pointer to the right, and the mouse is to the right of the stacked pointers,
        // the true closest pointer must be the furthest pointer to the right in the stack
        return (farRightOverlappedPointer != 0 && mouseXSliderVal > normalPointerValues[leftmostClosestPointer]) ? farRightOverlappedPointer : leftmostClosestPointer;
    }

//    protected void update(int mouseX)
//    {
//        setNormalizedValue(lastHeldPointer, getSliderValueAtMouseX(mouseX));
//
//        updateTooltip(lastHeldPointer);
//
//        sendChangeToParent();
//    }

    // mouse position on the slider
    protected double getSliderValueAtMouseX(int mouseX)
    {
        return (mouseX - xPos) / (double) width;
    }

    protected double normalize(double value)
    {
        return (value - min) / (max - min);
    }

    protected double denormalize(double normalizedValue)
    {
        return normalizedValue * (max - min) + min;
    }

    protected double clamp(double value)
    {
        // Clamp value (0, 1)
        return clamp(value, 0.0, 1.0);
    }

    protected double clamp(double value, double min, double max)
    {
        // Clamp value (min, max)
        return Math.min((Math.max(value, min)), max);
    }

    protected void claimTooltipForPointer(int pointerIndex)
    {
        this.tooltipStr = decimalFormat.format(getValue(pointerIndex));

        GuiTooltip.claim(this);

        int strWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(tooltipStr) - 1;
        GuiTooltip.getInstance().posX = getPointerXCenter(pointerIndex) - (strWidth / 2F);
        GuiTooltip.getInstance().color = colors.get(pointerIndex);
    }

    public void claimHoverTooltip(int mouseX, int mouseY)
    {
        claimTooltipForPointer(getClosestPointerToMouse(mouseX));
    }
}
