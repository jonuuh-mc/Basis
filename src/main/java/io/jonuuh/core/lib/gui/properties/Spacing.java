package io.jonuuh.core.lib.gui.properties;

public class Spacing
{
    private float left;
    private float right;
    private float top;
    private float bottom;

    public Spacing(float left, float right, float top, float bottom)
    {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
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
        this(elementWidth * scale, elementWidth * scale, elementHeight * scale, elementHeight * scale);
    }

    public float left()
    {
        return left;
    }

    public void setLeft(float left)
    {
        this.left = left;
    }

    public float right()
    {
        return right;
    }

    public void setRight(float right)
    {
        this.right = right;
    }

    public float top()
    {
        return top;
    }

    public void setTop(float top)
    {
        this.top = top;
    }

    public float bottom()
    {
        return bottom;
    }

    public void setBottom(float bottom)
    {
        this.bottom = bottom;
    }

    @Override
    public String toString()
    {
        return "Spacing{" + "l=" + left + ", r=" + right + ", t=" + top + ", b=" + bottom + '}';
    }
}
