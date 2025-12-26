package io.jonuuh.basis.v000309.lib.gui.element;

import io.jonuuh.basis.v000309.lib.gui.BaseGuiScreen;
import io.jonuuh.basis.v000309.lib.gui.element.container.GuiContainer;
import io.jonuuh.basis.v000309.lib.gui.element.container.GuiRootContainer;
import io.jonuuh.basis.v000309.lib.gui.event.GuiEvent;
import io.jonuuh.basis.v000309.lib.gui.listener.input.InputListener;
import io.jonuuh.basis.v000309.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.v000309.lib.gui.properties.Spacing;
import io.jonuuh.basis.v000309.lib.util.Color;
import io.jonuuh.basis.v000309.lib.util.RenderUtils;
import net.minecraft.client.Minecraft;
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

    private float cornerRadius;

    private String tooltipStr;

    public boolean debug;

    protected GuiElement(AbstractBuilder<?, ?> builder)
    {
        this.elementName = builder.elementName;

        this.localXPos = builder.localXPos;
        this.localYPos = builder.localYPos;

        this.width = builder.width;
        this.height = builder.height;

        this.padding = builder.padding;
        this.margin = builder.margin;

        this.cornerRadius = builder.cornerRadius;

        this.visible = builder.visible;

        this.colorMap = builder.colorMap;

        this.debug = false;

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
     * Propagate up the tree to retrieve a reference to the BaseGuiScreen containing this element held by the GuiRootContainer
     */
    public BaseGuiScreen getGuiScreen()
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

    public void putColor(GuiColorType type, Color color)
    {
        colorMap.put(type, color);
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
        return getLocalXPos() + inheritedXPos;
    }

    /**
     * Element y position in world space (local y pos + sum of all ancestors world y pos)
     */
    public float worldYPos()
    {
        return getLocalYPos() + inheritedYPos;
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

    public float getCornerRadius()
    {
        if (this.cornerRadius != -1)
        {
            return cornerRadius;
        }
        else if (hasParent())
        {
            float cornerRadius = parent.getCornerRadius();
            // Cache the corner radius in more specific elements downward for slightly faster access in future (negligible?)
            setCornerRadius(cornerRadius);
            return cornerRadius;
        }

        return 0;
    }

    public void setCornerRadius(float cornerRadius)
    {
        this.cornerRadius = cornerRadius;
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

//        this.debug = true; // TODO:
//        mc.getTextureManager().bindTexture(mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getLocationSkin());
//        System.out.println(mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getLocationSkin());
//        Gui.drawScaledCustomSizeModalRect(0, 0, 8, 8, 8, 8, 50, 50, 64F, 64F); // base
//        Gui.drawScaledCustomSizeModalRect(0, 0, 40, 8, 8, 8, textureSize, textureSize, 64.0F, 64.0F); // hat

//        ResourceLocation texture = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getLocationSkin();
////        ResourceLocation texture = new ResourceLocation("minecraft:textures/entity/end_portal.png");
//        RenderUtils.drawTexturedRect(texture, 0, 0, getZLevel(), 300, 300, 100, false, new Color());
//
//        GL11.glEnable(GL11.GL_LINE_SMOOTH);
//        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
//        GL11.glLineWidth(2F);
//        RenderUtils.drawRoundedRect(GL11.GL_LINE_STRIP, 0, 0, 300, 300, 100, new Color());
//        GL11.glLineWidth(1F);
//        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
//        GL11.glDisable(GL11.GL_LINE_SMOOTH);

//        System.out.println(texture);

//        // TODO: safest behavior would be to disable an element if ANY of it is out of bounds,
//        //  otherwise unintended wins/losses of greatest z element calc could arise when clicking around the edges of a
//        //  container whose children are partially scrolled out of bounds
//        if (this instanceof InputListener && hasParent())
//        {
//            boolean isBelowTopBound = this.getTopBound() > getParent().getTopBound();
//            boolean isAboveBottomBound = this.getBottomBound() < getParent().getBottomBound();
//
////            System.out.println(this.elementName + " below top:" + isBelowTopBound + " above bottom:" + isAboveBottomBound);
//
//            ((InputListener) this).setEnabled(isBelowTopBound && isAboveBottomBound);
////            System.out.println(elementName + " " + ((InputListener) this).isEnabled());
//        }

        // Temporary; draw dark tint over disabled elements
        if (this instanceof InputListener && !((InputListener) this).isEnabled())
        {
            RenderUtils.drawRectangle(worldXPos(), worldYPos(), getWidth(), getHeight(), new Color("#aa181818"));
        }

        if (debug)
        {
            // Draw element outline
            RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, worldXPos(), worldYPos(), getWidth(), getHeight(),
                    isFocused() ? new Color("#00ff00") : new Color("#ff55ff"));

            // Draw padding rectangles
            Color padColor = new Color("#36ff0000");
            // Left
            RenderUtils.drawRectangle(worldXPos(), worldYPos(), getPadding().left(), getHeight(), padColor);
            // Right
            RenderUtils.drawRectangle(worldXPos() + getWidth() - getPadding().right(), worldYPos(), getPadding().right(), getHeight(), padColor);
            // Top
            RenderUtils.drawRectangle(worldXPos(), worldYPos(), getWidth(), getPadding().top(), padColor);
            // Bottom
            RenderUtils.drawRectangle(worldXPos(), worldYPos() + getHeight() - getPadding().bottom(), getWidth(), getPadding().bottom(), padColor);

            // Draw z level string
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

    public GuiElement getElementByName(String elementName)
    {
        return this.elementName.equals(elementName) ? this : null;
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "name='" + elementName + '\'' + ", parent=" + parent + '}';
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

        protected float cornerRadius = -1;

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

        public T cornerRadius(float cornerRadius)
        {
            this.cornerRadius = cornerRadius;
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
