package io.jonuuh.basis.lib.gui.element.container;

import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.element.container.behavior.FlexBehavior;
import io.jonuuh.basis.lib.gui.element.container.behavior.ScrollBehavior;
import io.jonuuh.basis.lib.gui.event.GuiEvent;
import io.jonuuh.basis.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.basis.lib.gui.event.input.MouseScrollEvent;
import io.jonuuh.basis.lib.gui.event.lifecycle.InitGuiEvent;
import io.jonuuh.basis.lib.gui.listener.input.MouseClickListener;
import io.jonuuh.basis.lib.gui.listener.input.MouseScrollListener;
import io.jonuuh.basis.lib.gui.listener.lifecycle.InitGuiListener;
import io.jonuuh.basis.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.lib.util.CollectionUtils;
import io.jonuuh.basis.lib.util.Color;
import io.jonuuh.basis.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class GuiContainer extends GuiElement implements InitGuiListener, MouseClickListener, MouseScrollListener
{
    protected final List<GuiElement> children;
    protected FlexBehavior flexBehavior;
    protected ScrollBehavior scrollBehavior;
    protected boolean shouldScissor;
    protected boolean enabled;
    protected boolean mouseDown;

    protected GuiContainer(AbstractBuilder<?, ?> builder)
    {
        super(builder);
        this.children = new ArrayList<>();
        this.enabled = builder.enabled;

        this.shouldScissor = builder.shouldScissor;

        addChildren(builder.children);

        if (builder.scrollBehaviorBuilder != null)
        {
            this.scrollBehavior = builder.scrollBehaviorBuilder.host(this).build();

            if (this.getPadding().right() < scrollBehavior.getSlider().getWidth())
            {
                this.getPadding().setRight(scrollBehavior.getSlider().getWidth());
            }
        }

        if (builder.flexBehaviorBuilder != null)
        {
            builder.flexBehaviorBuilder.host(this);

            if (scrollBehavior != null)
            {
                builder.flexBehaviorBuilder.mainAxisSize(scrollBehavior.getSlider().getMax());
            }

            this.flexBehavior = builder.flexBehaviorBuilder.build();

            // Note that normal children will be before flex item children in children list. Currently, the ordering of children
            // (not flex items) within a container has no meaning, but if it does later maybe this will be important
            addChildren(flexBehavior.getElements());
        }
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

    public FlexBehavior getFlexBehavior()
    {
        return flexBehavior;
    }

    public void setFlexBehavior(FlexBehavior flexBehavior)
    {
        this.flexBehavior = flexBehavior;
    }

    public ScrollBehavior getScrollBehavior()
    {
        return scrollBehavior;
    }

    public void setScrollBehavior(ScrollBehavior scrollBehavior)
    {
        this.scrollBehavior = scrollBehavior;
    }

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

    @Override
    public boolean isMouseDown()
    {
        return mouseDown;
    }

    @Override
    public void setMouseDown(boolean mouseDown)
    {
        this.mouseDown = mouseDown;
    }

    protected boolean shouldScissor()
    {
        return shouldScissor;
    }

    public void addChild(GuiElement child)
    {
        addChild(child, children.size());
    }

    public void addChild(GuiElement child, int index)
    {
        if (children.contains(child))
        {
            return;
        }

        children.add(index, child);

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
        // TODO: because of this, any container that is invisible will make all its children invisible too.
        //  is this a problem or should this be the intended behavior
        if (!isVisible())
        {
            return;
        }

        // TODO: dumb solution to making root invisible?
        //  split only the actual draw textured rect into a protected funct which can be overridden to prevent drawing?
        //  still drawing debug info for root is prob okay
        if (!(this instanceof GuiRootContainer))
        {
            // Handle screen draw for this element
            RenderUtils.drawRoundedRectWithBorder(worldXPos(), worldYPos(), getWidth(), getHeight(), getCornerRadius(), 1, getColor(GuiColorType.BACKGROUND), getColor(GuiColorType.BORDER));

            if (debug && flexBehavior != null)
            {
                flexBehavior.drawInspector();
            }

            super.onScreenDraw(mouseX, mouseY, partialTicks);
        }

        drawChildren(mouseX, mouseY, partialTicks);
    }

    protected void drawChildren(int mouseX, int mouseY, float partialTicks)
    {
        List<GuiContainer> containers = new ArrayList<>();
        collectScissoringParents(containers);

        int scissorX = 0;
        int scissorY = 0;
        int scissorWidth = 0;
        int scissorHeight = 0;

        if (!containers.isEmpty())
        {
            int greatestLeftBound = (int) CollectionUtils.getMax(containers, Comparator.comparingDouble(GuiElement::getLeftBound)).getLeftBound();
            int greatestTopBound = (int) CollectionUtils.getMax(containers, Comparator.comparingDouble(GuiElement::getTopBound)).getTopBound();

            int leastRightBound = (int) CollectionUtils.getMin(containers, Comparator.comparingDouble(GuiElement::getRightBound)).getRightBound();
            int leastBottomBound = (int) CollectionUtils.getMin(containers, Comparator.comparingDouble(GuiElement::getBottomBound)).getBottomBound();

            scissorX = greatestLeftBound;
            scissorY = greatestTopBound;
            scissorWidth = leastRightBound - greatestLeftBound;
            scissorHeight = leastBottomBound - greatestTopBound;
        }

        for (GuiElement child : children)
        {
            if (!containers.isEmpty())
            {
                // Prevent trying to scissor an area with zero width/height
                if (scissorWidth <= 0 || scissorHeight <= 0)
                {
                    return;
                }

                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_SCISSOR_TEST);

//                RenderUtils.drawRectangle(scissorX, scissorY, scissorWidth, scissorHeight, new Color("#4400ff00"));
                RenderUtils.scissorFromTopLeft(scissorX, scissorY, scissorWidth, scissorHeight);
            }

            // If the child is another container, this will continue propagating the draw to any additional children
            child.onScreenDraw(mouseX, mouseY, partialTicks);

            if (!containers.isEmpty())
            {
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                GL11.glPopMatrix();
            }
        }
    }

    protected void collectScissoringParents(List<GuiContainer> containers)
    {
        if (shouldScissor())
        {
            containers.add(this);
        }

        if (hasParent())
        {
            getParent().collectScissoringParents(containers);
        }
    }

    @Override
    public void onInitGui(InitGuiEvent event)
    {
        if (flexBehavior != null)
        {
            flexBehavior.updateItemsLayout();
        }

        if (scrollBehavior != null)
        {
            scrollBehavior.updateSlider();
        }
    }

    @Override
    public void onMouseDown(MouseDownEvent event)
    {
        MouseClickListener.super.onMouseDown(event);
    }

    /**
     * Redirect mouse wheel scroll events to the scroll behavior's slider, if it exists.
     */
    @Override
    public void onMouseScroll(MouseScrollEvent event)
    {
        // Ignore propagating events to other containers
        if (event.target != this)
        {
            return;
        }

        if (scrollBehavior != null)
        {
            scrollBehavior.getSlider().onMouseScroll(event);
        }
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

    // TODO: make children a map if using this becomes common enough?
    @Override
    public GuiElement getElementByName(String elementName)
    {
        GuiElement element = super.getElementByName(elementName);

        if (element != null)
        {
            return element;
        }

        for (GuiElement child : children)
        {
            GuiElement element1 = child.getElementByName(elementName);

            if (element1 != null)
            {
                return element1;
            }
        }
        return null;
    }

    protected static abstract class AbstractBuilder<T extends AbstractBuilder<T, R>, R extends GuiContainer> extends GuiElement.AbstractBuilder<T, R>
    {
        protected final List<GuiElement> children = new ArrayList<>();
        protected FlexBehavior.Builder flexBehaviorBuilder = null;
        protected ScrollBehavior.Builder scrollBehaviorBuilder = null;
        protected boolean shouldScissor = false;
        protected boolean enabled = true;

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

        public T flexBehavior(FlexBehavior.Builder flexBehaviorBuilder)
        {
            this.flexBehaviorBuilder = flexBehaviorBuilder;
            return self();
        }

        public T scrollBehavior(ScrollBehavior.Builder scrollBehaviorBuilder)
        {
            this.scrollBehaviorBuilder = scrollBehaviorBuilder;
            this.shouldScissor = true;
            return self();
        }

        public T enabled(boolean enabled)
        {
            this.enabled = enabled;
            return self();
        }
    }
}
