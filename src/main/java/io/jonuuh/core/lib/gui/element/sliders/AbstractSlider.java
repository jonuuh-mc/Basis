package io.jonuuh.core.lib.gui.element.sliders;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.core.lib.gui.event.input.MouseScrollEvent;
import io.jonuuh.core.lib.gui.event.lifecycle.ScreenTickEvent;
import io.jonuuh.core.lib.gui.listener.input.MouseClickListener;
import io.jonuuh.core.lib.gui.listener.input.MouseScrollListener;
import io.jonuuh.core.lib.gui.listener.lifecycle.ScreenTickListener;
import io.jonuuh.core.lib.util.MathUtils;

import java.text.DecimalFormat;

abstract class AbstractSlider extends GuiElement implements MouseClickListener, MouseScrollListener, ScreenTickListener
{
    protected final float min;
    protected final float max;
    protected final boolean isVertical;
    protected final boolean isInteger;
    protected float normalValue;
    protected DecimalFormat decimalFormat;
    protected int isMovingTimer;

    private boolean enabled;
    private boolean mouseDown;

    protected AbstractSlider(String elementName, float localXPos, float localYPos, float width, float height, float min, float max, float startValue, boolean isVertical, boolean isInteger)
    {
        super(elementName, localXPos, localYPos, width, height);
        this.min = min;
        this.max = max;
        this.isVertical = isVertical;
        this.isInteger = isInteger;
        setValue(startValue);

        this.decimalFormat = new DecimalFormat("#.###");
    }

    protected AbstractSlider(String elementName, float localXPos, float localYPos, float min, float max, float startValue, boolean isVertical, boolean isInteger)
    {
        this(elementName, localXPos, localYPos, DEFAULT_WIDTH, DEFAULT_HEIGHT, min, max, startValue, isVertical, isInteger);
    }

    public float getValue()
    {
        return (float) MathUtils.denormalize(getNormalizedValue(), min, max);
    }

    public void setValue(float value)
    {
        setNormalizedValue((float) MathUtils.clamp(MathUtils.normalize(value, min, max)));
    }

    public int getValueInt()
    {
        return Math.round(getValue());
    }

    public float getNormalizedValue()
    {
        return normalValue;
    }

    public void setNormalizedValue(float normalValue)
    {
        double value = MathUtils.clamp(normalValue, 0, 1);
        // If an integer slider, round proportional to the max value
        this.normalValue = isInteger ? (Math.round(value * max) / max) : (float) value;
//        handlePostCustomEvent();
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public boolean isMouseDown()
    {
        return mouseDown;
    }

    @Override
    public void setMouseDown(boolean mouseDown)
    {
        this.mouseDown = mouseDown;
    }

    public DecimalFormat getDecimalFormat()
    {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat)
    {
        this.decimalFormat = decimalFormat;
    }

    protected float getPointerSize()
    {
        return isVertical ? getWidth() : getHeight();
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        if (isVertical)
        {
            drawVerticalSlider();
        }
        else
        {
            drawHorizontalSlider();
        }

        if (isHovered() || isMouseDown())
        {
            isMovingTimer = 5;
        }
    }

    protected abstract void drawVerticalSlider();

    protected abstract void drawHorizontalSlider();

    @Override
    public void onMouseDown(MouseDownEvent event)
    {
        MouseClickListener.super.onMouseDown(event);
    }

    @Override
    public void onMouseScroll(MouseScrollEvent event)
    {
        isMovingTimer = 10;
        setNormalizedValue(getNormalizedValue() + (float) MathUtils.normalize(event.wheelDelta, min, max));
    }

    @Override
    public void onScreenTick(ScreenTickEvent event)
    {
        if (isMovingTimer > 0)
        {
            isMovingTimer--;
        }
    }

    /**
     * Calculate normal value along the slider given some world x,y position
     * <p>
     * Used to calculate where on the slider the mouse was clicked
     */
    protected float getNormalValueAtScreenPos(int xPos, int yPos)
    {
        return isVertical
                ? (xPos - worldYPos()) / getHeight()
                : (yPos - worldXPos()) / getWidth();
    }

    /**
     * Calculate world x or y screen position of some normal value along the slider
     */
    protected float getScreenPosAtNormalValue(float normalValue)
    {
        return isVertical
                ? (worldYPos() + (normalValue * getHeight()))
                : (worldXPos() + (normalValue * getWidth()));
    }
}
