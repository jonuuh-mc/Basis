package io.jonuuh.basis.lib.gui.element.slider;

import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.element.behavior.ClickBehavior;
import io.jonuuh.basis.lib.gui.event.input.MouseScrollEvent;
import io.jonuuh.basis.lib.gui.event.lifecycle.ScreenTickEvent;
import io.jonuuh.basis.lib.util.Color;
import io.jonuuh.basis.lib.util.MathUtils;
import io.jonuuh.basis.lib.util.RenderUtils;

import java.text.DecimalFormat;

abstract class GuiSlider extends GuiElement
{
    // min and max are (as of writing this) only non-final so that a scroll slider of a list view element
    // can change the slider's max length when more elements are added to the list
    /** The minimum value of this slider (not normalized; readable value) */
    protected float min;
    /** The maximum value of this slider (not normalized; readable value) */
    protected float max;
    protected final boolean isVertical;
    protected final boolean isInteger;
    protected Color pointerColor;
    protected Color trackColor;
    protected final DecimalFormat decimalFormat;
    protected float normalValue;
    /** This slider will be treated as 'moving' for this many more screen ticks */
    protected int movingTimer;
    /** This slider will be treated as 'hovered' for this many more screen ticks */
    protected int hoveredTimer;

    protected ClickBehavior clickBehavior;

    protected GuiSlider(AbstractBuilder<?, ?> builder)
    {
        super(builder);
        this.min = builder.min;
        this.max = builder.max;
        this.isVertical = builder.isVertical;
        this.isInteger = builder.isInteger;
        this.pointerColor = builder.pointerColor;
        this.trackColor = builder.trackColor;
        this.decimalFormat = builder.decimalFormat;

        // This is the same as `setValue(builder.startValue);`, but calling setValue() from constructor
        // is a bad idea as it also tries to apply the event behavior in setNormalizedValue()
        float value = (float) MathUtils.clamp(MathUtils.normalize(builder.startValue, min, max), 0, 1);
        this.normalValue = isInteger ? roundAgainstSliderRange(value) : value;

        this.clickBehavior = new ClickBehavior(this);
        this.addEventListener(MouseScrollEvent.class, this::onMouseScroll);
        this.addEventListener(ScreenTickEvent.class, this::onScreenTick);
    }

    public float getValue()
    {
        return (float) MathUtils.denormalize(getNormalizedValue(), min, max);
    }

    public void setValue(float value)
    {
        setNormalizedValue((float) MathUtils.normalize(value, min, max));
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
        float value = (float) MathUtils.clamp(normalValue, 0, 1);
        // If an integer slider, round proportional to the range of the slider
        this.normalValue = isInteger ? roundAgainstSliderRange(value) : value;
        // TODO: tryApplyPostEventBehavior(MouseDownEvent.class);
    }

    public float getMin()
    {
        return min;
    }

    public float getMax()
    {
        return max;
    }

    protected float getPointerSizeRatio()
    {
        return 2;
    }

    protected float getPointerSize()
    {
        return isVertical ? getWidth() / getPointerSizeRatio() : getHeight() / getPointerSizeRatio();
    }

    protected float getTrackThickness()
    {
        return isVertical ? (getWidth() / 3F) : (getHeight() / 3F);
    }

    protected Color getPointerColor()
    {
        return pointerColor;
    }

    protected Color getTrackColor()
    {
        return trackColor;
    }

    protected float scaleWheelDelta(int wheelDelta)
    {
        // wheelDelta divided by the range of the slider if integer, else 1% of wheelDelta
        return isInteger ? wheelDelta / (max - min) : wheelDelta * 0.01F;
    }

    /**
     * Used for setting normalized values of integer sliders
     *
     * @param clampedNormalValue An already normalized, already clamped value
     * @return The given value rounded proportional to the range of the slider
     */
    protected float roundAgainstSliderRange(float clampedNormalValue)
    {
        float sliderRange = max - min;
        return Math.round(clampedNormalValue * sliderRange) / sliderRange;
    }

    /**
     * Calculate normal value along the slider given some world x,y position
     * <p>
     * Used to calculate where on the slider the mouse was clicked
     */
    protected float getNormalValueAtScreenPos(int xPos, int yPos)
    {
        return isVertical
                ? (yPos - worldYPos()) / getHeight()
                : (xPos - worldXPos()) / getWidth();
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

    public boolean isMouseDown()
    {
        return clickBehavior.isMouseDown();
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        if (shouldDrawBackground())
        {
            RenderUtils.drawRoundedRectWithBorder(worldXPos(), worldYPos(), getWidth(), getHeight(),
                    getCornerRadius(), 1, getBackgroundColor(), getBorderColor());
        }

        if (isVertical)
        {
            drawVerticalSlider();
        }
        else
        {
            drawHorizontalSlider();
        }
    }

    protected abstract void drawVerticalSlider();

    protected abstract void drawHorizontalSlider();

    // TODO: GuiSliders are currently the only case of onScreenDraw being used for something other than actually drawing elements.
    //  Would be great to use onMouseDrag around here for setting normal value rather than onScreenDraw, except mouse
    //  drag events fire from game ticking loop (every ~50ms) rather than from rendering loop (~<fps>/1000ms?). This makes
    //  the slider appear "laggy" if not an integer slider.
    //  Have thought about jumping through some extra hoops to make smooth sliders using onMouseDrag workable
    //  (lerping from last value updated by onMouseDrag?), but you'd still ultimately need to set the normal value from onScreenDraw,
    //  so the underlying problem isn't fixed

    public void onMouseScroll(MouseScrollEvent event)
    {
        movingTimer = 10;
        setNormalizedValue(getNormalizedValue() + scaleWheelDelta(event.wheelDelta));
    }

    private void onScreenTick(ScreenTickEvent event)
    {
        if (isHovered())
        {
            hoveredTimer = 5;
        }
        else if (hoveredTimer > 0)
        {
            hoveredTimer--;
        }

        if (isMouseDown())
        {
            movingTimer = 5;
        }
        else if (movingTimer > 0)
        {
            movingTimer--;
        }
    }

    public static abstract class AbstractBuilder<T extends GuiSlider.AbstractBuilder<T, R>, R extends GuiSlider> extends GuiElement.AbstractBuilder<T, R>
    {
        protected float min = 0;
        protected float max = 100;
        protected float startValue = 0;
        protected boolean isVertical = false;
        protected boolean isInteger = false;
        protected Color pointerColor = Color.GRAY;
        protected Color trackColor = Color.WHITE;
        protected DecimalFormat decimalFormat = new DecimalFormat("#.##");

        protected AbstractBuilder(String elementName)
        {
            super(elementName);
            // Override GuiElement's default of drawing the background
            drawBackground(false);
        }

        public T bounds(float min, float max)
        {
            this.min = min;
            this.max = max;
            return self();
        }

        public T startValue(float startValue)
        {
            this.startValue = startValue;
            return self();
        }

        public T vertical(boolean isVertical)
        {
            this.isVertical = isVertical;
            return self();
        }

        public T integer(boolean isInteger)
        {
            this.isInteger = isInteger;
            return self();
        }

        public T pointerColor(Color color)
        {
            this.pointerColor = color;
            return self();
        }

        public T trackColor(Color color)
        {
            this.trackColor = color;
            return self();
        }

        public T decimalFormat(DecimalFormat decimalFormat)
        {
            this.decimalFormat = decimalFormat;
            return self();
        }
    }
}
