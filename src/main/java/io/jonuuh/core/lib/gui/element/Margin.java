package io.jonuuh.core.lib.gui.element;

public final class Margin
{
    private float topMargin;
    private float rightMargin;
    private float bottomMargin;
    private float leftMargin;

    public Margin(float topMargin, float rightMargin, float bottomMargin, float leftMargin)
    {
        this.topMargin = topMargin;
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
        this.leftMargin = leftMargin;
    }

    public float getTopMargin()
    {
        return topMargin;
    }

    public void setTopMargin(float topMargin)
    {
        this.topMargin = topMargin;
    }

    public float getRightMargin()
    {
        return rightMargin;
    }

    public void setRightMargin(float rightMargin)
    {
        this.rightMargin = rightMargin;
    }

    public float getBottomMargin()
    {
        return bottomMargin;
    }

    public void setBottomMargin(float bottomMargin)
    {
        this.bottomMargin = bottomMargin;
    }

    public float getLeftMargin()
    {
        return leftMargin;
    }

    public void setLeftMargin(float leftMargin)
    {
        this.leftMargin = leftMargin;
    }
}
