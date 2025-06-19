package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.event.GuiEvent;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.audio.SoundHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class GuiContainer extends GuiElement
{
    protected final List<GuiElement> children;

    protected GuiContainer(AbstractBuilder<?, ?> builder)
    {
        super(builder);

        this.children = new ArrayList<>();
        addChildren(builder.children);
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
        if (children.contains(child))
        {
            return;
        }

        children.add(child);

        // If this function was called independently rather than being called via
        // GuiElement#setParent(), make the child aware that this is now its parent
        if (this != child.getParent())
        {
            child.setParent(this);
        }

        child.setInheritedXPos(this.worldXPos());
        child.setInheritedYPos(this.worldYPos());


        // TODO: inefficient? fix for zLevel being broken if adding a child to a container,
        //  then adding that container as a child to another container (zlevel of first child wouldn't be
        //  aware of the grandparent because it doesn't exist yet when its added to its parent)

        // TODO: make setZLevel overridden in container similarly to setInheritedPos? would that solve this problem?
        this.performAction(element -> element.setZLevel(element.getNumParents()));
    }

    public void removeChild(GuiElement child)
    {
        if (!children.contains(child))
        {
            return;
        }

        children.remove(child);

        // If this function was called independently rather than being called via
        // GuiElement#setParent(), make the child aware that it no longer has a parent
        if (this == child.getParent())
        {
            child.setParent(null);
        }
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
    public void propagateEvent(GuiEvent event)
    {
        super.propagateEvent(event);

        for (GuiElement child : children)
        {
            child.propagateEvent(event);
        }
    }

    @Override
    public void collectMatchingElements(List<GuiElement> collector, Predicate<GuiElement> predicate)
    {
        super.collectMatchingElements(collector, predicate);

        for (GuiElement child : children)
        {
            child.collectMatchingElements(collector, predicate);
        }
    }

    protected static abstract class AbstractBuilder<T extends AbstractBuilder<T, R>, R extends GuiContainer> extends GuiElement.AbstractBuilder<T, R>
    {
        protected final List<GuiElement> children = new ArrayList<>();
        protected boolean shouldScissor = false;

        protected AbstractBuilder(String elementName)
        {
            super(elementName);
        }

        public T colorMap(Map<GuiColorType, Color> colorMap)
        {
            this.colorMap = colorMap;
            return self();
        }

        public T scissor(boolean shouldScissor)
        {
            this.shouldScissor = shouldScissor;
            return self();
        }

        public T child(GuiElement child)
        {
            this.children.add(child);
            return self();
        }

        public T children(Collection<GuiElement> children)
        {
            this.children.addAll(children);
            return self();
        }

        public T children(GuiElement... children)
        {
            this.children.addAll(Arrays.asList(children));
            return self();
        }
    }
}
