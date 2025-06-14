package io.jonuuh.core.lib.gui.properties;

public class Spacing
{
    private float top;
    private float right;
    private float bottom;
    private float left;

    public Spacing(float top, float right, float bottom, float left)
    {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    /**
     * Construct a Spacing with the same amount of spacing on all four sides.
     */
    public Spacing(float spacing)
    {
        this(spacing, spacing, spacing, spacing);
    }

    /**
     * Construct a Spacing proportional to an element's size.
     *
     * @param elementWidth An element's width
     * @param elementHeight An element's height
     * @param scale What fraction of the element's dimensions should be used for spacing,
     * expected to be a very small value for example 0.05 for 5%
     */
    public Spacing(float elementWidth, float elementHeight, float scale)
    {
        this(elementHeight * scale, elementWidth * scale, elementHeight * scale, elementWidth * scale);
    }

    public float getTop()
    {
        return top;
    }

    public void setTop(float top)
    {
        this.top = top;
    }

    public float getRight()
    {
        return right;
    }

    public void setRight(float right)
    {
        this.right = right;
    }

    public float getBottom()
    {
        return bottom;
    }

    public void setBottom(float bottom)
    {
        this.bottom = bottom;
    }

    public float getLeft()
    {
        return left;
    }

    public void setLeft(float left)
    {
        this.left = left;
    }
}
