package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.AbstractGuiScreen;
import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import io.jonuuh.core.lib.gui.element.container.GuiWindow;
import io.jonuuh.core.lib.gui.event.GuiEventBehavior;
import io.jonuuh.core.lib.gui.event.GuiEventType;
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

public abstract class GuiElement
{
    /** A reference to the static minecraft object */
    public static final Minecraft mc;
    /** A resource pointing to the default mouse down sound */
    public static final ResourceLocation resourceClickSound;

    /** A readable name qualifying this element TODO: no enforcement of this being unique */
    public final String elementName;
    /** This element's immediate ancestor: the container it's inside; Should only ever be null for the root container of the element tree */
    protected GuiContainer parent;

    /** A map of colors that may be used by this element */
    protected Map<GuiColorType, Color> colorMap;
    /**
     * A map of custom event behavior lambdas which usually will have no entries;
     * Allows for 'injecting' into and potentially overriding events dispatched by the GuiScreen when they are handled by a GuiElement
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
    protected boolean mouseDown;
    protected boolean drawBounds;

    protected Margin margin;
    protected Padding padding;

    protected String tooltipStr;

    // TODO: use a ticker with onScreenTick instead for tooltips?
    protected int hoverTimeCounter;

    static
    {
        mc = Minecraft.getMinecraft();
        // TODO: implement similar feature to colorMap? propagate up tree until finding a parent that has this defined?
        resourceClickSound = new ResourceLocation("core:click");
    }

    protected GuiElement(String elementName, float localXPos, float localYPos, float width, float height)
    {
        this.elementName = elementName;

        this.localXPos = localXPos;
        this.localYPos = localYPos;
        this.dimensions = new Dimensions(width, height);

        this.visible = true;
        this.enabled = true;

        this.drawBounds = true; // TODO: debug

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

    public AbstractGuiScreen getGuiScreen()
    {
        if (this instanceof GuiWindow)
        {
            return ((GuiWindow) this).guiScreen;
        }
        else // if (this.hasParent()) (shouldn't be necessary, should have a parent if not a GuiWindow)
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
        return getGuiScreen().getCurrentFocus() == this;
    }

//    public void setFocused(boolean focused)
//    {
//        this.focused = focused;
//    }

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
     * Perform some action on this element.
     *
     * @see GuiContainer#performAction(Consumer)
     */
    public void performAction(Consumer<GuiElement> action)
    {
        action.accept(this);
    }

    /**
     * Assign a custom behavior to this element for some gui event
     * <p>
     * The behavior should return a boolean: whether to override the default behavior
     *
     * @param eventType the event type
     * @param behavior the custom behavior
     * @see GuiEventBehavior
     */
    public void assignCustomEventBehavior(GuiEventType eventType, GuiEventBehavior behavior)
    {
        eventBehaviors.put(eventType, behavior);
    }

    /**
     * If it exists, apply the custom event behavior to this element for some event type
     *
     * @param eventType the event type
     * @return true if this behavior exists and should override the default behavior for this event, otherwise false
     */
    protected boolean tryApplyCustomEventBehavior(GuiEventType eventType)
    {
        GuiEventBehavior behavior = eventBehaviors.get(eventType);
        return behavior != null && behavior.apply(this);
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

    public final void dispatchInitGuiEvent(ScaledResolution scaledResolution)
    {
        boolean overrideDefaultBehavior = tryApplyCustomEventBehavior(GuiEventType.INIT);

        if (!overrideDefaultBehavior)
        {
            // TODO: scuffed fix for now with added elements not having proper order? maybe this is just fine
            //  better to be dynamic now than have something break later to inflexibility?
            //  what if we want to add elements while gui is open? would need to do this in onscreentick or something
            setZLevel(getNumParents());
            // TODO: just do this via container.addChild() similarly to updateInheritedXPos() and etc
        }

        onInitGui(scaledResolution);
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
                    RenderUtils.drawRoundedRect(GL11.GL_LINE_LOOP, worldXPos(), worldYPos(), getWidth(), getHeight(), hasParent() ? parent.getOuterRadius() : 3,
                            isFocused() ? new Color("#00ff00") : new Color("#ff55ff"), true);

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
