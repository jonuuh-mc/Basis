package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.AbstractGuiScreen;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import io.jonuuh.core.lib.gui.element.container.GuiRootContainer;
import io.jonuuh.core.lib.gui.event.GuiEvent;
import io.jonuuh.core.lib.gui.listener.input.InputListener;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.gui.properties.Spacing;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class GuiElement
{
    /** A reference to the static minecraft instance */
    public static final Minecraft mc = Minecraft.getMinecraft();
    protected static final ResourceLocation resourceBackgroundTex = new ResourceLocation("core:textures/background.png");
    protected static final float DEFAULT_WIDTH = 100;
    protected static final float DEFAULT_HEIGHT = 20;

    /** A readable name qualifying this element TODO: no enforcement of this being unique */
    public final String elementName;
    /**
     * This element's immediate ancestor: the container it's inside.
     * Should only ever be null for the root container of the element tree, otherwise
     * the element will be inaccessible via any events dispatched by the GuiScreen
     */
    protected GuiContainer parent;

    /** A map of colors that may be used by this element */
    protected Map<GuiColorType, Color> colorMap;

    /** Element x position within its parent */
    private float localXPos;
    /** Element y position within its parent */
    private float localYPos;
    /** The sum of all world xPos from this element's ancestors */
    private float inheritedXPos;
    /** The sum of all world yPos from this element's ancestors */
    private float inheritedYPos;

    /** Width of this element */
    private float width;
    /** Height of this element */
    private float height;

    /** Which symbolic layer this element is on, should be equal to how many parents this element has */
    private int zLevel;
    /** Whether this element is visible (should be drawn to screen) */
    private boolean visible;
    /** Whether this element is currently hovered; Updated constantly via onScreenDraw (not via onScreenTick because mouse pos is needed) */
    private boolean hovered;

    private Spacing margin;
    private Spacing padding;

    private String tooltipStr;

    protected boolean debug;

    protected GuiElement(AbstractBuilder<?, ?> builder)
    {
        this.elementName = builder.elementName;

        this.localXPos = builder.localXPos;
        this.localYPos = builder.localYPos;

        this.width = builder.width;
        this.height = builder.height;

        this.padding = builder.padding;
        this.margin = builder.margin;

        this.visible = builder.visible;

        this.colorMap = builder.colorMap;

        this.debug = true;

        if (builder.parent != null)
        {
            setParent(builder.parent);
        }
    }

    public GuiContainer getParent()
    {
        return parent;
    }

    /**
     * Set the parent of this GuiElement, handling any relationships between this element and the parent
     *
     * @param parent {@link GuiElement#parent}
     */
    public void setParent(GuiContainer parent)
    {
        // Temporarily hold previous parent, doesn't matter if it was null (should usually be the case)
        GuiContainer prevParent = this.parent;

        // Assign new parent
        this.parent = parent;

        // If this function was called independently rather than being called via
        // GuiContainer#removeChild(), make the parent aware that this is no longer its child
        if (prevParent != null && prevParent.hasChild(this))
        {
            prevParent.removeChild(this);
        }

        // If this function was called independently rather than being called via
        // GuiContainer#addChild(), make the parent aware that this is now its child
        if (parent != null && !parent.hasChild(this))
        {
            parent.addChild(this);
        }
    }

    public boolean hasParent()
    {
        return parent != null;
    }

    public int getNumParents()
    {
        return hasParent() ? parent.getNumParents() + 1 : 0;
    }

    /**
     * Propagate up the tree to retrieve a reference to the GuiScreen containing this element held by the GuiRootContainer
     */
    public AbstractGuiScreen getGuiScreen()
    {
        if (this instanceof GuiRootContainer)
        {
            return ((GuiRootContainer) this).guiScreen;
        }
        else // if (this.hasParent()) (shouldn't be necessary, should have a parent if not a GuiRootContainer)
        {
            return parent.getGuiScreen();
        }
    }

    public GuiElement putColor(GuiColorType type, Color color)
    {
        colorMap.put(type, color);
        return this;
    }

    public Color getColor(GuiColorType type)
    {
        if (colorMap.containsKey(type))
        {
            return colorMap.get(type);
        }
        else if (hasParent())
        {
            Color c = parent.getColor(type);
            // TODO: is this really any more efficient or negligible
            // propagate the color down the tree from whichever container has a key for it,
            // caching it in more specific elements for (faster?) access in future
            putColor(type, c);
            return c;
        }

        return new Color();
    }

    /**
     * Element x position in world space (local x pos + sum of all ancestors world x pos)
     */
    public float worldXPos()
    {
        return localXPos + inheritedXPos;
    }

    /**
     * Element y position in world space (local y pos + sum of all ancestors world y pos)
     */
    public float worldYPos()
    {
        return localYPos + inheritedYPos;
    }

    public float getLocalXPos()
    {
        return localXPos;
    }

    public float getLocalYPos()
    {
        return localYPos;
    }

    public void setLocalXPos(float localXPos)
    {
        this.localXPos = localXPos;
    }

    public void setLocalYPos(float localYPos)
    {
        this.localYPos = localYPos;
    }

    public float getInheritedXPos()
    {
        return inheritedXPos;
    }

    public float getInheritedYPos()
    {
        return inheritedYPos;
    }

    public void setInheritedXPos(float inheritedXPos)
    {
        this.inheritedXPos = inheritedXPos;
    }

    public void setInheritedYPos(float inheritedYPos)
    {
        this.inheritedYPos = inheritedYPos;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public boolean isHovered()
    {
        return hovered;
    }

    public void setHovered(boolean hovered)
    {
        this.hovered = hovered;
    }

    public int getZLevel()
    {
        return zLevel;
    }

    public void setZLevel(int zLevel)
    {
        this.zLevel = zLevel;
    }

    public String getTooltipStr()
    {
        return tooltipStr;
    }

    public void setTooltipStr(String tooltipStr)
    {
        this.tooltipStr = tooltipStr;
    }

    public boolean isFocused()
    {
        return getGuiScreen().getCurrentFocus() == this;
    }

    public Spacing getMargin()
    {
        return margin;
    }

    public void setMargin(Spacing margin)
    {
        this.margin = margin;
    }

    public Spacing getPadding()
    {
        return padding;
    }

    public void setPadding(Spacing padding)
    {
        this.padding = padding;
    }

    public boolean isPointWithinBounds(float x, float y)
    {
        return (x > getLeftBound()) && (x < getRightBound())
                && (y > getTopBound()) && (y < getBottomBound());
    }

    public float getLeftBound()
    {
        return worldXPos();
    }

    public float getRightBound()
    {
        return worldXPos() + getWidth();
    }

    public float getTopBound()
    {
        return worldYPos();
    }

    public float getBottomBound()
    {
        return worldYPos() + getHeight();
    }

    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }

        setHovered(isPointWithinBounds(mouseX, mouseY));

        if (this instanceof InputListener && !((InputListener) this).isEnabled())
        {
            RenderUtils.drawRectangle(worldXPos(), worldYPos(), getWidth(), getHeight(), new Color("#aa181818"));
        }

        if (debug)
        {
            RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, worldXPos(), worldYPos(), getWidth(), getHeight(),
                    isFocused() ? new Color("#00ff00") : new Color("#ff55ff"));

            mc.fontRendererObj.drawString(String.valueOf(getZLevel()), getRightBound() - mc.fontRendererObj.getStringWidth(String.valueOf(getZLevel())),
                    getBottomBound() - mc.fontRendererObj.FONT_HEIGHT, getColor(GuiColorType.ACCENT2).toPackedARGB(), true);
        }
    }

    /**
     * Get an event propagation path from the root of the element tree to this element.
     *
     * @return The path; A list of all this element's ancestors
     */
    public List<GuiElement> getPropagationPath()
    {
        List<GuiElement> path = new ArrayList<>();

        GuiElement element = this;
        while (element != null)
        {
            path.add(0, element); // Insert at beginning to reverse order
            element = element.parent;
        }
        return path;
    }

    /**
     * Perform some action on this element.
     *
     * @see GuiContainer#performAction(Consumer)
     */
    public void performAction(Consumer<GuiElement> action)
    {
        action.accept(this);
    }

    public void propagateEvent(GuiEvent event)
    {
        event.tryDispatchTo(this);
    }

    public void collectMatchingElements(List<GuiElement> collector, Predicate<GuiElement> predicate)
    {
        if (predicate.test(this))
        {
            collector.add(this);
        }
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "elementName='" + elementName + '\'' + ", parent=" + parent + '}';
    }

    /**
     * An abstract GuiElement builder.
     * <p>
     * <a href="https://medium.com/@AbhineyKumar/why-use-the-curiously-recurring-template-pattern-crtp-in-java-a9a192022849">Curiously recurring template pattern</a>
     *
     * @param <T> The type of this builder
     * @param <R> The type of object being built
     */
    protected static abstract class AbstractBuilder<T extends AbstractBuilder<T, R>, R extends GuiElement>
    {
        protected final String elementName;

        protected GuiContainer parent = null;
        protected Map<GuiColorType, Color> colorMap = new HashMap<>();

        protected float localXPos, localYPos;
        protected float width = DEFAULT_WIDTH;
        protected float height = DEFAULT_HEIGHT;

        //        public int zLevel = 0;
        protected boolean visible = true;

        protected Spacing margin = new Spacing(0);
        protected Spacing padding = new Spacing(0);

        protected AbstractBuilder(String elementName)
        {
            this.elementName = elementName;
        }

        /**
         * Set the position of this element builder within its parent; (0,0) would be the top left corner of the parent
         *
         * @param localXPos X position relative to this element's parent
         * @param localYPos Y position relative to this element's parent
         * @return This builder
         */
        public T localPosition(float localXPos, float localYPos)
        {
            this.localXPos = localXPos;
            this.localYPos = localYPos;
            return self();
        }

        public T size(float width, float height)
        {
            this.width = width;
            this.height = height;
            return self();
        }

//        public T zLevel(int zLevel)
//        {
//            this.zLevel = zLevel;
//            return self();
//        }

        public T visible(boolean visible)
        {
            this.visible = visible;
            return self();
        }

        public T margin(Spacing margin)
        {
            this.margin = margin;
            return self();
        }

        public T padding(Spacing padding)
        {
            this.padding = padding;
            return self();
        }

        public T color(GuiColorType type, Color color)
        {
            this.colorMap.put(type, color);
            return self();
        }

        public T parent(GuiContainer parent)
        {
            this.parent = parent;
            return self();
        }

        /**
         * Each concrete subclass builder must implement this to make method chaining properly typed
         *
         * @return This Builder
         */
        protected abstract T self();

        /**
         * Build this object
         *
         * @return The built object
         */
        public abstract R build();
    }
}
