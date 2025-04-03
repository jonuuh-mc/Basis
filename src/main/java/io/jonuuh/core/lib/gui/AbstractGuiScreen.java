package io.jonuuh.core.lib.gui;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import io.jonuuh.core.lib.gui.event.CloseGuiEvent;
import io.jonuuh.core.lib.gui.event.InitGuiEvent;
import io.jonuuh.core.lib.gui.event.KeyInputEvent;
import io.jonuuh.core.lib.gui.event.MouseDownEvent;
import io.jonuuh.core.lib.gui.event.MouseDragEvent;
import io.jonuuh.core.lib.gui.event.MouseScrollEvent;
import io.jonuuh.core.lib.gui.event.ScreenDrawEvent;
import io.jonuuh.core.lib.gui.event.ScreenTickEvent;
import io.jonuuh.core.lib.util.MathUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public abstract class AbstractGuiScreen extends GuiScreen
{
    protected GuiContainer rootContainer;
    protected GuiElement currentFocus;
    // TODO: implement something like this later? could be shared between all guiscreens of a mod
//    protected FontRenderer customFontRenderer = new FontRenderer(mc.gameSettings, new ResourceLocation("core:ascii.png"), mc.renderEngine, false);


    public AbstractGuiScreen()
    {
//        for (GuiElement element : rootContainer.getNestedChildren())
//        {
//            Setting<?> setting = settings.get(element.elementName);
//
//            if (setting != null && element instanceof GuiSettingElement)
//            {
//                ((GuiSettingElement) element).associateSetting(setting);
//                System.out.println("associated setting for: " + element.elementName);
//            }
//        }
    }

    protected abstract GuiContainer initRootContainer();

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Called when the GUI is displayed or when the window resizes
     */
    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);

//        rootContainer.setWidth(50); // shows
        rootContainer.propagateEvent(new InitGuiEvent(new ScaledResolution(mc)));
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        currentFocus = null;
        rootContainer.propagateEvent(new CloseGuiEvent());
    }

    @Override
    public void updateScreen()
    {
        rootContainer.propagateEvent(new ScreenTickEvent());
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        rootContainer.propagateEvent(new KeyInputEvent(typedChar, keyCode));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        rootContainer.propagateEvent(new ScreenDrawEvent(mouseX, mouseY, partialTicks));
    }

    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();

        int wheelDelta = Mouse.getEventDWheel();

        if (wheelDelta != 0)
        {
            wheelDelta = (int) MathUtils.clamp(wheelDelta, -1, 1);
            rootContainer.propagateEvent(new MouseScrollEvent(wheelDelta));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseEventButton)
    {
        if (mouseEventButton != 0) // left click only
        {
            return;
        }

        MouseDownEvent event = new MouseDownEvent(mouseX, mouseY, mouseEventButton);
        rootContainer.propagateEvent(event);

        if (!event.hasHits())
        {
            return;
        }

        GuiElement greatestZElement = event.mouseDownElements.get(0);
        for (GuiElement element : event.mouseDownElements)
        {
            if (element.getZLevel() > greatestZElement.getZLevel())
            {
                greatestZElement = element;
            }
        }

//        System.out.println("mouseDownElements: " + event.mouseDownElements);
        System.out.println("greatestZElement: " + greatestZElement);

        greatestZElement.dispatchMouseDownEvent(mouseX, mouseY);
        currentFocus = greatestZElement;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseEventButton)
    {
        if (mouseEventButton != 0)
        {
            return;
        }

        if (currentFocus != null && currentFocus.isMouseDown())
        {
            currentFocus.dispatchMouseUpEvent(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
        rootContainer.propagateEvent(new MouseDragEvent(mouseX, mouseY, clickedMouseButton, msHeld));
    }
}
