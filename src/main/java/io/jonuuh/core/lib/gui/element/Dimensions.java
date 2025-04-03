package io.jonuuh.core.lib.gui.element;

/**
 * Only should be used within the context of gui elements
 */
public final class Dimensions
{
    /** Minimum width of this element */
    public float minWidth;
    /** Width of this element */
    public float width;
    /** Maximum width of this element */
    public float maxWidth;

    /** Minimum height of this element */
    public float minHeight;
    /** Height of this element */
    public float height;
    /** Maximum height of this element */
    public float maxHeight;

    public Dimensions(float minWidth, float width, float maxWidth, float minHeight, float height, float maxHeight)
    {
        this.minWidth = minWidth;
        this.width = width;
        this.maxWidth = maxWidth;
        this.minHeight = minHeight;
        this.height = height;
        this.maxHeight = maxHeight;
    }

    // TODO: look into min with flex
    public Dimensions(float width, float height)
    {
        this(10, width, width, 10, height, height);
    }

    public Dimensions()
    {
        this(100, 20);
    }
}
