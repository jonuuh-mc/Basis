package io.jonuuh.core.lib.gui.element;

public final class Padding
{
    private float topPadding;
    private float rightPadding;
    private float bottomPadding;
    private float leftPadding;

    public Padding(float topPadding, float rightPadding, float bottomPadding, float leftPadding)
    {
        this.topPadding = topPadding;
        this.rightPadding = rightPadding;
        this.bottomPadding = bottomPadding;
        this.leftPadding = leftPadding;
    }

    public float getTopPadding()
    {
        return topPadding;
    }

    public void setTopPadding(float topPadding)
    {
        this.topPadding = topPadding;
    }

    public float getRightPadding()
    {
        return rightPadding;
    }

    public void setRightPadding(float rightPadding)
    {
        this.rightPadding = rightPadding;
    }

    public float getBottomPadding()
    {
        return bottomPadding;
    }

    public void setBottomPadding(float bottomPadding)
    {
        this.bottomPadding = bottomPadding;
    }

    public float getLeftPadding()
    {
        return leftPadding;
    }

    public void setLeftPadding(float leftPadding)
    {
        this.leftPadding = leftPadding;
    }
}
