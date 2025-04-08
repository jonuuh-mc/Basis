package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.util.Color;
import net.minecraft.client.audio.SoundHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class GuiContainer extends GuiElement
{
    // TODO: make this a map again?
    protected final List<GuiElement> children;

    protected GuiContainer(String elementName, float xPos, float yPos, float width, float height, Map<GuiColorType, Color> colorMap)
    {
        super(elementName, xPos, yPos, width, height);
        this.children = new ArrayList<>();

        if (colorMap != null)
        {
            this.colorMap = colorMap;
        }
    }

    protected GuiContainer(String elementName, float xPos, float yPos, float width, float height)
    {
        this(elementName, xPos, yPos, width, height, null);
    }

    public List<GuiElement> getChildren()
    {
        return children;
    }

    public boolean hasChildren()
    {
        return !children.isEmpty();
    }

    public boolean hasChild(GuiElement child)
    {
        return children.contains(child);
    }

    // TODO: only for debugging for now, needs to be removed
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

    public void addChild(GuiElement child)
    {
        if (child instanceof GuiRootContainer)
        {
            throw new IllegalArgumentException();
        }

//        if (child.getParent() != this)
//        {
        child.setParent(this);
//        }

        children.add(child);

        child.setInheritedXPos(this.worldXPos());
        child.setInheritedYPos(this.worldYPos());

        // TODO: can be broken if adding children in wrong order
        child.setZLevel(child.getNumParents());

//        boolean southBounds = (element.localYPos())
        // TODO: add handling for bounds of element being outside its parent? snap to relevant to inner parent edge?
    }

    public void addChildren(Collection<GuiElement> children)
    {
        for (GuiElement child : children)
        {
            addChild(child);
        }
    }

    public void addChildren(GuiElement... children)
    {
        for (GuiElement child : children)
        {
            addChild(child);
        }
    }

    @Override
    public void setLocalXPos(float xPos)
    {
        super.setLocalXPos(xPos);
        updateChildrenInheritedXPos();
    }

    @Override
    public void setLocalYPos(float yPos)
    {
        super.setLocalYPos(yPos);
        updateChildrenInheritedYPos();
    }

    @Override
    public void setInheritedXPos(float inheritedXPos)
    {
        super.setInheritedXPos(inheritedXPos);
        updateChildrenInheritedXPos();
    }

    @Override
    public void setInheritedYPos(float inheritedYPos)
    {
        super.setInheritedYPos(inheritedYPos);
        updateChildrenInheritedYPos();
    }

    protected void updateChildrenInheritedXPos()
    {
        for (GuiElement child : children)
        {
            child.setInheritedXPos(this.worldXPos());
        }
    }

    protected void updateChildrenInheritedYPos()
    {
        for (GuiElement child : children)
        {
            child.setInheritedYPos(this.worldYPos());
        }
    }

    // TODO: refactor this and inherited pos updates to use performAction()?
    @Override
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);

        for (GuiElement child : children)
        {
            child.setVisible(visible);
        }
    }

    @Override
    public GuiElement getGreatestZLevelHovered(GuiElement currGreatest)
    {
        currGreatest = super.getGreatestZLevelHovered(currGreatest);

        for (GuiElement child : children)
        {
            currGreatest = child.getGreatestZLevelHovered(currGreatest);
        }
        return currGreatest;
    }

    /**
     * Propagate some action which returns no result down the element tree starting from this container,
     * performing the action depth first in pre-order on every element along the way.
     * <p>
     * One reason the actions are performed in pre-order is for propagating a screen draw event;
     * parents should be drawn before their children so that they appear visually behind the children
     *
     * @param action The action to be performed
     */
    @Override
    public void performAction(Consumer<GuiElement> action)
    {
        // Apply the action to this element
        super.performAction(action);

        for (GuiElement child : children)
        {
            // Continue propagating the action to any additional children
            child.performAction(action);
        }
    }

    @Override
    protected void playClickSound(SoundHandler soundHandler)
    {
    }

    //    @Override
//    public void onScreenDraw(float mouseX, float mouseY, float partialTicks)
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
