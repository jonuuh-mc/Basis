package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class GuiFlexContainer extends GuiContainer
{
    protected int freeWidth;
    protected int freeHeight;
    protected boolean isVertical;

    public GuiFlexContainer(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, float outerRadius, float innerRadius, Map<GuiColorType, Color> colorMap)
    {
        super(parent, elementName, xPos, yPos, width, height, outerRadius, innerRadius, colorMap);
    }

    public GuiFlexContainer(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, float outerRadius, float innerRadius)
    {
        super(parent, elementName, xPos, yPos, width, height, outerRadius, innerRadius);
    }

    public boolean isVertical()
    {
        return isVertical;
    }

    public void setVertical(boolean vertical)
    {
        isVertical = vertical;
    }

    @Override
    public void addChild(GuiElement element)
    {
        super.addChild(element);
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), width, height, innerRadius, getColor(GuiColorType.BACKGROUND), true);
    }

    @Override
    protected void onKeyTyped(char typedChar, int keyCode)
    {
//        if (keyCode == Keyboard.KEY_RETURN)
//        {
//            children.get(0).setXPos(children.get(0).getXPosInit() + (62 * 1));
//            children.get(1).setXPos(children.get(1).getXPosInit() + children.get(0).getWidth() + (62 * 2));
//        }
//        else if (keyCode == Keyboard.KEY_BACK)
//        {
//            children.get(0).setXPos(children.get(0).getXPosInit());
//            children.get(1).setXPos(children.get(1).getXPosInit());
//        }
    }

    @Override
    protected void onInitGui(int guiScreenWidth, int guiScreenHeight)
    {
        if (hasParent())
        {
            // TODO: this needs to be totally rewritten, not really doing much, pushes in width/height of containers when the side of the gui pushes it
            //  could be better with a change to root gui flexing and addition of padding?

//            (width / 2) - (child.getWidth() / 2)

//            int parFreeWidth = (parent.getWidth() - width);
//            int parFreeHeight = (parent.getHeight() - height);

//            width = Math.min(parent.getWidth() - worldXPos() - parent.worldXPos() + 20, initialWidth);
//            height = Math.min(parent.getHeight() - worldYPos() - parent.worldYPos(), initialHeight);

            width = Math.min(parent.getWidth() - (worldXPos() - parent.worldXPos() + 20), initialWidth);
            height = Math.min(parent.getHeight() - (worldYPos() - parent.worldYPos()), initialHeight);
        }
        // root container
        else
        {
            int xPadding = guiScreenWidth / 10;
            int yPadding = guiScreenHeight / 10;

            setLocalXPos(Math.max((guiScreenWidth / 2) - (getInitialWidth() / 2), xPadding));
            setLocalYPos(Math.max((guiScreenHeight / 2) - (getInitialHeight() / 2), yPadding));

            setWidth(Math.min(guiScreenWidth - localXPos() - xPadding, getInitialWidth()));
            setHeight(Math.min(guiScreenHeight - localYPos() - yPadding, getInitialHeight()));
        }

        if (isVertical)
        {
            flexVertical(guiScreenWidth, guiScreenHeight);

            for (GuiElement child : children)
            {
                child.setLocalXPos((width / 2) - (child.getWidth() / 2));
            }
        }
        else
        {
            flexHorizontal(guiScreenWidth, guiScreenHeight);

            for (GuiElement child : children)
            {
                child.setLocalYPos((height / 2) - (child.getHeight() / 2));
            }
        }
    }

    private void flexVertical(int guiScreenWidth, int guiScreenHeight)
    {
//        if (hasParent())
//        {
//            height = Math.min(parent.getHeight() - (worldYPos() - parent.worldYPos()), initialHeight);
//        }
//        else
//        {
//            height = Math.min(guiScreenHeight - worldYPos() - (guiScreenHeight / 10), initialHeight);
//        }

//        height = Math.min(parent.getHeight() - (worldYPos() - parent.worldYPos()), initialHeight);
//        height = Math.min(guiScreenHeight - worldYPos() - thisPadding, initialHeight);

        freeHeight = height - children.stream().mapToInt(GuiElement::getHeight).sum();
        int heightPerSpace = freeHeight / (1 + children.size());

//        System.out.println(height + " " + elemHeightSum + " " + freeHeight + " " + spaces + " " + heightPerSpace);

        int heightCovered = 0;
        for (int i = 0; i < children.size(); i++)
        {
            GuiElement child = children.get(i);
//            System.out.println(child + " " + child.getInheritedXPos());
            child.setLocalYPos(/*child.getInheritedYPos() +*/ heightCovered + (heightPerSpace * (i + 1)));
            heightCovered += child.getHeight();
        }
//        System.out.println(guiScreenWidth + " " + (xPos + width) + " " + (guiScreenWidth - xPos));
    }

    private void flexHorizontal(int guiScreenWidth, int guiScreenHeight)
    {
//        if (hasParent())
//        {
//            width = Math.min(parent.getWidth() - (worldXPos() - parent.worldXPos()), initialWidth);
//        }
//        else
//        {
//            width = Math.min(guiScreenWidth - worldXPos() - (guiScreenWidth / 10), initialWidth);
//        }

//        width = Math.min(parent.getWidth() - (worldXPos() - parent.worldXPos()), initialWidth);

        freeWidth = width - children.stream().mapToInt(GuiElement::getWidth).sum();
        int widthPerSpace = freeWidth / (1 + children.size());

//        System.out.println(width + " " + elemWidthSum + " " + freeWidth + " " + spaces + " " + widthPerSpace);

        int widthCovered = 0;
        for (int i = 0; i < children.size(); i++)
        {
            GuiElement child = children.get(i);
//            System.out.println(child + " " + child.getInheritedXPos());
            child.setLocalXPos(/*child.getInheritedXPos() +*/ widthCovered + (widthPerSpace * (i + 1)));
            widthCovered += child.getWidth();
        }
//        System.out.println(guiScreenWidth + " " + (xPos + width) + " " + (guiScreenWidth - xPos));
    }
}
