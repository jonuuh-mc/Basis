package io.jonuuh.core.lib.config.gui;

import io.jonuuh.core.lib.config.gui.elements.container.GuiContainer;
import io.jonuuh.core.lib.config.gui.elements.GuiElement;
import io.jonuuh.core.lib.config.gui.event.CloseGuiEvent;
import io.jonuuh.core.lib.config.gui.event.InitGuiEvent;
import io.jonuuh.core.lib.config.gui.event.KeyInputEvent;
import io.jonuuh.core.lib.config.gui.event.MouseDownEvent;
import io.jonuuh.core.lib.config.gui.event.MouseDragEvent;
import io.jonuuh.core.lib.config.gui.event.MouseScrollEvent;
import io.jonuuh.core.lib.config.gui.event.ScreenDrawEvent;
import io.jonuuh.core.lib.config.gui.event.ScreenTickEvent;
import io.jonuuh.core.lib.util.MathUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public abstract class AbstractGuiScreen extends GuiScreen
{
    protected GuiContainer rootContainer;
    protected GuiElement currentFocus;

    public AbstractGuiScreen()
    {
//        this.rootContainer = rootContainer;
//        rootContainer.getNestedChildren().forEach(System.out::println);
//
//        // TODO: make recursive instead of using getNestedChildren?
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

//        if (elementMap.size() != settings.size())
//        {
//            for (String settingName : settings.keySet())
//            {
//                if (elementMap.get(settingName) == null)
//                {
//                    Log4JLogger.INSTANCE.warn("No UI element found for setting '{}'", settingName);
//                }
//            }
//
//            for (String elementName : elementMap.keySet())
//            {
//                if (settings.get(elementName) == null)
//                {
//                    Log4JLogger.INSTANCE.warn("No setting found for UI element '{}'", elementName);
//                }
//            }
//        }

//        this.inverseElementMap = new HashMap<>();
//        for (Map.Entry<String, GuiInteractableElement> entry : elementMap.entrySet())
//        {
//            inverseElementMap.put(entry.getValue(), entry.getKey());
//        }

//        GuiTooltip.createInstance(Minecraft.getMinecraft());
    }

    protected abstract GuiContainer initRootContainer();

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);

//        rootContainer.setLocalXPos(Math.max((width / 2) - (rootContainer.getInitialWidth() / 2), 0));
//        rootContainer.setLocalYPos(Math.max((height / 2) - (rootContainer.getInitialHeight() / 2), 0));
//
//        rootContainer.setWidth(Math.min(width - rootContainer.localXPos() /*- (width / 10)*/, rootContainer.getInitialWidth()));
//        rootContainer.setHeight(Math.min(height - rootContainer.localYPos() /*- (height / 10)*/, rootContainer.getInitialHeight()));

        rootContainer.propagateEvent(new InitGuiEvent(width, height));
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        currentFocus = null; // TODO: focused element should lose focus on gui close
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

//        attemptClaimDefaultTooltip(mouseX, mouseY);
//        // Can be true either by the above function or individual "claims" of the tooltip within element classes (e.g. GuiAbstractSlider)
//        if (GuiTooltip.isClaimed())
//        {
//            GuiTooltip.draw();
//        }
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

        System.out.println("mouseDownElements: " + event.mouseDownElements);
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

//        rootContainer.propagateEvent(new MouseUpEvent(mouseX,  mouseY, mouseEventButton));
        if (currentFocus != null && currentFocus.isMouseDown())
        {
            currentFocus.dispatchMouseUpEvent(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
        rootContainer.propagateEvent(new MouseDragEvent(mouseX, mouseY, clickedMouseButton, msHeld));
//        System.out.println("mouseClickMove: " + mouseX + " " + mouseY + " " + clickedMouseButton + " " + msHeld);
    }

//    /**
//     * If tooltip was not already claimed (in onScreenDraw for an indiv. element),
//     * track hovering time and draw tooltip if an element has been hovered for >= 500ms
//     */
//    protected void attemptClaimDefaultTooltip(int mouseX, int mouseY)
//    {
//        if (GuiTooltip.isClaimed())
//        {
//            return;
//        }
//
//        for (GuiElement element : containerMap.get("CONTAINER").getChildren())
//        {
//            if (element.isHovered() /*&& !element.getTooltipStr().isEmpty()*/)
//            {
//                if (element.startHoverTime == 0)
//                {
//                    element.startHoverTime = Minecraft.getSystemTime();
//                }
//
//                element.hoverTimeMs = (int) (Minecraft.getSystemTime() - element.startHoverTime);
//
//                if (element.hoverTimeMs >= 500)
//                {
//                    element.claimHoverTooltip(mouseX, mouseY);
//                }
//
//                break; // only track hover time for one element at a time (assume elements are not overlapping) TODO: this might be problematic
//            }
//            else if (element.startHoverTime != 0 || element.hoverTimeMs != 0)
//            {
//                element.startHoverTime = 0;
//                element.hoverTimeMs = 0;
//            }
//        }
//    }
}
