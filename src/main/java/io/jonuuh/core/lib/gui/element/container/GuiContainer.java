package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.util.Color;
import net.minecraft.client.audio.SoundHandler;

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
            elements.add(element);

            if (element instanceof GuiContainer)
            {
                elements.addAll(((GuiContainer) element).getNestedChildren());
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

//        boolean southBounds = (element.localYPos())

        // TODO: add handling for bounds of element being outside its parent? snap to relevant to inner parent edge?
    }

//    public void addChildren(Collection<GuiElement> children)
//    {
//        for (GuiElement child : children)
//        {
//            addChild(child);
//        }
//    }

    @Override
    public void setLocalXPos(int xPos)
    {
        super.setLocalXPos(xPos);
        updateChildrenInheritedXPos();
//        System.out.println(elementName + " xPos: " + xPos + " initX: " + initialXPos + " inheritedX: " + inheritedXPos);
    }

    @Override
    public void setLocalYPos(int yPos)
    {
        super.setLocalYPos(yPos);
        updateChildrenInheritedYPos();
//        System.out.println(elementName + " yPos: " + yPos + " initY " + initialYPos + " inheritedY: " + inheritedYPos);
    }

    protected void updateChildrenInheritedXPos()
    {
        for (GuiElement child : this.getChildren())
        {
            // TODO: whether this is before or after "recursive" call should be VERY critical (pos inheritance in wrong order?); check in debugger?
            child.setInheritedXPos(this.worldXPos());

            if (child instanceof GuiContainer)
            {
                ((GuiContainer) child).updateChildrenInheritedXPos();
            }
        }
    }

    protected void updateChildrenInheritedYPos()
    {
        for (GuiElement child : this.getChildren())
        {
            // TODO: whether this is before or after "recursive" call should be VERY critical (pos inheritance in wrong order?); check in debugger?
            child.setInheritedYPos(this.worldYPos());

            if (child instanceof GuiContainer)
            {
                ((GuiContainer) child).updateChildrenInheritedYPos();
            }
        }
    }

    @Override
    protected void playClickSound(SoundHandler soundHandler)
    {
    }

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
