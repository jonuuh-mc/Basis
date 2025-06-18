package io.jonuuh.core.lib.gui.element.container.flex;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexAlign;

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
    /** Part of how much weight this element has when the items along the main axis are shrunk.
     * A value of 1 is default, a value of 0 means this element will not shrink when there is negative free space */
    private int shrink;
    /** How this item should be aligned along the cross axis */
    private FlexAlign alignSelf;

    public FlexItem(GuiElement element, float minWidth, float maxWidth, float minHeight, float maxHeight, Integer order, int grow, int shrink, FlexAlign alignSelf)
    {
        this.element = element;

        this.initWidth = element.getWidth();
        this.initHeight = element.getHeight();

        setMinWidth(minWidth);
        setMaxWidth(maxWidth);
        setMinHeight(minHeight);
        setMaxHeight(maxHeight);

        setOrder(order);
        setGrow(grow);
        setShrink(shrink);
        setAlign(alignSelf);
    }

    public FlexItem(GuiElement element, float minWidth, float maxWidth, float minHeight, float maxHeight)
    {
        this(element, minWidth, maxWidth, minHeight, maxHeight, null, 0, 1, null);
    }

    public FlexItem(GuiElement element, Integer order, int grow, int shrink, FlexAlign alignSelf)
    {
        this(element, 5, Integer.MAX_VALUE, 5, Integer.MAX_VALUE, order, grow, shrink, alignSelf);
    }

    public FlexItem(GuiElement element)
    {
        // TODO: need to figure out good defaults for min/maxes
        this(element, 5, Integer.MAX_VALUE, 5, Integer.MAX_VALUE);
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
}
