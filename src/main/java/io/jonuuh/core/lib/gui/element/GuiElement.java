package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import io.jonuuh.core.lib.gui.event.CloseGuiEvent;
import io.jonuuh.core.lib.gui.event.GuiEvent;
import io.jonuuh.core.lib.gui.event.GuiEventBehavior;
import io.jonuuh.core.lib.gui.event.GuiEventType;
import io.jonuuh.core.lib.gui.event.InitGuiEvent;
import io.jonuuh.core.lib.gui.event.KeyInputEvent;
import io.jonuuh.core.lib.gui.event.MouseDownEvent;
import io.jonuuh.core.lib.gui.event.MouseDragEvent;
import io.jonuuh.core.lib.gui.event.MouseScrollEvent;
import io.jonuuh.core.lib.gui.event.ScreenDrawEvent;
import io.jonuuh.core.lib.gui.event.ScreenTickEvent;
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

public abstract class GuiElement
{
    /** A reference to the static minecraft object */
    public final Minecraft mc;
    /** A readable name qualifying this element TODO: no enforcement of this being unique */
    public final String elementName;
    /** This element's immediate ancestor: the container it's inside; Should only ever be null for the root container of the element tree */
    protected GuiContainer parent;

    // TODO: implement similar feature to colorMap? propagate up tree until finding a parent that has this defined
    /** A resource representing the default mouse down sound */
    protected final ResourceLocation resourceClickSound;
    /** A map of colors that may be used by this element */
    protected Map<GuiColorType, Color> colorMap;
    /**
     * A map of custom event behavior lambdas which usually will have no entries;
     * Allows for 'injecting' into and potentially overriding events dispatched by the GuiScreen when they are handled in this class
     */
    protected Map<GuiEventType, GuiEventBehavior> eventBehaviors;

    /** Element x position within its {@link GuiElement#parent} */
    protected float localXPos;
    /** Element y position within its {@link GuiElement#parent} */
    protected float localYPos;
    /** The sum of all world xPos from this element's ancestors */
    protected float inheritedXPos;
    /** The sum of all world yPos from this element's ancestors */
    protected float inheritedYPos;

    protected Dimensions dimensions;

    /** Which symbolic layer this element is on: equal to how many parents this element has */
    protected int zLevel;

    protected boolean visible;
    protected boolean enabled;
    protected boolean hovered;
    protected boolean focused;
    protected boolean mouseDown;
    protected boolean drawBounds;

    protected Margin margin;
    protected Padding padding;

    protected String tooltipStr;

    // TODO: use a ticker with onScreenTick instead for tooltips?
    protected int hoverTimeCounter;

    protected GuiElement(String elementName, float localXPos, float localYPos, float width, float height)
    {
        this.mc = Minecraft.getMinecraft();
        this.elementName = elementName;

        this.localXPos = localXPos;
        this.localYPos = localYPos;
        this.dimensions = new Dimensions(width, height);

        this.visible = true;
        this.enabled = true;

        this.drawBounds = true; // TODO: debug

        this.resourceClickSound = new ResourceLocation("core:click");
        this.eventBehaviors = new HashMap<>();
        this.colorMap = new HashMap<>();
    }

    protected GuiElement(String elementName, float localXPos, float localYPos, Dimensions dimensions)
    {
        this(elementName, localXPos, localYPos, dimensions.width, dimensions.height);
    }

    protected GuiElement(String elementName, float localXPos, float localYPos)
    {
        this(elementName, localXPos, localYPos, new Dimensions());
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

//        if (!parent.hasChild(this))
//        {
//            parent.addChild(this);
//        }
//        else
//        {
//            this.setInheritedXPos(parent.worldXPos());
//            this.setInheritedYPos(parent.worldYPos());
//        }
    }

    // TODO: add `return this` for setters?

    public boolean hasParent()
    {
        return parent != null;
    }

