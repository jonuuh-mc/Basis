package io.jonuuh.core.lib.config.gui;

import io.jonuuh.core.lib.config.gui.elements.GuiContainer;
import io.jonuuh.core.lib.config.gui.elements.GuiElement;
import io.jonuuh.core.lib.config.gui.elements.GuiTooltip;
import io.jonuuh.core.lib.config.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.core.lib.config.gui.elements.interactable.GuiTextFieldVanilla;
import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.config.setting.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractSettingsGui extends GuiScreen
{
    protected final Settings settings;
    protected final GuiContainer rootContainer;
    //    protected final Map<GuiInteractableElement, String> inverseElementMap;
//    protected GuiInteractableElement lastInteracted;
    public GuiTextFieldVanilla textField;

    protected int[] center;

    public AbstractSettingsGui(Settings settings) // maybe should pass subclass instance here for element parents?
    {
        this.settings = settings;
        this.rootContainer = initRootContainer();

        // TODO: not recursive at all, only associates top level
        for (Map.Entry<String, GuiElement> entry : rootContainer.getChildrenMap().entrySet())
        {
            String elementName = entry.getKey();
            GuiElement element = entry.getValue();

            Setting<?> setting = settings.get(elementName);

            if (setting != null)
            {
                ((GuiInteractableElement) element).associateSetting(setting);
                System.out.println("associated setting for: " + elementName);
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

        GuiTooltip.createInstance(Minecraft.getMinecraft());
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

        // TODO: labelList not cleared because it doesn't matter? their values don't change after gui is finalized?
        // TODO: create labellist in constructor? check vanilla code
//        labelList.clear(); // buttonList is cleared before init is called, but labelList isn't for some reason
//        GuiLabel label = new GuiLabel(mc.fontRendererObj, 0, (width / 2), (height / 2), 0, 0, -1);
//        label.func_175202_a("Label");
//        labelList.add(label.setCentered());

//        buttonList.addAll(elementSettingNameMap.keySet());

        Keyboard.enableRepeatEvents(true);
        textField = new GuiTextFieldVanilla(this.fontRendererObj, this.width / 2 - 100/*- 75*/, 50, 200, 20);

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
//        GL11.glDisable(GL11.GL_SCISSOR_TEST);
//        GL11.glPopMatrix();

        System.out.println(settings);
    }

    @Override
    public void updateScreen()
    {
        rootContainer.handleScreenTick();
        textField.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);

        rootContainer.handleKeyTyped(typedChar, keyCode);
        textField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        textField.drawTextBox();
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

//        int i = Mouse.getEventDWheel();
//
//        if (i != 0)
//        {
//            i = (int) MathUtils.clamp(i, -1, 1);
////            i = !isShiftKeyDown() ? i * 7 : i;
//
////            this.mc.ingameGUI.getChatGUI().scroll(i);
//        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseEventButton)
    {
        if (mouseEventButton != 0) // left click only
        {
            return;
        }

        textField.mouseClicked(mouseX, mouseY, mouseEventButton);

        List<GuiInteractableElement> mouseDownElements = rootContainer.handleMouseDown(mouseX, mouseY);
        System.out.println(mouseDownElements);

        if (mouseDownElements.isEmpty())
        {
            return;
        }

        GuiInteractableElement highestZLevelElement = mouseDownElements.get(0);

        for (GuiInteractableElement element : mouseDownElements)
        {
            if (element.getZLevel() > highestZLevelElement.getZLevel())
            {
                highestZLevelElement = element;
            }
        }

        highestZLevelElement.playPressSound(this.mc.getSoundHandler());
        highestZLevelElement.onMousePress(mouseX, mouseY);
//        lastMouseDownElement = highestZLevelElement;
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

//    @Override
//    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
//    {
//        System.out.println("mouseClickMove: " + mouseX + " " + mouseY + " " + clickedMouseButton + " " + timeSinceLastClick);
//    }
}
