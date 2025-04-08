package io.jonuuh.core.lib.gui;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.element.container.GuiRootContainer;
import io.jonuuh.core.lib.util.MathUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

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
     * Called when this screen is first displayed or when the window resizes
     */
    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        rootContainer.performAction(element -> element.handleInitGuiEvent(new ScaledResolution(mc)));
    }

    /**
     * Called when this screen is unloaded
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
     * Mainly used to draw the elements on the screen but also as a very frequent event ticker.
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
            currentFocus.handleKeyInputEvent(typedChar, keyCode);
        }
    }

    /**
     * Used to parse lwjgl {@link Mouse} event states.
     * By default, calls mouseClicked, mouseReleased, and mouseClickMove via GuiScreen superclass,
     * but can be extended as is done here for more features like mouse wheel states
     */
    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();

        int wheelDelta = (int) MathUtils.clamp(Mouse.getEventDWheel(), -1, 1);

        if (hasCurrentFocus() && wheelDelta != 0)
        {
            currentFocus.handleMouseScrollEvent(wheelDelta);
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

        currentFocus = rootContainer.getGreatestZLevelHovered(rootContainer);
        System.out.println("greatestZElement: " + currentFocus);
        currentFocus.handleMouseDownEvent(mouseX, mouseY);
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

        if (hasCurrentFocus() && currentFocus.isMouseDown())
        {
            currentFocus.handleMouseUpEvent(mouseX, mouseY);
        }
    }

    /**
     * Fired when the mouse is moved around while pressed.
     * Downstream of {@link GuiScreen#handleMouseInput()}
     */
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
        rootContainer.performAction(element -> element.handleMouseDragEvent(mouseX, mouseY, clickedMouseButton, msHeld));
    }
}
