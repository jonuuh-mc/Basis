package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
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


        // TODO: inefficient? fix for zLevel being broken if adding a child to a container,
        //  then adding that container as a child to another container (zlevel of first child wouldn't be
        //  aware of the grandparent because it doesn't exist yet when its added to its parent)

        // TODO: make setZLevel overridden in container similarly to setInheritedPos? would that solve this problem?
        this.performAction(element -> element.setZLevel(element.getNumParents()));


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

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (debug && hasChildren())
        {
            Color padColor = new Color("#36ff0000");
            // Left
            RenderUtils.drawRectangle(worldXPos(), worldYPos(), padding.getLeft(), getHeight(), padColor);
            // Right
            RenderUtils.drawRectangle(worldXPos() + getWidth() - padding.getRight(), worldYPos(), padding.getRight(), getHeight(), padColor);
            // Top
            RenderUtils.drawRectangle(worldXPos(), worldYPos(), getWidth(), padding.getTop(), padColor);
            // Bottom
            RenderUtils.drawRectangle(worldXPos(), worldYPos() + getHeight() - padding.getBottom(), getWidth(), padding.getBottom(), padColor);
        }
    }
}
