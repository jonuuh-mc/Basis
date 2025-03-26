package io.jonuuh.core.lib.config.gui.elements.container;

import io.jonuuh.core.lib.config.gui.GuiColorType;
import io.jonuuh.core.lib.config.gui.elements.GuiElement;
import io.jonuuh.core.lib.util.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GuiContainer extends GuiElement
{
    protected final List<GuiElement> children;
    protected float outerRadius;
    protected float innerRadius;

    protected GuiContainer(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, float outerRadius, float innerRadius, Map<GuiColorType, Color> colorMap)
    {
        super(parent, elementName, xPos, yPos, width, height);
        this.children = new ArrayList<>();
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;

        if (colorMap != null)
        {
            this.colorMap = colorMap;
        }
    }

    protected GuiContainer(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, float outerRadius, float innerRadius)
    {
        this(parent, elementName, xPos, yPos, width, height, outerRadius, innerRadius, null);
    }

    public List<GuiElement> getChildren()
    {
        return children;
    }

    public boolean hasChildren()
    {
        return !children.isEmpty();
    }

    public List<GuiElement> getNestedChildren()
    {
        List<GuiElement> elements = new ArrayList<>();

        for (GuiElement element : children)
        {
            if (element instanceof GuiContainer)
            {
                elements.add(element);
                elements.addAll(((GuiContainer) element).getNestedChildren());
            }
            else
            {
                elements.add(element);
            }
        }

        return elements;
    }

    public float getOuterRadius()
    {
        return outerRadius;
    }

    public float getInnerRadius()
    {
        return innerRadius;
    }

    public void addChild(GuiElement element)
    {
        children.add(element);

//        element.xPos += this.xPos;
//        element.yPos += this.yPos;
//        element.xPosInit += this.xPosInit;
//        element.yPosInit += this.yPosInit;
//        element.zLevel = element.getNumParents();
    }

//    public void addChildren(Collection<GuiElement> children)
//    {
//        for (GuiElement child : children)
//        {
//            addChild(child);
//        }
//    }

    // TODO: temp commented out
//    @Override
//    public void setXPos(int xPos)
//    {
//        this.xPos = xPos;
//
//        for (GuiElement element : getNestedChildren())
//        {
//            element.setXPos(element.xPosInit + xPos - this.xPosInit);
//        }
//    }
//
//    @Override
//    public void setYPos(int yPos)
//    {
//        this.yPos = yPos;
//
//        for (GuiElement element : getNestedChildren())
//        {
//            element.setYPos(element.yPosInit + yPos - this.yPosInit);
//        }
//    }

//    @Override
//    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
//    {
////        GL11.glPushMatrix();
////        GL11.glEnable(GL11.GL_SCISSOR_TEST);
////        RenderUtils.scissorFromTopLeft(xPos, yPos, width, height);
//
//        super.onScreenDraw(mouseX, mouseY, partialTicks);
//
////        GL11.glDisable(GL11.GL_SCISSOR_TEST);
////        GL11.glPopMatrix();
//        childrenMap.values().forEach(element -> element.onScreenDraw(mouseX, mouseY, partialTicks));
//
////        for (GuiElement element : getNestedChildren())
////        {
////            if (!(element instanceof GuiContainer))
////            {
////                element.onScreenDraw(mouseX, mouseY, partialTicks);
////            }
////            else
////            {
////                super.onScreenDraw(mouseX, mouseY, partialTicks);
//////                System.out.println(element.elementName + " " + element.getNumParents());
////                if (element.getNumParents() == 0)
////                {
////                    super.onScreenDraw(mouseX, mouseY, partialTicks);
////
//////                    element.onScreenDraw(mouseX, mouseY, partialTicks);
////                }
////            }
////        }
//
//    }
}