    public int getNumParents()
    {
        return hasParent() ? parent.getNumParents() + 1 : 0;
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
     * Element x position in world space (local pos + sum of all ancestors world pos)
     */
    public float worldXPos()
    {
        return localXPos + inheritedXPos;
    }

    /**
     * Element y position in world space (local pos + sum of all ancestors world pos)
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

    public void setInheritedXPos(float inheritedXPos)
    {
        this.inheritedXPos = inheritedXPos;

        if (this instanceof GuiContainer)
        {
            for (GuiElement child : ((GuiContainer) this).getChildren())
            {
                child.setInheritedXPos(this.worldXPos());
            }
        }
    }

    public float getInheritedYPos()
    {
        return inheritedYPos;
    }

    public void setInheritedYPos(float inheritedYPos)
    {
        this.inheritedYPos = inheritedYPos;

        if (this instanceof GuiContainer)
        {
            for (GuiElement child : ((GuiContainer) this).getChildren())
            {
                child.setInheritedYPos(this.worldYPos());
            }
        }
    }

    public Dimensions getDimensions()
    {
        return dimensions;
    }

    public float getMinWidth()
    {
        return dimensions.minWidth;
    }

    public float getWidth()
    {
        return dimensions.width;
    }

    public float getMaxWidth()
    {
        return dimensions.maxWidth;
    }

    public void setWidth(float width)
    {
        dimensions.width = width;
    }

    public float getMinHeight()
    {
        return dimensions.minHeight;
    }

    public float getHeight()
    {
        return dimensions.height;
    }

    public float getMaxHeight()
    {
        return dimensions.maxHeight;
    }

    public void setHeight(float height)
    {
        dimensions.height = height;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;

        if (this instanceof GuiContainer)
        {
            for (GuiElement child : ((GuiContainer) this).getChildren())
            {
                child.setVisible(visible);
            }
        }
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

    public boolean shouldDrawBounds()
    {
        return drawBounds;
    }

    public void setDrawBounds(boolean drawBounds)
    {
        this.drawBounds = drawBounds;
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
        return focused;
    }

    public void setFocused(boolean focused)
    {
        this.focused = focused;
    }

    public boolean isMouseDown()
    {
        return mouseDown;
    }

    public void setMouseDown(boolean mouseDown)
    {
        this.mouseDown = mouseDown;
    }

    public Margin getMargin()
    {
        return margin;
    }

    public void setMargin(Margin margin)
    {
        this.margin = margin;
    }

    public Padding getPadding()
    {
        return padding;
    }

    public void setPadding(Padding padding)
    {
        this.padding = padding;
    }

    /**
     * Assign a custom behavior to this element for some gui event
     * <p>The behavior should return a boolean: whether to override the default behavior
     *
     * @param eventType the event type
     * @param behavior the custom behavior
     * @see GuiEventBehavior
     */
    public final void assignCustomEventBehavior(GuiEventType eventType, GuiEventBehavior behavior)
    {
        eventBehaviors.put(eventType, behavior);
    }

    /**
     * If it exists, apply the custom event behavior to this element for some event type
     *
     * @param eventType the event type
     * @return true if this behavior exists and should override the default behavior for this event, otherwise false
     */
    protected final boolean tryApplyCustomEventBehavior(GuiEventType eventType)
    {
        GuiEventBehavior behavior = eventBehaviors.get(eventType);
        return behavior != null && behavior.apply(this);
    }

    // depth first searching i think?
    public final void propagateEvent(GuiEvent event)
    {
        // handle events as we propagate down each tree branch.
        // handling order per element matters for drawing element order for example,
        // an element in a container should be drawn after (on top) of the container
        handleEvent(event);

        if (this instanceof GuiContainer)
        {
            for (GuiElement child : ((GuiContainer) this).getChildren())
            {
                child.propagateEvent(event);
            }
        }
    }

    private void handleEvent(GuiEvent event)
    {
        if (event instanceof InitGuiEvent)
        {
            dispatchInitGuiEvent(((InitGuiEvent) event).scaledResolution);
        }
        else if (event instanceof CloseGuiEvent)
        {
            dispatchCloseGuiEvent();
        }
        else if (event instanceof ScreenDrawEvent)
        {
            ScreenDrawEvent e = (ScreenDrawEvent) event;
            dispatchScreenDrawEvent(e.mouseX, e.mouseY, e.partialTicks);
        }
        else if (event instanceof ScreenTickEvent)
        {
            dispatchScreenTickEvent();
        }
        else if (event instanceof KeyInputEvent)
        {
            dispatchKeyInputEvent(((KeyInputEvent) event).typedChar, ((KeyInputEvent) event).keyCode);
        }
        else if (event instanceof MouseDownEvent)
        {
            if (hovered && enabled && visible)
            {
                ((MouseDownEvent) event).collectMouseDownElement(this);
            }
            // reset focus of whichever element (if any) currently has focus
            if (focused)
            {
                focused = false;
            }
        }
//        else if (event instanceof MouseUpEvent)
//        {
//        }
        else if (event instanceof MouseDragEvent)
        {
            MouseDragEvent e = (MouseDragEvent) event;
            dispatchMouseDragEvent(e.mouseX, e.mouseY, e.clickedMouseButton, e.msHeld);
        }
        else if (event instanceof MouseScrollEvent)
        {
            dispatchMouseScrollEvent(((MouseScrollEvent) event).wheelDelta);
        }
    }

    public final void dispatchInitGuiEvent(ScaledResolution scaledResolution)
    {
        boolean overrideDefaultBehavior = tryApplyCustomEventBehavior(GuiEventType.INIT);

        if (!overrideDefaultBehavior)
        {
            // TODO: scuffed fix for now with added elements not having proper order? maybe this is just fine
            //  better to be dynamic now than have something break later to inflexibility?
            //  what if we want to add elements while gui is open? would need to do this in onscreentick or something
            setZLevel(getNumParents());
        }

        onInitGui(scaledResolution);


//        if (this.padding != null && this instanceof GuiContainer)
//        {
//
//            for (GuiElement child : ((GuiContainer) this).getChildren())
//            {
//                child.setInheritedYPos(this.worldYPos());
//            }
//
//        }

//        this.applyPadding();
    }

//    protected void applyPadding()
//    {
//        if (hasParent() && parent.padding != null)
//        {
//            Padding padding = parent.padding;
//
//            if (localXPos < padding.getLeftPadding())
//            {
//                setLocalXPos(padding.getLeftPadding());
//            }
//
//            if (localXPos + width > parent.width - padding.getRightPadding())
//            {
//                // how far the element is across the right padding line
//                float withinPaddingAmt = (localXPos + width) - (parent.width - padding.getRightPadding());
//                setLocalXPos(localXPos - withinPaddingAmt);
//            }
//
//            if (localYPos < padding.getTopPadding())
//            {
//                setLocalYPos(padding.getTopPadding());
//            }
//
//            if (localYPos + height > parent.height - padding.getBottomPadding())
//            {
//                // how far the element is across the bottom padding line
//                float withinPaddingAmt = (localYPos + height) - (parent.height - padding.getBottomPadding());
//                setLocalYPos(localXPos - withinPaddingAmt);
//            }
//        }
//    }

    protected void onInitGui(ScaledResolution scaledResolution)
    {
    }

    public final void dispatchCloseGuiEvent()
    {
        boolean overrideDefaultBehavior = tryApplyCustomEventBehavior(GuiEventType.CLOSE);

        if (!overrideDefaultBehavior)
        {
            // reset focus of whichever element (if any) currently has focus
            if (focused)
            {
                focused = false;
            }
        }

        onCloseGui();
    }

    protected void onCloseGui()
    {
    }

    public final void dispatchScreenDrawEvent(int mouseX, int mouseY, float partialTicks)
    {
        boolean overrideDefaultBehavior = tryApplyCustomEventBehavior(GuiEventType.SCREEN_DRAW);

        if (!overrideDefaultBehavior)
        {
            if (visible)
            {
                hovered = (mouseX >= worldXPos()) && (mouseX < worldXPos() + getWidth())
                        && (mouseY >= worldYPos()) && (mouseY < worldYPos() + getHeight());

                onScreenDraw(mouseX, mouseY, partialTicks);

                if (drawBounds)
                {
                    // TODO: width test
                    RenderUtils.drawRoundedRect(GL11.GL_LINE_LOOP, worldXPos(), worldYPos(), getWidth(), getHeight(), hasParent() ? parent.getOuterRadius() : 3,
                            focused ? new Color("#00ff00") : new Color("#ff55ff"), true);
//                    RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, worldXPos(), worldYPos(), width, height, focused ? new Color("#00ff00") : new Color("#ff55ff"));
                    mc.fontRendererObj.drawString(String.valueOf(zLevel), worldXPos() + getWidth() - mc.fontRendererObj.getStringWidth(String.valueOf(zLevel)),
                            worldYPos() + getHeight() - mc.fontRendererObj.FONT_HEIGHT, getColor(GuiColorType.ACCENT2).toPackedARGB(), true);
                }
            }
            return;
        }

        onScreenDraw(mouseX, mouseY, partialTicks);
    }

    protected abstract void onScreenDraw(int mouseX, int mouseY, float partialTicks);

    public final void dispatchScreenTickEvent()
    {
        boolean overrideDefaultBehavior = tryApplyCustomEventBehavior(GuiEventType.SCREEN_TICK);

        if (!overrideDefaultBehavior)
        {
            // default behaviors; none for now, should add tooltip handling here later?

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

        //  shouldn't be tied to fps, unlike screen drawing
        onScreenTick();
    }

    protected void onScreenTick()
    {
    }

    public final void dispatchMouseDownEvent(int mouseX, int mouseY)
    {
        boolean overrideDefaultBehavior = tryApplyCustomEventBehavior(GuiEventType.MOUSE_DOWN);

        if (!overrideDefaultBehavior)
        {
            mouseDown = true;
            focused = true;
            playClickSound(mc.getSoundHandler());
        }

        onMouseDown(mouseX, mouseY);
    }

    protected void onMouseDown(int mouseX, int mouseY)
    {
    }

    public final void dispatchMouseUpEvent(int mouseX, int mouseY)
    {
        boolean overrideDefaultBehavior = tryApplyCustomEventBehavior(GuiEventType.MOUSE_UP);

        if (!overrideDefaultBehavior)
        {
            mouseDown = false;
        }

        onMouseUp(mouseX, mouseY);
    }

    protected void onMouseUp(int mouseX, int mouseY)
    {
    }

    public final void dispatchMouseScrollEvent(int wheelDelta)
    {
        boolean overrideDefaultBehavior = tryApplyCustomEventBehavior(GuiEventType.MOUSE_SCROLL);

        if (!overrideDefaultBehavior)
        {
            if (!focused)
            {
                return;
            }
        }

        onMouseScroll(wheelDelta);
    }

    protected void onMouseScroll(int wheelDelta)
    {
    }

    public final void dispatchMouseDragEvent(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
        boolean overrideDefaultBehavior = tryApplyCustomEventBehavior(GuiEventType.MOUSE_DRAG);

        if (!overrideDefaultBehavior)
        {
            // note -> mouseDown should only be true for 1 element at a time: the highest z level that was hovered during click event
            if (!mouseDown)
            {
                return;
            }
        }

        onMouseDrag(mouseX, mouseY, clickedMouseButton, msHeld);
    }

    protected void onMouseDrag(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
    }

    public final void dispatchKeyInputEvent(char typedChar, int keyCode)
    {
        boolean overrideDefaultBehavior = tryApplyCustomEventBehavior(GuiEventType.KEY_INPUT);

        if (!overrideDefaultBehavior)
        {
            if (!focused)
            {
                return;
            }
        }

        onKeyTyped(typedChar, keyCode);
    }

    protected void onKeyTyped(char typedChar, int keyCode)
    {
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
