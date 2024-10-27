package io.jonuuh.core.lib.config.gui.elements.interactable.sliders;

import io.jonuuh.core.lib.util.MathUtils;
import io.jonuuh.core.lib.util.RenderUtils;
import io.jonuuh.core.lib.config.gui.ISettingsGui;
import io.jonuuh.core.lib.config.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.core.lib.config.gui.elements.GuiTooltip;
import io.jonuuh.core.lib.util.Color;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiSlider extends GuiInteractableElement
{
    protected final double min;
    protected final double max;
    protected final double[] normalPointerValues;
    protected final List<Color> colors;
    protected DecimalFormat decimalFormat;
    protected int lastHeldPointer;
    protected int pointerSize;

    public GuiSlider(ISettingsGui parent, int xPos, int yPos, int width, int height, /*boolean isVertical,*/ double min, double max, double[] startValues)
    {
        super(parent, xPos, yPos, width, height);
        this.pointerSize = (this instanceof GuiVerticalSlider) ? width : height;
        this.min = min;
        this.max = max;

        this.decimalFormat = new DecimalFormat("#.###");

        // TODO: temp
        this.colors = new ArrayList<>();
        for (double v : startValues)
        {
            colors.add(Color.getRandom());
        }

        this.normalPointerValues = new double[startValues.length];

        Arrays.sort(startValues);
        for (int i = 0; i < startValues.length; i++)
        {
            normalPointerValues[i] = MathUtils.clamp(MathUtils.normalize(startValues[i], min, max));
        }

//        setDrawBounds(false);
    }

    public GuiSlider(ISettingsGui parent, int xPos, int yPos, int width, int height, double min, double max, double startValue)
    {
        this(parent, xPos, yPos, width, height, min, max, new double[]{startValue});
    }

    public GuiSlider(ISettingsGui parent, int xPos, int yPos, double min, double max, double[] startValues)
    {
        this(parent, xPos, yPos, 200, 16, min, max, startValues);
    }

    public GuiSlider(ISettingsGui parent, int xPos, int yPos, double min, double max, double startValue)
    {
        this(parent, xPos, yPos, min, max, new double[]{startValue});
    }

    public int getLength()
    {
        return normalPointerValues.length;
    }

    public double[] getValues()
    {
        double[] values = new double[normalPointerValues.length];

        for (int i = 0; i < values.length; i++)
        {
            values[i] = getValue(i);
        }
        return values;
    }

    public void setValues(double[] values)
    {
        Arrays.sort(values);

        // TODO: allow for size changing?
//        normalPointerValues = new double[values.length];

        for (int i = 0; i < normalPointerValues.length; i++)
        {
            normalPointerValues[i] = MathUtils.clamp(MathUtils.normalize(values[i], min, max));
        }
    }

    public double getValue(int pointerIndex)
    {
        return MathUtils.denormalize(getNormalizedValue(pointerIndex), min, max);
    }

    public void setValue(int pointerIndex, double value)
    {
        setNormalizedValue(pointerIndex, MathUtils.normalize(value, min, max));
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

        normalPointerValues[pointerIndex] = MathUtils.clamp(normalValue, minValue, maxValue);
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
    public void onScreenDraw(Minecraft mc, int mouseX, int mouseY)
    {
        super.onScreenDraw(mc, mouseX, mouseY);

        drawSlider();

        if (mouseDown)
        {
//                System.out.println(Arrays.toString(normalPointerValues));
            setNormalizedValue(lastHeldPointer, getSliderValueAtMousePos(mouseX, mouseY));

            // TODO: don't check for pre-claim? assumes no slider overlap (drag two sliders once)
            claimTooltipForPointer(lastHeldPointer);

            sendChangeToParent();
        }
    }

    @Override
    public void onMousePress(int mouseX, int mouseY)
    {
        super.onMousePress(mouseX, mouseY);
        lastHeldPointer = getClosestPointerToMouse(mouseX, mouseY);
    }

    protected void drawSlider()
    {
        float trackHeight = (height / 3F);

        // Draw track(s)
        for (int i = 0; i < normalPointerValues.length; i++)
        {
            float currPointerXCenter = getPointerCenter(i);
//            float prevPointerXCenter = getPointerXCenter(i - 1);

            float trackX = i != 0 ? getPointerCenter(i - 1) : xPos;
            float trackWidth = i != 0 ? currPointerXCenter - getPointerCenter(i - 1) : currPointerXCenter - xPos;

            // Left side track for each pointer
            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, trackX, yPos + trackHeight, trackWidth, trackHeight, parent.getOuterRadius(), colors.get(i), true);

            if (i == normalPointerValues.length - 1)
            {
                // Right side track for last pointer
                RenderUtils.drawRoundedRect(GL11.GL_POLYGON, currPointerXCenter, yPos + trackHeight, width - (currPointerXCenter - xPos), trackHeight, parent.getOuterRadius(), colorMap.get("ACCENT"), true);
            }
        }

        // Draw pointer(s)
        for (int i = 0; i < normalPointerValues.length; i++)
        {
//            if (i == lastHeldPointer)
//            {
//                continue;
//            }
            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, getPointerCenter(i) - (pointerSize / 2F), yPos, pointerSize, pointerSize, parent.getInnerRadius(), colors.get(i), true);
        }

//        GuiUtils.drawRoundedRect(GL11.GL_POLYGON, getPointerXCenter(lastHeldPointer) - (pointerSize / 2F), yPosition /*+ 5*/, pointerSize, pointerSize, radius * 3, colors.get(lastHeldPointer), true);
    }

    // can't really move by <1 pixel; normalized val is restricted by mouseX which is an int
    // technically all sliders are int sliders bc of this?
    public float getPointerCenter(int pointerIndex)
    {
        float center = (this instanceof GuiVerticalSlider)
                ? (float) (yPos + (getNormalizedValue(pointerIndex) * height))
                : (float) (xPos + (getNormalizedValue(pointerIndex) * width));

        if (this instanceof GuiVerticalSlider)
        {
            RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, xPos, center, 3, 3, new Color("#00ff00"));
        }
        else
        {
            RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, center, yPos, 3, 3, new Color("#00ff00"));
        }

        return center;
        // prevents sliders from going half off the start/end of the track
//        x = (float) clamp(x, xPos + (pointerSize / 2D), xPos + width - (pointerSize / 2D));
    }

    public int getClosestPointerToMouse(int mouseX, int mouseY)
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

    // mouse position on the slider
    protected double getSliderValueAtMousePos(int mouseX, int mouseY)
    {
        return (this instanceof GuiVerticalSlider) ? ((mouseY - yPos) / (double) height) : ((mouseX - xPos) / (double) width);
    }

    protected void claimTooltipForPointer(int pointerIndex)
    {
        this.tooltipStr = decimalFormat.format(getValue(pointerIndex));

        GuiTooltip.claim(this);

        int strWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(tooltipStr) - 1;
        GuiTooltip.getInstance().posX = getPointerCenter(pointerIndex) - (strWidth / 2F);
        GuiTooltip.getInstance().color = colors.get(pointerIndex);
    }

    public void claimHoverTooltip(int mouseX, int mouseY)
    {
        claimTooltipForPointer(getClosestPointerToMouse(mouseX, mouseY));
    }
}
