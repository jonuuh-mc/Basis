package io.jonuuh.basis.lib.gui.element.container.behavior;

import io.jonuuh.basis.lib.gui.element.ElementUtils;
import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.element.container.GuiContainer;
import io.jonuuh.basis.lib.gui.element.slider.GuiScrollSlider;
import io.jonuuh.basis.lib.gui.listener.input.InputListener;

import java.util.List;

public class ScrollBehavior
{
    private final GuiContainer host;
    private final GuiScrollSlider scrollSlider;
    private int sliderXPosOffset;
    private int sliderWidth;

    public ScrollBehavior(Builder builder)
    {
        this.host = builder.host;
        this.sliderXPosOffset = 2;
        this.sliderWidth = 7;

        this.scrollSlider = new GuiScrollSlider.Builder(host.elementName + "$sliderVertical")
                .localPosition(host.getWidth() - sliderWidth - sliderXPosOffset, 2)
                .size(sliderWidth, host.getHeight() - 4)
                .vertical(true)
                .stateChangeBehavior(element -> slideChildrenVertically())
                .bounds(0, builder.scrollLength).build();

        host.addChild(scrollSlider);
    }

    // make this abstract maybe; a container might only want to scroll some elements for some reason
    // (definitely don't want to scroll the scroll slider itself, at least)
    public List<GuiElement> getScrollElements()
    {
        return host.getChildren();
    }

    public GuiScrollSlider getSlider()
    {
        return scrollSlider;
    }

    public void setSliderXPosOffset(int sliderXPosOffset)
    {
        this.sliderXPosOffset = sliderXPosOffset;
    }

    public void setSliderWidth(int sliderWidth)
    {
        this.sliderWidth = sliderWidth;
    }

    public void updateSlider()
    {
        getSlider().setLocalXPos(host.getWidth() - sliderWidth - sliderXPosOffset);
        getSlider().setHeight(host.getHeight() - 4);
        getSlider().updateScrollBarLength();
//        getSlider().setValue(0);
    }

    public void slideChildrenVertically()
    {
        float sliderValue = getSlider().getLastChange();

        for (GuiElement element : getScrollElements())
        {
            if (element.equals(scrollSlider))
            {
                continue;
            }

            element.setLocalYPos(element.getLocalYPos() - sliderValue);

            // TODO: safest behavior would be to disable an element if ANY of it is out of bounds,
            //  otherwise unintended wins/losses of greatest z element calc could arise when clicking around the edges of a
            //  container whose children are partially scrolled out of bounds
            boolean isOutOfBottomBound = (element.getBottomBound() - ElementUtils.getInnerBottomBound(host)) > 0;
            boolean isOutOfTopBound = (ElementUtils.getInnerTopBound(host) - element.getTopBound()) > 0;

            if (element instanceof InputListener)
            {
                ((InputListener) element).setEnabled(!isOutOfBottomBound && !isOutOfTopBound);
            }
        }
    }

    public static class Builder
    {
        protected GuiContainer host = null;
        protected float scrollLength;

        public Builder host(GuiContainer host)
        {
            this.host = host;
            return this;
        }

        public Builder length(float scrollLength)
        {
            this.scrollLength = scrollLength;
            return this;
        }

        public ScrollBehavior build()
        {
            return new ScrollBehavior(this);
        }
    }
}

