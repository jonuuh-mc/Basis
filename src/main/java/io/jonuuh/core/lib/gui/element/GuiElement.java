package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.AbstractGuiScreen;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import io.jonuuh.core.lib.gui.element.container.GuiRootContainer;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.gui.properties.GuiEventType;
import io.jonuuh.core.lib.gui.properties.Spacing;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class GuiElement
{
    /** A reference to the static minecraft instance */
    public static final Minecraft mc;

    public static final float DEFAULT_WIDTH = 100;
    public static final float DEFAULT_HEIGHT = 20;

    /** A resource pointing to the default mouse down sound */
    protected static final ResourceLocation resourceClickSound;
    protected static final ResourceLocation resourceGenericBackgroundTex;

    static
    {
        mc = Minecraft.getMinecraft();
        // TODO: implement similar feature to colorMap? propagate up tree until finding a parent that has this defined?
        resourceClickSound = new ResourceLocation("core:click");
        resourceGenericBackgroundTex = new ResourceLocation("core:textures/background_generic.png");
    }

    /** A readable name qualifying this element TODO: no enforcement of this being unique */
    public final String elementName;
    /** This element's immediate ancestor: the container it's inside; Should only ever be null for the root container of the element tree */
    protected GuiContainer parent;

    /** A map of colors that may be used by this element */
    protected Map<GuiColorType, Color> colorMap;

    /**
     * A map of pre-event behavior lambdas.
     * <p>
     * Allows for 'injecting' an arbitrary function call BEFORE this element's default behavior for an event.
     * This function call should return a boolean: whether it should override the element's default behavior for this event.
     * <p>
     * All possible events are normally dispatched by the GuiScreen, EXCEPT FOR 'CUSTOM',
     * which may be dispatched by the element itself, at a time which is defined by that element.
     * <p>
     * e.g. A GuiSingleSlider tries to call any assigned CUSTOM pre event behavior before its value is updated via
     * other events: KEY_INPUT, MOUSE_SCROLL, SCREEN_DRAW
     * (TODO: this design is so weird)
     * <p>
     * Keys are enums representing different events a GuiElement can act on, and values are functions which will
     * be executed (if they exist) before their associated event's default behavior.
     */
    protected Map<GuiEventType, Function<GuiElement, Boolean>> preEventBehaviors;

    /**
     * A map of post-event behavior lambdas.
     * <p>
     * Allows for 'injecting' an arbitrary function call AFTER this element's default behavior for an event.
     * <p>
     * All possible events are normally dispatched by the GuiScreen, EXCEPT FOR 'CUSTOM',
     * which may be dispatched by the element itself, at a time which is defined by that element.
     * <p>
     * e.g. A GuiSingleSlider tries to call any assigned CUSTOM post event behavior after its value is updated via
     * other events: KEY_INPUT, MOUSE_SCROLL, SCREEN_DRAW
     * (TODO: this design is so weird)
     * <p>
     * Keys are enums representing different events a GuiElement can act on, and values are consumers which will
     * be executed (if they exist) after their associated event's default behavior.
     * <p>
     * Should probably be used way more commonly than pre-event behaviors, e.g. for updating the value(s) of
     * {@link io.jonuuh.core.lib.config.setting.types.Setting}s if associated with an element,
     * after that element's state is changed through its default event behavior
     */
    protected Map<GuiEventType, Consumer<GuiElement>> postEventBehaviors;

    /** Element x position within its parent */
    protected float localXPos;
    /** Element y position within its parent */
    protected float localYPos;
    /** The sum of all world xPos from this element's ancestors */
    protected float inheritedXPos;
    /** The sum of all world yPos from this element's ancestors */
    protected float inheritedYPos;

    /** Width of this element */
    protected float width;
    /** Height of this element */
    protected float height;

    /** Which symbolic layer this element is on, should be equal to how many parents this element has */
    protected int zLevel;
    /** Whether this element is visible (should be drawn to screen) */
    protected boolean visible;
    /** Whether this element can be interacted with (usually whether it can be clicked, but could be defined differently in subclasses) */
    protected boolean enabled;
    /** Whether this element is currently hovered; Updated constantly via onScreenDraw (not via onScreenTick because mouse pos is needed) */
    protected boolean hovered;
    /** Whether the mouse is currently pressed down on this element (!= hovered, is set true on mouse down and false on mouse release */
    protected boolean mouseDown;

    protected boolean debug;

    protected Spacing margin;
    protected Spacing padding;

    /** Like the colorMap, should not be accessed directly, use getCornerRadius instead */
    protected float cornerRadius;

    protected String tooltipStr;

    // TODO: use a ticker with onScreenTick instead for tooltips?
    protected int hoverTimeCounter;

    protected GuiElement(String elementName, float localXPos, float localYPos, float width, float height)
    {
        this.elementName = elementName;

        this.localXPos = localXPos;
        this.localYPos = localYPos;

        this.width = width;
        this.height = height;
        this.padding = new Spacing(0);

        this.visible = true;
        this.enabled = true;
        this.cornerRadius = -1F;

        this.debug = true;

        this.preEventBehaviors = new HashMap<>();
        this.postEventBehaviors = new HashMap<>();
        this.colorMap = new HashMap<>();
    }

    protected GuiElement(String elementName, float localXPos, float localYPos)
    {
        this(elementName, localXPos, localYPos, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public GuiContainer getParent()
    {
        return parent;
    }

    /**
     * Use {@link GuiContainer#addChild(GuiElement)} instead?
     * TODO: can't make this protected unless this & container are in same class; factory, bridge, accessor, visitor, something?
     */
    public void setParent(GuiContainer parent)
    {
        this.parent = parent;
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

    public boolean hasParent()
    {
        return parent != null;
    }

    public int getNumParents()
    {
        return hasParent() ? parent.getNumParents() + 1 : 0;
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

    public float getCornerRadius()
    {
        if (cornerRadius != -1F)
        {
            return cornerRadius;
        }
        else if (hasParent())
        {
            return parent.getCornerRadius();
        }
        return 0F;
    }

    public GuiElement setCornerRadius(float cornerRadius)
    {
        this.cornerRadius = cornerRadius;
        return this;
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

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isFocused()
    {
        return getGuiScreen().getCurrentFocus() == this;
    }

    public boolean isMouseDown()
    {
        return mouseDown;
    }

    public void setMouseDown(boolean mouseDown)
    {
        this.mouseDown = mouseDown;
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
        return (x >= worldXPos()) && (x < worldXPos() + getWidth())
                && (y >= worldYPos()) && (y < worldYPos() + getHeight());
    }

    public GuiElement getGreatestZLevelHovered(GuiElement currGreatest)
    {
        if (hovered && enabled && visible)
        {
            if (this.zLevel > currGreatest.zLevel)
            {
                return this;
            }
        }
        return currGreatest;
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

    /**
     * Assign some pre-event behavior to this element
     * <p>
     * The behavior should return a boolean where true means it should override the default behavior, false means it should not
     *
     * @param eventType An enum representing the event this behavior should be associated with
     * @param behavior A pre-event behavior
     * @see GuiElement#preEventBehaviors
     */
    public void assignPreEventBehavior(GuiEventType eventType, Function<GuiElement, Boolean> behavior)
    {
        preEventBehaviors.put(eventType, behavior);
    }

    /**
     * Assign some post-event behavior to this element
     *
     * @param eventType An enum representing the event this behavior should be associated with
     * @param behavior A post-event behavior
     * @see GuiElement#postEventBehaviors
     */
    public void assignPostEventBehavior(GuiEventType eventType, Consumer<GuiElement> behavior)
    {
        postEventBehaviors.put(eventType, behavior);
    }

    /**
     * If it exists, apply the pre-event behavior to this element for some event type
     *
     * @param eventType An enum representing the event this behavior should be associated with
     * @return true if this behavior exists and should override the default behavior for this event, otherwise false
     */
    protected boolean tryApplyPreEventBehavior(GuiEventType eventType)
    {
        Function<GuiElement, Boolean> behavior = preEventBehaviors.get(eventType);
        return behavior != null && behavior.apply(this);
    }

    /**
     * If it exists, apply the post-event behavior to this element for some event type
     *
     * @param eventType An enum representing the event this behavior should be associated with
     */
    protected void tryApplyPostEventBehavior(GuiEventType eventType)
    {
        Consumer<GuiElement> behavior = postEventBehaviors.get(eventType);
        if (behavior != null)
        {
            behavior.accept(this);
        }
    }

    public final void handleInitGuiEvent(ScaledResolution scaledResolution)
    {
        boolean doDefaultBehavior = !tryApplyPreEventBehavior(GuiEventType.INIT);

        if (doDefaultBehavior)
        {
        }

        onInitGui(scaledResolution);

        tryApplyPostEventBehavior(GuiEventType.INIT);
    }

    protected void onInitGui(ScaledResolution scaledResolution)
    {
    }

    public final void handleCloseGuiEvent()
    {
        boolean doDefaultBehavior = !tryApplyPreEventBehavior(GuiEventType.CLOSE);

        if (doDefaultBehavior)
        {
        }

        onCloseGui();

        tryApplyPostEventBehavior(GuiEventType.CLOSE);
    }

    protected void onCloseGui()
    {
    }

    public final void handleScreenDrawEvent(int mouseX, int mouseY, float partialTicks)
    {
        boolean doDefaultBehavior = !tryApplyPreEventBehavior(GuiEventType.SCREEN_DRAW);

        if (doDefaultBehavior)
        {
            if (visible)
            {
                hovered = isPointWithinBounds(mouseX, mouseY);

                onScreenDraw(mouseX, mouseY, partialTicks);

                if (!enabled)
                {
                    RenderUtils.drawRectangle(worldXPos(), worldYPos(), getWidth(), getHeight(), new Color("#aa181818"));
                }

                if (debug)
                {
                    GL11.glEnable(GL11.GL_LINE_SMOOTH);
                    GL11.glLineWidth(0.8F);
                    RenderUtils.drawRoundedRect(GL11.GL_LINE_LOOP, worldXPos(), worldYPos(), getWidth(), getHeight(), getCornerRadius(),
                            isFocused() ? new Color("#00ff00") : new Color("#ff55ff"));
                    GL11.glLineWidth(1F);
                    GL11.glDisable(GL11.GL_LINE_SMOOTH);

                    mc.fontRendererObj.drawString(String.valueOf(zLevel), worldXPos() + getWidth() - mc.fontRendererObj.getStringWidth(String.valueOf(zLevel)),
                            worldYPos() + getHeight() - mc.fontRendererObj.FONT_HEIGHT, getColor(GuiColorType.ACCENT2).toPackedARGB(), true);
                }
            }
            return;
        }

        onScreenDraw(mouseX, mouseY, partialTicks);

        tryApplyPostEventBehavior(GuiEventType.SCREEN_DRAW);
    }

    protected abstract void onScreenDraw(int mouseX, int mouseY, float partialTicks);

    public final void handleScreenTickEvent()
    {
        boolean doDefaultBehavior = !tryApplyPreEventBehavior(GuiEventType.SCREEN_TICK);

        if (doDefaultBehavior)
        {
            // TODO: something like this?
            if (hovered && hoverTimeCounter != 20)
            {
                hoverTimeCounter = 20;
            }
            else if (hoverTimeCounter > 0)
            {
                hoverTimeCounter--;
            }
        }

        onScreenTick();

        tryApplyPostEventBehavior(GuiEventType.SCREEN_TICK);
    }

    protected void onScreenTick()
    {
    }

    public final void handleMouseDownEvent(int mouseX, int mouseY)
    {
        boolean doDefaultBehavior = !tryApplyPreEventBehavior(GuiEventType.MOUSE_DOWN);

        if (doDefaultBehavior)
        {
            mouseDown = true;
            playClickSound(mc.getSoundHandler());
        }

        onMouseDown(mouseX, mouseY);

        tryApplyPostEventBehavior(GuiEventType.MOUSE_DOWN);
    }

    protected void onMouseDown(int mouseX, int mouseY)
    {
    }

    public final void handleMouseUpEvent(int mouseX, int mouseY)
    {
        boolean doDefaultBehavior = !tryApplyPreEventBehavior(GuiEventType.MOUSE_UP);

        if (doDefaultBehavior)
        {
            mouseDown = false;
        }

        onMouseUp(mouseX, mouseY);

        tryApplyPostEventBehavior(GuiEventType.MOUSE_UP);
    }

    protected void onMouseUp(int mouseX, int mouseY)
    {
    }

    public final void handleMouseScrollEvent(int wheelDelta)
    {
        boolean doDefaultBehavior = !tryApplyPreEventBehavior(GuiEventType.MOUSE_SCROLL);

        if (doDefaultBehavior)
        {
        }

        onMouseScroll(wheelDelta);

        tryApplyPostEventBehavior(GuiEventType.MOUSE_SCROLL);
    }

    protected void onMouseScroll(int wheelDelta)
    {
    }

    public final void handleMouseDragEvent(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
        boolean doDefaultBehavior = tryApplyPreEventBehavior(GuiEventType.MOUSE_DRAG);

        if (doDefaultBehavior)
        {
            // note -> mouseDown should only be true for 1 element at a time: the highest z level that was hovered during click event
            if (!mouseDown)
            {
                return;
            }
        }

        onMouseDrag(mouseX, mouseY, clickedMouseButton, msHeld);

        tryApplyPostEventBehavior(GuiEventType.MOUSE_DRAG);
    }

    protected void onMouseDrag(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
    }

    public final void handleKeyInputEvent(char typedChar, int keyCode)
    {
        boolean doDefaultBehavior = tryApplyPreEventBehavior(GuiEventType.KEY_INPUT);

        if (doDefaultBehavior)
        {
        }

        onKeyTyped(typedChar, keyCode);

        tryApplyPostEventBehavior(GuiEventType.KEY_INPUT);
    }

    protected void onKeyTyped(char typedChar, int keyCode)
    {
    }

    protected boolean handlePreCustomEvent()
    {
        return !tryApplyPreEventBehavior(GuiEventType.CUSTOM);
    }

    protected void handlePostCustomEvent()
    {
        tryApplyPostEventBehavior(GuiEventType.CUSTOM);
    }

    protected void playClickSound(SoundHandler soundHandler)
    {
        soundHandler.playSound(PositionedSoundRecord.create(resourceClickSound, 2.0F));
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" + "elementName='" + elementName + '\'' + ", parent=" + parent + '}';
    }
}
