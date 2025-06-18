package io.jonuuh.core.lib.gui;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.element.container.GuiRootContainer;
import io.jonuuh.core.lib.gui.event.GuiTargetedEvent;
import io.jonuuh.core.lib.gui.event.input.KeyInputEvent;
import io.jonuuh.core.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.core.lib.gui.event.input.MouseDragEvent;
import io.jonuuh.core.lib.gui.event.input.MouseScrollEvent;
import io.jonuuh.core.lib.gui.event.input.MouseUpEvent;
import io.jonuuh.core.lib.gui.event.lifecycle.CloseGuiEvent;
import io.jonuuh.core.lib.gui.event.lifecycle.InitGuiEvent;
import io.jonuuh.core.lib.gui.event.lifecycle.ScreenTickEvent;
import io.jonuuh.core.lib.gui.listener.input.InputListener;
import io.jonuuh.core.lib.gui.listener.input.MouseClickListener;
import io.jonuuh.core.lib.gui.listener.input.MouseScrollListener;
import io.jonuuh.core.lib.util.CollectionUtils;
import io.jonuuh.core.lib.util.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractGuiScreen extends GuiScreen
{
    protected GuiRootContainer rootContainer;
    protected GuiElement currentFocus;
    // TODO: implement something like this later? could be shared between all guiscreens of a mod
//    protected FontRenderer customFontRenderer = new FontRenderer(mc.gameSettings, new ResourceLocation("core:ascii.png"), mc.renderEngine, false);

    public AbstractGuiScreen()
    {
    }

    protected abstract GuiRootContainer initRootContainer();

    /**
     * Get the element that currently has focus, if any
     */
    public GuiElement getCurrentFocus()
    {
        return currentFocus;
    }

    /**
     * Whether any element on this screen is focused (not whether this GuiScreen itself is focused)
     * <p>
     * This should only be false if nothing has been clicked since the Gui was last closed
     */
    public boolean hasCurrentFocus()
    {
        return currentFocus != null;
    }

    /**
     * Whether this screen should pause the game in singleplayer
     */
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Called when this GuiScreen is first displayed or when the window resizes
     *
     * @see GuiScreen#setWorldAndResolution(Minecraft, int, int)
     */
    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        rootContainer.performAction(element -> element.handleInitGuiEvent(new ScaledResolution(mc)));
    }

    /**
     * Called when any other GuiScreen (including a 'null' GuiScreen which is how a GuiScreen is closed normally)
     * is opened while this one was already opened
     *
     * @see Minecraft#displayGuiScreen(GuiScreen)
     */
    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        currentFocus = null;
        rootContainer.performAction(GuiElement::handleCloseGuiEvent);
    }

    /**
     * Should be used similarly to {@link AbstractGuiScreen#drawScreen(int, int, float)}, but when mouse position
     * and/or very frequent updates aren't required. Called 20 client ticks per second?
     */
    @Override
    public void updateScreen()
    {
        rootContainer.performAction(GuiElement::handleScreenTickEvent);
    }

    /**
     * Mainly used to draw the elements on the screen but also as a very frequent event ticker (hence the mouse position params).
     * Called [fps] times per second?
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        rootContainer.performAction(element -> element.handleScreenDrawEvent(mouseX, mouseY, partialTicks));
    }

    /**
     * Fired after parsing lwjgl {@link Keyboard} event states via {@link GuiScreen#handleKeyboardInput()}
     * when a key is typed (apart from f11 and esc?)
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);

        if (hasCurrentFocus())
        {
            dispatchTargetedEvent(new KeyInputEvent(currentFocus, typedChar, keyCode));
        }
    }

    /**
     * Used to parse lwjgl {@link Mouse} event states.
     * By default, calls mouseClicked, mouseReleased, and mouseClickMove via GuiScreen superclass (super.handleMouseInput()),
     * but can be extended as is done here for more features like mouse wheel states
     */
    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();

        int wheelDelta = (int) MathUtils.clamp(Mouse.getEventDWheel(), -1, 1);

        if (wheelDelta != 0)
        {
            List<GuiElement> scrollable = new ArrayList<>();
            rootContainer.collectMatchingElements(scrollable, element -> (element.isVisible() && element.isHovered() && canScrollOn(element)));

            GuiElement scrollTarget = CollectionUtils.getMax(scrollable, Comparator.comparingInt(GuiElement::getZLevel));

            dispatchTargetedEvent(new MouseScrollEvent(scrollTarget, wheelDelta));
        }
    }

    /**
     * Fired when a mouse button is pressed down.
     * Downstream of {@link GuiScreen#handleMouseInput()}
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseEventButton)
    {
        if (mouseEventButton != 0) // left click only
        {
            return;
        }

        List<GuiElement> clickable = new ArrayList<>();
        rootContainer.collectMatchingElements(clickable, element -> (element.isVisible() && element.isHovered() && canClickOn(element)));

        if (!clickable.isEmpty())
        {
            // TODO: should currentFocus be an InputListener?
            currentFocus = CollectionUtils.getMax(clickable, Comparator.comparingInt(GuiElement::getZLevel));

            System.out.println("greatestZElement: " + currentFocus);

            dispatchTargetedEvent(new MouseDownEvent(currentFocus, mouseX, mouseY));
        }
    }

    /**
     * Fired when a mouse button is released up.
     * Downstream of {@link GuiScreen#handleMouseInput()}
     */
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseEventButton)
    {
        if (mouseEventButton != 0) // left click only
        {
            return;
        }

        if (hasCurrentFocus() && canClickOn(currentFocus) && ((MouseClickListener) currentFocus).isMouseDown())
        {
            dispatchTargetedEvent(new MouseUpEvent(currentFocus, mouseX, mouseY));
        }
    }

    /**
     * Fired when the mouse is moved around while pressed.
     * Downstream of {@link GuiScreen#handleMouseInput()}
     */
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
        if (hasCurrentFocus() && canClickOn(currentFocus) && ((MouseClickListener) currentFocus).isMouseDown())
        {
            dispatchTargetedEvent(new MouseDragEvent(currentFocus, mouseX, mouseY, clickedMouseButton, msHeld));
        }
    }

    private static boolean canClickOn(GuiElement element)
    {
        return element instanceof MouseClickListener && ((InputListener) element).isEnabled();
    }

    private static boolean canScrollOn(GuiElement element)
    {
        return element instanceof MouseScrollListener && ((InputListener) element).isEnabled();
    }

    private static void dispatchTargetedEvent(GuiTargetedEvent event)
    {
        // Build propagation path: root -> ... -> target
        List<GuiElement> path = event.target.getPropagationPath();

        for (GuiElement element : path)
        {
            event.tryDispatchTo(element);

            if (event.hasPropagationStopped())
            {
                return;
            }
        }
    }
}
