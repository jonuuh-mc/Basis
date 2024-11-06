package io.jonuuh.core.lib.config.gui.elements.interactable.sliders;

import io.jonuuh.core.lib.config.gui.elements.GuiContainer;
import io.jonuuh.core.lib.util.MathUtils;
import io.jonuuh.core.lib.util.RenderUtils;
import io.jonuuh.core.lib.config.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.core.lib.config.gui.elements.GuiTooltip;
import io.jonuuh.core.lib.util.Color;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGuiSlider<T extends Number> extends GuiInteractableElement/*<T>*/
{
    protected final double min;
    protected final double max;
    protected final List<Color> colors;
    protected final boolean isVertical;
    protected double[] normalPointerValues;

    protected DecimalFormat decimalFormat;
    protected int lastHeldPointer;
    protected int pointerSize;

    public AbstractGuiSlider(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, double min, double max, T[] startValues, boolean isVertical)
    {
        super(parent, elementName, xPos, yPos, width, height);
        this.min = min;
        this.max = max;

        this.isVertical = isVertical;
        this.pointerSize = isVertical ? width : height;

        this.decimalFormat = new DecimalFormat("#.###");

        // TODO: temp
        this.colors = new ArrayList<>();
        for (T v : startValues)
        {
            colors.add(Color.getRandom());
        }

        setValues(startValues);
    }

    public AbstractGuiSlider(GuiContainer parent, String elementName, int xPos, int yPos, double min, double max, T[] startValues)
    {
        this(parent, elementName, xPos, yPos, 200, 16, min, max, startValues, false);
    }

    public abstract T getValue(int pointerIndex);

    public abstract void setValue(int pointerIndex, T value);

    public abstract T[] getValues();

    public abstract void setValues(T[] values);

    public double getNormalizedValue(int pointerIndex)
    {
        return normalPointerValues[pointerIndex];
    }

    public void setNormalizedValue(int pointerIndex, double normalValue)
    {
        normalPointerValues[pointerIndex] = clampBetweenAdjacents(pointerIndex, normalValue);
    }

    public DecimalFormat getDecimalFormat()
    {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat)
    {
        this.decimalFormat = decimalFormat;
    }

    public int getNumPointers()
    {
        return normalPointerValues.length;
    }

    // what prevents sliders from passing each other
    protected double clampBetweenAdjacents(int pointerIndex, double normalValue)
    {
        boolean hasLeftAdjacent = pointerIndex > 0;
        boolean hasRightAdjacent = pointerIndex < normalPointerValues.length - 1;

        double minValue = hasLeftAdjacent ? (normalPointerValues[pointerIndex - 1]) : 0;
        double maxValue = hasRightAdjacent ? (normalPointerValues[pointerIndex + 1]) : 1;

        return MathUtils.clamp(normalValue, minValue, maxValue);
    }

    // mouse position on the slider
    protected double getSliderValueAtMousePos(int mouseX, int mouseY)
    {
        return isVertical
                ? (mouseY - yPos) / (double) height
                : (mouseX - xPos) / (double) width;
    }

    // x/y screen position of the center of a pointer
    protected float getPointerScreenPos(int pointerIndex)
    {
//        if (isVertical)
//        {
//            RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, xPos, center, 3, 3, new Color("#00ff00"));
//        }
//        else
//        {
//            RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, center, yPos, 3, 3, new Color("#00ff00"));
//        }
        return isVertical
                ? (float) (yPos + (getNormalizedValue(pointerIndex) * height))
                : (float) (xPos + (getNormalizedValue(pointerIndex) * width));
    }

    @Override
    public void onMousePress(int mouseX, int mouseY)
    {
        super.onMousePress(mouseX, mouseY);
        lastHeldPointer = getClosestPointerToMouse(mouseX, mouseY);
    }

    @Override
    protected void drawElement(int mouseX, int mouseY, float partialTicks)
    {
        if (isVertical)
        {
            drawVerticalSlider();
        }
        else
        {
            drawHorizontalSlider();
        }

        if (mouseDown)
        {
//                System.out.println(Arrays.toString(normalPointerValues));
            setNormalizedValue(lastHeldPointer, getSliderValueAtMousePos(mouseX, mouseY));

            // TODO: don't check for pre-claim? assumes no slider overlap (drag two sliders once)
//            claimTooltipForPointer(lastHeldPointer);

            sendChangeToSetting();
        }
    }

    protected void drawHorizontalSlider()
    {
        float trackHeight = (height / 3F);

        // Draw track(s)
        for (int i = 0; i < normalPointerValues.length; i++)
        {
            float currPointerCenter = getPointerScreenPos(i);

            // Right side track for last pointer
            if (i == normalPointerValues.length - 1)
            {
                float x = currPointerCenter;
                float w = width - (currPointerCenter - xPos);

                RenderUtils.drawRoundedRect(GL11.GL_POLYGON, x, yPos + trackHeight, w, trackHeight, parent.getOuterRadius(), parent.getColorMap().get("ACCENT"), true);
            }

            // Left side track for all but first pointer
            float x = (i == 0) ? xPos : getPointerScreenPos(i - 1);
            float w = (i == 0) ? (currPointerCenter - xPos) : (currPointerCenter - getPointerScreenPos(i - 1));

            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, x, yPos + trackHeight, w, trackHeight, parent.getOuterRadius(), colors.get(i), true);
        }

        drawPointers();
    }

    protected void drawVerticalSlider()
    {
        float trackWidth = (width / 3F);

        // Draw track(s)
        for (int i = 0; i < normalPointerValues.length; i++)
        {
            float currPointerCenter = getPointerScreenPos(i);

            // Right side track for last pointer
            if (i == normalPointerValues.length - 1)
            {
                float y = currPointerCenter;
                float h = height - (currPointerCenter - yPos);

                RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos + trackWidth, y, trackWidth, h, parent.getOuterRadius(), parent.getColorMap().get("ACCENT"), true);
            }

            // Left side track for all but first pointer
            float y = (i == 0) ? yPos : getPointerScreenPos(i - 1);
            float h = (i == 0) ? (currPointerCenter - yPos) : (currPointerCenter - getPointerScreenPos(i - 1));

            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos + trackWidth, y, trackWidth, h, parent.getOuterRadius(), colors.get(i), true);
        }

        drawPointers();
    }

    protected void drawPointers()
    {
        for (int i = 0; i < normalPointerValues.length; i++)
        {
            float x = isVertical ? xPos : getPointerScreenPos(i) - (pointerSize / 2F);
            float y = isVertical ? getPointerScreenPos(i) - (pointerSize / 2F) : yPos;

//            if (i == lastHeldPointer && mouseDown)
//            {
//                RenderUtils.drawRoundedRect(GL11.GL_POLYGON, x - 2, y - 2, pointerSize + 4, pointerSize + 4, parent.getInnerRadius(), colors.get(i).setA(0.8F), true);
//                continue;
//            }
            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, x, y, pointerSize, pointerSize, parent.getInnerRadius(), colors.get(i), true);
        }
    }

    protected int getClosestPointerToMouse(int mouseX, int mouseY)
    {
        double mousePosSliderVal = getSliderValueAtMousePos(mouseX, mouseY);

        // find the closest pointer to the mouse
        int leftmostClosestPointer = 0;
        double leastDistance = 1.0;
        for (int i = 0; i < normalPointerValues.length; i++)
        {
            double distance = Math.abs(normalPointerValues[i] - mousePosSliderVal);

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
        return (farRightOverlappedPointer != 0 && mousePosSliderVal > normalPointerValues[leftmostClosestPointer]) ? farRightOverlappedPointer : leftmostClosestPointer;
    }

    protected void claimTooltipForPointer(int pointerIndex)
    {
        this.tooltipStr = decimalFormat.format(getValue(pointerIndex));
        int strWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(tooltipStr) - 1;

        if (isVertical)
        {
            this.tooltip.posX = xPos - 7 - strWidth;
            this.tooltip.posY = getPointerScreenPos(pointerIndex) /*- (strWidth / 2F)*/;
        }
        else
        {
            this.tooltip.posX = getPointerScreenPos(pointerIndex) - (strWidth / 2F);
        }

//        GuiTooltip.getInstance().color = colors.get(pointerIndex);

//        this.tooltipStr = decimalFormat.format(getValue(pointerIndex));
//        GuiTooltip.claim(this);
//
//        int strWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(tooltipStr) - 1;
//
//        if (isVertical)
//        {
//            GuiTooltip.getInstance().posX = xPos - 7 - strWidth;
//            GuiTooltip.getInstance().posY = getPointerScreenPos(pointerIndex) /*- (strWidth / 2F)*/;
//        }
//        else
//        {
//            GuiTooltip.getInstance().posX = getPointerScreenPos(pointerIndex) - (strWidth / 2F);
//        }
//
//        GuiTooltip.getInstance().color = colors.get(pointerIndex);
    }

//    @Override
//    public void claimHoverTooltip(int mouseX, int mouseY)
//    {
//        claimTooltipForPointer(getClosestPointerToMouse(mouseX, mouseY));
//    }
}
