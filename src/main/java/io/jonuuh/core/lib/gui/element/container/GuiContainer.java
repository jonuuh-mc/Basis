package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.event.GuiEvent;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

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
    protected boolean shouldScissor;
    protected ScissorBox scissorBox;
    private boolean enabled = true; // TODO:

    protected GuiContainer(AbstractBuilder<?, ?> builder)
    {
        super(builder);
        this.children = new ArrayList<>();
        this.shouldScissor = builder.shouldScissor;
        this.scissorBox = new ScissorBox(this);
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

//    public FlexBehavior getFlexBehavior()
//    {
//        return flexBehavior;
//    }

//    public void setFlexBehavior(FlexBehavior flexBehavior)
//    {
//        this.flexBehavior = flexBehavior;
//    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public ScissorBox getScissorBox()
    {
        return scissorBox;
    }

    protected boolean shouldScissor()
    {
        return shouldScissor && hasChildren();
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
        // TODO: optionally inherit different properties? could make some sort of child properties object which is set
        //  through the child() method of the container builder?
//        child.setVisible(this.isVisible());

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
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        // Handle screen draw for this element
        super.onScreenDraw(mouseX, mouseY, partialTicks);
//        if (debug && hasChildren())
//        {
//            Color padColor = new Color("#36ff0000");
//            // Left
//            RenderUtils.drawRectangle(worldXPos(), worldYPos(), getPadding().getLeft(), getHeight(), padColor);
//            // Right
//            RenderUtils.drawRectangle(worldXPos() + getWidth() - getPadding().getRight(), worldYPos(), getPadding().getRight(), getHeight(), padColor);
//            // Top
//            RenderUtils.drawRectangle(worldXPos(), worldYPos(), getWidth(), getPadding().getTop(), padColor);
//            // Bottom
//            RenderUtils.drawRectangle(worldXPos(), worldYPos() + getHeight() - getPadding().getBottom(), getWidth(), getPadding().getBottom(), padColor);
//        }

        boolean scissoring = false;

        if (this.shouldScissor())
        {
            int greatestLeft = (int) getGreatestLeftBound();
            int leastRight = (int) getLeastRightBound();

            int greatestTopBound = (int) getGreatestTopBound();
            int leastBottom = (int) getLeastBottomBound();

            if (greatestLeft < leastRight && greatestTopBound < leastBottom)
            {
                int boundWidth = leastRight - greatestLeft;
                int boundHeight = leastBottom - greatestTopBound;

                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                RenderUtils.drawRectangle(greatestLeft, greatestTopBound, boundWidth, boundHeight, new Color("#220000ff"));
                RenderUtils.scissorFromTopLeft(greatestLeft, greatestTopBound, boundWidth, boundHeight);
                scissoring = true;
            }
        }

        for (GuiElement child : children)
        {
            // Continue propagating the screen draw event to any additional children
            child.onScreenDraw(mouseX, mouseY, partialTicks);
        }

        if (scissoring)
        {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }
    }

    protected float getGreatestLeftBound()
    {
        return hasParent() ? Math.max(parent.getGreatestLeftBound(), this.getLeftBound()) : this.getLeftBound();
    }

    protected float getLeastRightBound()
    {
        return hasParent() ? Math.min(parent.getLeastRightBound(), this.getRightBound()) : this.getRightBound();
    }

    protected float getGreatestTopBound()
    {
        return hasParent() ? Math.max(parent.getGreatestTopBound(), this.getTopBound()) : this.getTopBound();
    }

    protected float getLeastBottomBound()
    {
        return hasParent() ? Math.min(parent.getLeastBottomBound(), this.getBottomBound()) : this.getBottomBound();
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
