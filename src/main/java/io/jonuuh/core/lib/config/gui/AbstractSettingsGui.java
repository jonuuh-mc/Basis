package io.jonuuh.core.lib.config.gui;

import io.jonuuh.core.lib.config.gui.elements.GuiContainer;
import io.jonuuh.core.lib.config.gui.elements.GuiElement;
import io.jonuuh.core.lib.config.gui.elements.GuiTooltip;
import io.jonuuh.core.lib.config.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.core.lib.config.gui.elements.interactable.GuiSettingElement;
import io.jonuuh.core.lib.config.gui.elements.interactable.GuiTextFieldVanilla;
import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.util.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractSettingsGui extends GuiScreen
{
    protected final Settings settings;
    protected final GuiContainer rootContainer;
    //    protected final Map<GuiInteractableElement, String> inverseElementMap;
//    protected GuiInteractableElement lastInteracted;
//    public GuiTextFieldVanilla textField;

    protected int[] center;

    public AbstractSettingsGui(Settings settings) // maybe should pass subclass instance here for element parents?
    {
        this.settings = settings;
        this.rootContainer = initRootContainer();

        rootContainer.getNestedChildren().forEach(System.out::println);

        // TODO: make recursive instead of using getNestedChildren?
        for (GuiElement element : rootContainer.getNestedChildren())
        {
            Setting<?> setting = settings.get(element.elementName);

            if (setting != null && element instanceof GuiSettingElement)
            {
                ((GuiSettingElement) element).associateSetting(setting);
                System.out.println("associated setting for: " + element.elementName);
            }
        }

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

    // TODO: some textfield logic: `this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;`

    // TODO: should be in ISettingsGui maybe?
    protected abstract GuiContainer initRootContainer();

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui()
    {
        center = new int[]{(this.width / 2), (this.height / 2)};

        Keyboard.enableRepeatEvents(true);
//        textField = new GuiTextFieldVanilla(this.fontRendererObj, this.width / 2 - 100/*- 75*/, 50, 200, 20);

        rootContainer.setXPos((this.width / 2) - (rootContainer.getWidth() / 2));
        rootContainer.setYPos((this.height / 2) - (rootContainer.getHeight() / 2));

//        elementMap.put("TESTFIELD", new GuiTextField(this, -100, -100, "12345678901234567890"));
//
//        for (GuiInteractableElement element : elementMap.values())
//        {
//            element.addXPos((width / 2) /*- (w / 2)*/);
//            element.addYPos((height / 2) - (120 / 3));
//        }
//
//        GL11.glPushMatrix();
//        GL11.glEnable(GL11.GL_SCISSOR_TEST);
//        RenderUtils.scissorFromTopLeft(100, 100, 250, 100);
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
        settings.save();

        // TODO: focused element should lose focus on gui close

        System.out.println(settings);
    }

    @Override
    public void updateScreen()
    {
        rootContainer.handleScreenTick();
//        textField.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);

        rootContainer.handleKeyTyped(typedChar, keyCode);
//        textField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
//        textField.drawTextBox();
        rootContainer.onScreenDraw(mouseX, mouseY, partialTicks);

//        attemptClaimDefaultTooltip(mouseX, mouseY);
//
//        // Can be true either by the above function or individual "claims" of the tooltip within element classes (e.g. GuiAbstractSlider)
//        if (GuiTooltip.isClaimed())
//        {
//            GuiTooltip.draw();
//        }
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

    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();

        int wheelDelta = Mouse.getEventDWheel();

        if (wheelDelta != 0)
        {
            wheelDelta = (int) MathUtils.clamp(wheelDelta, -1, 1);
            rootContainer.handleMouseScroll(wheelDelta);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseEventButton)
    {
        if (mouseEventButton != 0) // left click only
        {
            return;
        }

//        textField.mouseClicked(mouseX, mouseY, mouseEventButton);

        rootContainer.handleMouseDown(mouseX, mouseY);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseEventButton)
    {
        if (mouseEventButton != 0)
        {
            return;
        }

        rootContainer.handleMouseRelease(mouseX, mouseY);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
        rootContainer.handleMouseDrag(mouseX, mouseY, clickedMouseButton, msHeld);
//        System.out.println("mouseClickMove: " + mouseX + " " + mouseY + " " + clickedMouseButton + " " + msHeld);
    }
}
