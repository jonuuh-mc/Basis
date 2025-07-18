package io.jonuuh.basis.lib.gui.element.slider;

import io.jonuuh.basis.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.basis.lib.gui.event.input.MouseDragEvent;
import io.jonuuh.basis.lib.gui.listener.input.MouseDragListener;
import io.jonuuh.basis.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.lib.util.MathUtils;
import io.jonuuh.basis.lib.util.RenderUtils;

public class GuiScrollSlider extends GuiSlider implements MouseDragListener
{
    /** A normalized value from 0-1: how long (height if vertical, width if horizontal) the scrollbar is */
    protected float scrollBarLength;
    /** A normalized value from 0-1: where on the slider the mouse was last pressed down */
    protected float grabPosValue;
    /** A normalized value from 0-1: what the normalValue of the slider was when mouse was last pressed down */
    protected float startSliderValue;
    /** A normalized value from 0-1: what the normalValue of the slider was before it was last changed */
    protected float lastValue;

    public GuiScrollSlider(Builder builder)
    {
        super(builder);
        updateScrollBarLength();
    }

    /**
     * A normalized value from 0-1: where on the slider the start (top if vertical, left if horizontal) of the scroll bar is
     */
    @Override
    public float getNormalizedValue()
    {
        return normalValue;
    }

    public void setNormalizedValue(float normalValue)
    {
        this.lastValue = getNormalizedValue();
        super.setNormalizedValue((float) MathUtils.clamp(normalValue, 0, 1 - scrollBarLength));
//        handlePostCustomEvent();
    }

    public float getLastChange()
    {
        return getValue() - (float) MathUtils.denormalize(lastValue, min, max);
    }

    public void updateScrollBarLength()
    {
        this.scrollBarLength = isVertical ? getHeight() / max : getWidth() / max;
    }

//    @Override
//    public void onMouseScroll(MouseScrollEvent event)
//    {
//        isMovingTimer = 10;
//        setNormalizedValue(getNormalizedValue() + (float) (MathUtils.normalize(event.wheelDelta * 10, min, max)));
//    }

    @Override
    public void onMouseDrag(MouseDragEvent event)
    {
        float valueAtMouse = getNormalValueAtScreenPos(event.mouseX, event.mouseY);
        float distance = valueAtMouse - grabPosValue;
        setNormalizedValue(startSliderValue + distance);
    }

    @Override
    public void onMouseDown(MouseDownEvent event)
    {
        super.onMouseDown(event);

        grabPosValue = getNormalValueAtScreenPos(event.mouseX, event.mouseY);

        if (grabPosValue < getNormalizedValue() || grabPosValue > getNormalizedValue() + scrollBarLength)
        {
            setNormalizedValue(grabPosValue - (scrollBarLength / 2F));
        }

        startSliderValue = getNormalizedValue();
    }

    @Override
    protected void drawVerticalSlider()
    {
        drawPointer();
    }

    @Override
    protected void drawHorizontalSlider()
    {
        drawPointer();
    }

    protected void drawPointer()
    {
        float screenPosWindowStart = getScreenPosAtNormalValue(getNormalizedValue());
        float screenPosWindowEnd = getScreenPosAtNormalValue(getNormalizedValue() + scrollBarLength);

        if (isVertical)
        {
            RenderUtils.drawRoundedRectWithBorder(worldXPos(), screenPosWindowStart, getWidth(), screenPosWindowEnd - screenPosWindowStart, getCornerRadius(), 1, getColor(GuiColorType.BASE), getColor(GuiColorType.BORDER));
        }
        else
        {
            RenderUtils.drawRoundedRectWithBorder(screenPosWindowStart, worldYPos(), screenPosWindowEnd - screenPosWindowStart, getHeight(), getCornerRadius(), 1, getColor(GuiColorType.BASE), getColor(GuiColorType.BORDER));
        }
    }

    public static class Builder extends GuiSlider.AbstractBuilder<Builder, GuiScrollSlider>
    {
        public Builder(String elementName)
        {
            super(elementName);
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiScrollSlider build()
        {
            return new GuiScrollSlider(this);
        }
    }
}
