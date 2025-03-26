package io.jonuuh.core.lib.config.gui.elements;

import io.jonuuh.core.lib.config.gui.GuiColorType;
import io.jonuuh.core.lib.config.gui.elements.container.GuiContainer;
import io.jonuuh.core.lib.config.gui.event.CloseGuiEvent;
import io.jonuuh.core.lib.config.gui.event.GuiEvent;
import io.jonuuh.core.lib.config.gui.event.GuiEventBehavior;
import io.jonuuh.core.lib.config.gui.event.GuiEventType;
import io.jonuuh.core.lib.config.gui.event.InitGuiEvent;
import io.jonuuh.core.lib.config.gui.event.KeyInputEvent;
import io.jonuuh.core.lib.config.gui.event.MouseDownEvent;
import io.jonuuh.core.lib.config.gui.event.MouseDragEvent;
import io.jonuuh.core.lib.config.gui.event.MouseScrollEvent;
import io.jonuuh.core.lib.config.gui.event.ScreenDrawEvent;
import io.jonuuh.core.lib.config.gui.event.ScreenTickEvent;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.MathUtils;
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
    public final Minecraft mc;
    public final GuiContainer parent;
    public final String elementName;

    protected final ResourceLocation resourceClickSound;
    protected Map<GuiColorType, Color> colorMap;
    protected Map<GuiEventType, GuiEventBehavior> eventBehaviors;

    protected final int xPosInit;
    protected final int yPosInit;
    protected final int widthInit;
    protected final int heightInit;

    protected final int guiScreenWidthInit;
    protected final int guiScreenHeightInit;
    protected float currWidthScale;
    protected float currHeightScale;

    protected int xPos;
    protected int yPos;
    protected int width;
    protected int height;

    protected int zLevel;

    protected boolean visible;
    protected boolean enabled;
    protected boolean hovered;
    protected boolean focused;
    protected boolean mouseDown;
    protected boolean drawBounds;

    protected String tooltipStr;
//    protected GuiTooltip tooltip;

    // TODO: use a ticker with onScreenTick instead for tooltips?
    public int hoverTimeMs;
    public long startHoverTime;

    protected GuiElement(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, String tooltipStr)
    {
        this.mc = Minecraft.getMinecraft();
        this.parent = parent;
        this.elementName = elementName;

        // assumes that parent xPos and xPosInit are the same? probably fine
        this.xPosInit = this.xPos = xPos + (hasParent() ? parent.xPosInit : 0);
        this.yPosInit = this.yPos = yPos + (hasParent() ? parent.yPosInit : 0);

        this.widthInit = this.width = width;
        this.heightInit = this.height = height;

        // TODO: is this not stupid to be creating one of these for each element? assign just one of these to a
        //  parent container somehow and then access it from children? would that even be more efficient?
        //  this is dependent on the launch resolution which can probably be changed from launcher
        //  currently gui element sizes are based upon arbitrary-ish default launch res
        ScaledResolution sr = new ScaledResolution(mc);
        this.guiScreenWidthInit = sr.getScaledWidth();
        this.guiScreenHeightInit = sr.getScaledHeight();

        this.visible = true;
        this.enabled = true;
        this.zLevel = this.getNumParents();

        this.drawBounds = true; // TODO: debug
        this.tooltipStr = tooltipStr;

        this.resourceClickSound = new ResourceLocation("core-config:click");
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

    public int getXPos()
    {
        return xPos;
    }

    public void setXPos(int xPos)
    {
        this.xPos = xPos;
    }

    public int getYPos()
    {
        return yPos;
    }

    public void setYPos(int yPos)
    {
        this.yPos = yPos;
    }

//    public void setTempXPos(int xPos)
//    {
//        this.xPos = xPosInit + xPos;
//    }
//
//    public void setTempYPos(int yPos)
//    {
//        this.yPos = yPosInit + yPos;
//    }

    public int getWidth()
    {
        return width;
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

    public int getHoverTimeMs()
    {
        return hoverTimeMs;
    }

    public void setHoverTimeMs(int hoverTimeMs)
    {
        this.hoverTimeMs = hoverTimeMs;
    }

    public long getStartHoverTime()
    {
        return startHoverTime;
    }

    public void setStartHoverTime(long startHoverTime)
    {
        this.startHoverTime = startHoverTime;
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
            // TODO: make this eased or clamped? linear scaling makes for too small/large elements at smallest/biggest screen size
            currWidthScale = (float) guiScreenWidth / guiScreenWidthInit;
            currHeightScale = (float) guiScreenHeight / guiScreenHeightInit;

            System.out.println(currWidthScale + " " + currHeightScale);

            width = (int) (widthInit * currWidthScale);
            height = (int) (heightInit * currHeightScale);

            xPos = (int) (xPosInit * currWidthScale);
            yPos = (int) (yPosInit * currHeightScale);
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
                hovered = (mouseX >= xPos) && (mouseX < xPos + width)
                        && (mouseY >= yPos) && (mouseY < yPos + height);

                onScreenDraw(mouseX, mouseY, partialTicks);

                if (drawBounds)
                {
                    RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, xPos, yPos, width, height, focused ? new Color("#00ff00") : new Color("#ff55ff"));
                    drawScaledString(String.valueOf(zLevel), xPos + width - 6, yPos + height - 6, getColor(GuiColorType.ACCENT2).toDecimalARGB(), true);
                }
            }
            return;
        }

        onScreenDraw(mouseX, mouseY, partialTicks);
    }

    protected abstract void onScreenDraw(int mouseX, int mouseY, float partialTicks);

    public final void dispatchScreenTickEvent()
    {
        /*boolean overrideDefaultBehavior = */
        tryApplyCustomEventBehavior(GuiEventType.SCREEN_TICK);
//
//        if (!overrideDefaultBehavior)
//        {
//            // default behaviors; none for now, should add tooltip handling here later?
//        }

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

    protected void drawScaledString(String text, float x, float y, int color, boolean dropShadow)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(MathUtils.clamp(currWidthScale - 0.5, 0.2, 1.0), MathUtils.clamp(currHeightScale - 0.5, 0.2, 1.0), 0);

        mc.fontRendererObj.drawString(text, 0, 0, color, dropShadow);
        GL11.glPopMatrix();
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
