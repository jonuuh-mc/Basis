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
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public abstract class GuiElement
{
    /** A reference to the static minecraft object */
    public final Minecraft mc;
    /** This element's immediate ancestor: the container it's inside; Should only ever be null for the root container of the element tree */
    public final GuiContainer parent;
    /** A readable name qualifying this element TODO: no enforcement of this being unique */
    public final String elementName;

    /** A resource representing the default mouse down sound */
    protected final ResourceLocation resourceClickSound;
    /** A map of colors that may be used by this element */
    protected Map<GuiColorType, Color> colorMap;
    /**
     * A map of custom event behavior lambdas which usually will have no entries;
     * Allows for 'injecting' into and potentially overriding events dispatched by the GuiScreen when they are handled in this class
     */
    protected Map<GuiEventType, GuiEventBehavior> eventBehaviors;

    protected final int initialXPos;
    protected final int initialYPos;
    /** Initial constructed width of this element, used for element resizing */
    protected final int initialWidth;
    /** Initial constructed height of this element, used for element resizing */
    protected final int initialHeight;

    /** Local xPos, should almost never be used alone */
    private int xPos;
    /** Local yPos, should almost never be used alone */
    private int yPos;
    /** Logical width of this element */
    protected int width;
    /** Logical height of this element */
    protected int height;

    /** The sum of all xPos from this element's ancestors */
    protected int inheritedXPos;
    /** The sum of all yPos from this element's ancestors */
    protected int inheritedYPos;

    /** Which symbolic layer this element is on: equal to how many parents this element has */
    protected int zLevel;

    protected boolean visible;
    protected boolean enabled;
    protected boolean hovered;
    protected boolean focused;
    protected boolean mouseDown;
    protected boolean drawBounds;

    protected Margins margins;

    protected String tooltipStr;

    // TODO: use a ticker with onScreenTick instead for tooltips?
    protected int hoverTimeCounter;

    protected GuiElement(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, String tooltipStr)
    {
        this.mc = Minecraft.getMinecraft();
        this.parent = parent;
        this.elementName = elementName;

        this.initialXPos = this.xPos = xPos;
        this.initialYPos = this.yPos = yPos;

        this.initialWidth = this.width = width;
        this.initialHeight = this.height = height;

        this.inheritedXPos = hasParent() ? parent.worldXPos() : 0;
        this.inheritedYPos = hasParent() ? parent.worldYPos() : 0;

        this.visible = true;
        this.enabled = true;
        this.zLevel = this.getNumParents();

        this.drawBounds = true; // TODO: debug
        this.tooltipStr = tooltipStr;

        this.resourceClickSound = new ResourceLocation("core:click");
        this.eventBehaviors = new HashMap<>();
        this.colorMap = new HashMap<>();

        // TODO: why is this even legal honestly - putting a partially constructed object into a map?
        if (hasParent() /*&& !elementName.contains("$")*/)
        {
            parent.addChild(this);
        }
    }

    protected GuiElement(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height)
    {
        this(parent, elementName, xPos, yPos, width, height, "");
    }

    protected GuiElement(GuiContainer parent, String elementName, int xPos, int yPos)
    {
        this(parent, elementName, xPos, yPos, 200, 20);
    }

    // TODO: add `return this` for all setters?

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
     * Element x position in world space (accounting for all x movement from ancestors)
     */
    public int worldXPos()
    {
        return xPos + inheritedXPos;
    }

    /**
     * Element y position in world space (accounting for all y movement from ancestors)
     */
    public int worldYPos()
    {
        return yPos + inheritedYPos;
    }

    public int localXPos()
    {
        return xPos;
    }

    public int localYPos()
    {
        return yPos;
    }

    public void setLocalXPos(int xPos)
    {
        this.xPos = xPos;
    }

    public void setLocalYPos(int yPos)
    {
        this.yPos = yPos;
    }

    public int getInitialXPos()
    {
        return initialXPos;
    }

    public int getInitialYPos()
    {
        return initialYPos;
    }

    public int getInheritedXPos()
    {
        return inheritedXPos;
    }

    public void setInheritedXPos(int inheritedXPos)
    {
        this.inheritedXPos = inheritedXPos;
    }

    public int getInheritedYPos()
    {
        return inheritedYPos;
    }

    public void setInheritedYPos(int inheritedYPos)
    {
        this.inheritedYPos = inheritedYPos;
    }

    public int getWidth()
    {
        return width;
    }

    public int getInitialWidth()
    {
        return initialWidth;
    }

    public boolean isInitialWidth()
    {
        return width == initialWidth;
    }
    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }



    public int getInitialHeight()
    {
        return initialHeight;
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

    public Margins getMargins()
    {
        return margins;
    }

    public void setMargins(Margins margins)
    {
        this.margins = margins;
    }

    /**
     * Assign a custom behavior to this element for some gui event
     * <p>The behavior should return a boolean: whether to override the default behavior
     *
     * @param eventType the event type
     * @param behavior  the custom behavior
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
            dispatchInitGuiEvent(((InitGuiEvent) event).guiScreenWidth, ((InitGuiEvent) event).guiScreenHeight);
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

    public final void dispatchInitGuiEvent(int guiScreenWidth, int guiScreenHeight)
    {
        boolean overrideDefaultBehavior = tryApplyCustomEventBehavior(GuiEventType.INIT);

        if (!overrideDefaultBehavior)
        {
            // TODO: need to account for str width/height in drawing strings which will be damn near impossible given scaling of text with gl11
            //  drawing text is the sole reason i need to find a more mc idiomatic way to scale gui on resize

            // TODO: make this eased or clamped? linear scaling makes for too small/large elements at smallest/biggest screen size
//            currWidthScale = (float) guiScreenWidth / guiScreenWidthInit;
//            currHeightScale = (float) guiScreenHeight / guiScreenHeightInit;

//            System.out.println(currWidthScale + " " + currHeightScale);
//
//            width = (int) (widthInit * currWidthScale);
//            height = (int) (heightInit * currHeightScale);
//
//            xPos = (int) (xPosInit * currWidthScale);
//            yPos = (int) (yPosInit * currHeightScale);
        }

        onInitGui(guiScreenWidth, guiScreenHeight);
    }

    protected void onInitGui(int guiScreenWidth, int guiScreenHeight)
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
                hovered = (mouseX >= worldXPos()) && (mouseX < worldXPos() + width)
                        && (mouseY >= worldYPos()) && (mouseY < worldYPos() + height);

                onScreenDraw(mouseX, mouseY, partialTicks);

                if (drawBounds)
                {
                    RenderUtils.drawRoundedRect(GL11.GL_LINE_LOOP, worldXPos(), worldYPos(), width, height, hasParent() ? parent.getOuterRadius() : 3,
                            focused ? new Color("#00ff00") : new Color("#ff55ff"), true);
//                    RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, worldXPos(), worldYPos(), width, height, focused ? new Color("#00ff00") : new Color("#ff55ff"));
                    mc.fontRendererObj.drawString(String.valueOf(zLevel), worldXPos() + width - mc.fontRendererObj.getStringWidth(String.valueOf(zLevel)),
                            worldYPos() + height - mc.fontRendererObj.FONT_HEIGHT, getColor(GuiColorType.ACCENT2).toPackedARGB(), true);
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
            if (hovered)
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
