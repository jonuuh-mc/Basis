package io.jonuuh.configlib.gui.elements;

import io.jonuuh.configlib.gui.GuiUtils;
import io.jonuuh.configlib.gui.ISettingsGui;
import io.jonuuh.configlib.util.Color;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiAbstractSlider/*<T>*/ extends GuiInteractableElement
{
    protected final int pointerSize;
    protected final float radius;
    protected final double min;
    protected final double max;
    protected final double[] normalPointerValues;
    protected boolean mouseDown;
    protected int lastHeldPointer;
    private final List<Color> colors;

    public GuiAbstractSlider(ISettingsGui parent, int xPos, int yPos, int width, int height, float radius, /*boolean isVertical,*/ double min, double max, List<Double> startValues)
    {
        super(parent, xPos, yPos, width, height);
        this.pointerSize = height;
//        this.radius = radius;
        this.radius = Math.min(radius, (height / 2F)); // width if isVertical
        this.min = min;
        this.max = max;

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

    public GuiAbstractSlider(ISettingsGui parent, int xPos, int yPos, double min, double max, List<Double> startValues)
    {
        this(parent, xPos, yPos, 100, 10, 2, min, max, startValues);
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

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        super.drawButton(mc, mouseX, mouseY);

        if (visible)
        {
            if (mouseDown)
            {
                update(mouseX);
            }
            drawSlider();
        }
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
        if (mouseDown)
        {
            drawTooltip(mc);
        }
        else
        {
            if (hovered)
            {
                updateTooltip(getClosestPointerToMouse(mouseX));
            }
            super.mouseDragged(mc, mouseX, mouseY);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        boolean pressed = super.mousePressed(mc, mouseX, mouseY);

        if (pressed)
        {
            mouseDown = true;
            lastHeldPointer = getClosestPointerToMouse(mouseX);
        }
        return pressed;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY)
    {
        mouseDown = false;
    }

    protected void drawSlider()
    {
        float trackHeight = (height / 3F);

        // Draw track(s)
        for (int i = 0; i < normalPointerValues.length; i++)
        {
            float currPointerXCenter = getPointerXCenter(i);
//            float prevPointerXCenter = getPointerXCenter(i - 1);

            float trackX = i != 0 ? getPointerXCenter(i - 1) : xPosition;
            float trackWidth = i != 0 ? currPointerXCenter - getPointerXCenter(i - 1) : currPointerXCenter - xPosition;

            // Left side track for each pointer
            GuiUtils.drawRoundedRect(GL11.GL_POLYGON, trackX, yPosition + trackHeight, trackWidth, trackHeight, radius, colors.get(i), true);

            if (i == normalPointerValues.length - 1)
            {
                // Right side track for last pointer
                GuiUtils.drawRoundedRect(GL11.GL_POLYGON, currPointerXCenter, yPosition + trackHeight, width - (currPointerXCenter - xPosition), trackHeight, radius, accentColor, true);
            }
        }

        // Draw pointer(s)
        for (int i = 0; i < normalPointerValues.length; i++)
        {
            GuiUtils.drawRoundedRect(GL11.GL_POLYGON, getPointerXCenter(i) - (pointerSize / 2F), yPosition, pointerSize, pointerSize, radius * 3, colors.get(i), true);
        }
    }

    // can't really move by <1 pixel; normalized val is restricted by mouseX which is an int
    // technically all sliders are int sliders bc of this?
    public float getPointerXCenter(int pointerIndex)
    {
        float x = (float) (xPosition + (getNormalizedValue(pointerIndex) * width));
        GuiUtils.drawRectangle(GL11.GL_LINE_LOOP, x, yPosition, 3, 3, new Color());

        // prevents sliders from going half off the start/end of the track
        x = (float) clamp(x, xPosition + (pointerSize / 2D), xPosition + width - (pointerSize / 2D));

        return x;
    }

    protected int getClosestPointerToMouse(int mouseX)
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

    protected void update(int mouseX)
    {
        setNormalizedValue(lastHeldPointer, getSliderValueAtMouseX(mouseX));

        updateTooltip(lastHeldPointer);

        sendChangeToParent();
    }

    // mouse position on the slider
    protected double getSliderValueAtMouseX(int mouseX)
    {
        return (mouseX - xPosition) / (double) width;
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

    protected void updateTooltip(int pointerIndex)
    {
        DecimalFormat df = new DecimalFormat("#.###");
        String str = /*df.format(normalizedVal) + " " + */df.format(getValue(pointerIndex));

        int strWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(str) - 1;

        tooltip = new Tooltip(str);
        tooltip.x = getPointerXCenter(pointerIndex) - (strWidth / 2F);
        tooltip.color = colors.get(pointerIndex);
    }

//    protected double offsetByScreenPixel(double normalizedSliderValue, int offset)
//    {
//        double screenX = xPosition + (normalizedSliderValue * width);
//        return ((screenX + offset) - xPosition) / (double) width;
//    }
}
