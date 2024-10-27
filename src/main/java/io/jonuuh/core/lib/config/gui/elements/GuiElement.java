package io.jonuuh.core.lib.config.gui.elements;

public abstract class GuiElement
{
    protected int xPos;
    protected int yPos;
    protected int width;
    protected int height;
    protected boolean visible;

    protected GuiElement(int xPos, int yPos, int width, int height, boolean visible)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.visible = visible;
    }

    public int getXPos()
    {
        return xPos;
    }

    public int getYPos()
    {
        return yPos;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setXPos(int xPos)
    {
        this.xPos = xPos;
    }

    public void setYPos(int yPos)
    {
        this.yPos = yPos;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}
