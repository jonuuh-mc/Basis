package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.properties.FlexAlign;

/**
 * A wrapper for a GuiElement inside a GuiFlexContainer;
 * contains information describing the GuiElement in the container layout: how it should move, grow, shrink, etc
 */
public class FlexItem
{
    /** The GuiElement wrapped inside this FlexItem */
    private final GuiElement element;

    /** Initial width of this element */
    private final float initWidth;
    /** Initial height of this element */
    private final float initHeight;

    /** Minimum width this element can be shrunk to when the main axis is horizontal */
    private float minWidth;
    /** Maximum width this element can be grown to when the main axis is horizontal */
    private float maxWidth;

    /** Minimum height this element can be shrunk to when the main axis is vertical */
    private float minHeight;
    /** Maximum height this element can be grown to when the main axis is vertical */
    private float maxHeight;

    // TODO: impl. this in GuiFlexContainer
    /** Order of this element among the children along the main axis */
    private Integer order;
    /**
     * Part of how much weight this element has when the items along the main axis are grown.
     * A value of 0 is default and means this element will not grow to fill free space
     */
    private int grow;
    /**
     * Part of how much weight this element has when the items along the main axis are shrunk.
     * A value of 1 is default, a value of 0 means this element will not shrink when there is negative free space
     */
    private int shrink;
    /** How this item should be aligned along the cross axis */
    private FlexAlign alignSelf;

    public FlexItem(Builder builder)
    {
        this.element = builder.element;

        this.initWidth = element.getWidth();
        this.initHeight = element.getHeight();

        this.minWidth = builder.minWidth;
        this.maxWidth = builder.maxWidth;

        this.minHeight = builder.minHeight;
        this.maxHeight = builder.maxHeight;

        this.order = builder.order;
        this.grow = builder.grow;
        this.shrink = builder.shrink;
        this.alignSelf = builder.alignSelf;
    }

    public FlexItem(GuiElement element)
    {
        this(new Builder(element));
    }

    public GuiElement getElement()
    {
        return element;
    }

    public float getInitWidth()
    {
        return initWidth;
    }

    public float getInitHeight()
    {
        return initHeight;
    }

    public float getMinWidth()
    {
        return minWidth;
    }

    public FlexItem setMinWidth(float minWidth)
    {
        this.minWidth = minWidth;
        return this;
    }

    public float getMaxWidth()
    {
        return maxWidth;
    }

    public FlexItem setMaxWidth(float maxWidth)
    {
        this.maxWidth = maxWidth;
        return this;
    }

    public float getMinHeight()
    {
        return minHeight;
    }

    public FlexItem setMinHeight(float minHeight)
    {
        this.minHeight = minHeight;
        return this;
    }

    public float getMaxHeight()
    {
        return maxHeight;
    }

    public FlexItem setMaxHeight(float maxHeight)
    {
        this.maxHeight = maxHeight;
        return this;
    }

    public Integer getOrder()
    {
        return order;
    }

    public FlexItem setOrder(Integer order)
    {
        this.order = order;
        return this;
    }

    public int getGrow()
    {
        return grow;
    }

    public FlexItem setGrow(int grow)
    {
        this.grow = Math.max(grow, 0);
        return this;
    }

    public int getShrink()
    {
        return shrink;
    }

    public FlexItem setShrink(int shrink)
    {
        this.shrink = Math.max(shrink, 0);
        return this;
    }

    public FlexAlign getAlign()
    {
        return alignSelf;
    }

    public FlexItem setAlign(FlexAlign alignSelf)
    {
        this.alignSelf = alignSelf;
        return this;
    }

    public static class Builder
    {
        private final GuiElement element;

        // TODO: need to figure out good defaults for min/maxes
        private float minWidth = 5;
        private float minHeight = 5;
        private float maxWidth = Integer.MAX_VALUE;
        private float maxHeight = Integer.MAX_VALUE;

        private Integer order = null;
        private int grow = 0;
        private int shrink = 1;
        private FlexAlign alignSelf = null;

        public Builder(GuiElement element)
        {
            this.element = element;
        }

        public Builder minWidth(float minWidth)
        {
            this.minWidth = minWidth;
            return this;
        }

        public Builder maxWidth(float maxWidth)
        {
            this.maxWidth = maxWidth;
            return this;
        }

        public Builder minHeight(float minHeight)
        {
            this.minHeight = minHeight;
            return this;
        }

        public Builder maxHeight(float maxHeight)
        {
            this.maxHeight = maxHeight;
            return this;
        }

        public Builder order(Integer order)
        {
            this.order = order;
            return this;
        }

        public Builder grow(int grow)
        {
            this.grow = Math.max(grow, 0);
            return this;
        }

        public Builder shrink(int shrink)
        {
            this.shrink = Math.max(shrink, 0);
            return this;
        }

        public Builder align(FlexAlign alignSelf)
        {
            this.alignSelf = alignSelf;
            return this;
        }

        public FlexItem build()
        {
            return new FlexItem(this);
        }
    }
}
