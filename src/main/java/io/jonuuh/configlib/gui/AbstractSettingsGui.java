package io.jonuuh.configlib.gui;

import io.jonuuh.configlib.gui.elements.GuiTooltip;
import io.jonuuh.configlib.gui.elements.interactable.GuiTextField;
import io.jonuuh.configlib.gui.elements.interactable.GuiTextField2;
import io.jonuuh.configlib.setting.BoolSetting;
import io.jonuuh.configlib.setting.DoubleSetting;
import io.jonuuh.configlib.setting.IntSetting;
import io.jonuuh.configlib.setting.Setting;
import io.jonuuh.configlib.setting.Settings;
import io.jonuuh.configlib.Config;
import io.jonuuh.configlib.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.configlib.gui.elements.interactable.GuiSwitch;
import io.jonuuh.configlib.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSettingsGui extends GuiScreen implements ISettingsGui
{
    protected final Settings settings;
    protected final Map<String, GuiInteractableElement> elementMap;
    protected final Map<GuiInteractableElement, String> inverseElementMap;
    //    protected final GuiTooltip guiTooltipSingleton;
    protected GuiInteractableElement lastInteracted;
    public GuiTextField textField;
    public GuiTextField2 textField2;

    protected int[] center;

    public AbstractSettingsGui(Settings settings) // maybe should pass subclass instance here for element parents?
    {
        this.settings = settings;
        this.elementMap = initElementMap();

        this.inverseElementMap = new HashMap<>();
        for (Map.Entry<String, GuiInteractableElement> entry : elementMap.entrySet())
        {
            inverseElementMap.put(entry.getValue(), entry.getKey());
        }

        GuiTooltip.createInstance(Minecraft.getMinecraft());
//        guiTooltipSingleton = GuiTooltip.getInstance();
    }

    // TODO: some textfield logic: `this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;`

    // TODO: should be in ISettingsGui maybe?
    protected abstract Map<String, GuiInteractableElement> initElementMap();

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
        textField = new GuiTextField(this.fontRendererObj, this.width / 2 - 100/*- 75*/, 50, 200, 20);

        textField2 = new GuiTextField2(this, -100, -100);
        elementMap.put("TESTFIELD", textField2);

//        System.out.println(Integer.toHexString(-16777216));
//        System.out.println(Integer.toHexString(-6250336));

//        Color c = new Color("#56ff8b");
//        System.out.println(c);
//        System.out.println(c.toDecimalARGB());

//        System.out.println(new Color(-6250336));
//        System.out.println(new Color("a0a0a0"));

//        System.out.println(new Color(-16777216));

//        Color.convert(-16777216);
//        Color.convert(-6250336);
//
//        System.out.println(Color.convertToInt(255, 160, 160, 160));
//        System.out.println(Color.convertToInt(255, 0, 0, 0));
//        System.out.println(Color.convertToInt(255, 255, 255, 255));

//        this.commandTextField.setMaxStringLength(32767);
//        this.commandTextField.setFocused(true);
//        this.commandTextField.setText(this.localCommandBlock.getCommand());

        int w = 200;
        int h = 120;
        int pad = 5;

        for (GuiInteractableElement element : elementMap.values())
        {
            element.addXPos((width / 2) /*- (w / 2)*/);
            element.addYPos((height / 2) - (h / 3));
//            element.setXPos(width / 4);
//            element.setYPos((int) (element.getYPos() + (height / 2.5)));

//            element.setWidth(width / 2);
        }
//
//        ScaledResolution sr = new ScaledResolution(mc);
//        System.out.println(sr.getScaledWidth());
//        this.scale = (float) mc.displayWidth / mc.displayHeight;
//        System.out.println(scale);

//        this.scale = 0.5F;
//        scale = mc.gameSettings.guiScale;
//        System.out.println(scale);

//        if (sr.getScaledWidth() > 427)
//        {
//            this.scale = 2F;
//        }
//        else
//        {
//            this.scale = 1F;
//        }

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
        Config.getInstance().saveSettings(settings.getConfigurationCategory());
    }

    @Override
    public void updateScreen()
    {
        elementMap.values().forEach(GuiInteractableElement::onScreenTick);
        textField.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        elementMap.values().forEach(element -> element.onKeyTyped(typedChar, keyCode));

        textField.textboxKeyTyped(typedChar, keyCode);
    }

    protected void drawBackground()
    {
//        float w = (width / 2F);
//        float h = (height / 2F);
//        float hO = (height / 2.5F);
//
//        GuiUtils.drawRoundedRect(GL11.GL_POLYGON, w / 2 , hO, w, h, 6, new Color("#242424").setA(0.8F), true);

        int w = 200; // base element width
        int h = 120;
        int pad = 5;

//        GuiUtils.drawRoundedRect(GL11.GL_POLYGON, (width / 2F) - (w) - pad, (height / 2F) - (h / 3F) - pad, (w * 2) + (pad * 2), h + (pad * 2), 6, new Color("#242424").setA(0.8F), true);

        GuiUtils.drawRoundedRect(GL11.GL_POLYGON, (width / 2F) - (w / 2F) - pad, (height / 2F) - (h / 3F) - pad, w + (pad * 2), h + (pad * 2), 6, new Color("#242424").setA(0.8F), true);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
//        if (scale != 1)
//        {
//            GL11.glPushMatrix();
//            GL11.glTranslatef(center[0], center[1], 0.0F); // translate to point of tooltip triangle
//            GL11.glScalef(scale, scale, scale);
//            GL11.glTranslatef(-center[0], -(center[1]), 0.0F);
//        }

        drawBackground();

        textField.drawTextBox();

        for (GuiInteractableElement element : elementMap.values())
        {
//            element.addXPos((int) w);

            element.onScreenDraw(mc, mouseX, mouseY);
        }

        attemptClaimDefaultTooltip(mouseX, mouseY);

        // Can be true either by the above function or individual "claims" of the tooltip within element classes (e.g. GuiAbstractSlider)
        if (GuiTooltip.isClaimed())
        {
            GuiTooltip.draw();
        }
//
//        if (scale != 1)
//        {
//            GL11.glPopMatrix();
//        }
    }

    /**
     * If tooltip was not already claimed (in onScreenDraw for an indiv. element),
     * track hovering time and draw tooltip if an element has been hovered for >= 500ms
     */
    protected void attemptClaimDefaultTooltip(int mouseX, int mouseY)
    {
        if (GuiTooltip.isClaimed())
        {
            return;
        }

        for (GuiInteractableElement element : elementMap.values())
        {
            if (element.isHovered() /*&& !element.getTooltipStr().isEmpty()*/)
            {
                if (element.startHoverTime == 0)
                {
                    element.startHoverTime = Minecraft.getSystemTime();
                }

                element.hoverTimeMs = (int) (Minecraft.getSystemTime() - element.startHoverTime);

                if (element.hoverTimeMs >= 500)
                {
                    element.claimHoverTooltip(mouseX, mouseY);
                }

                break; // only track hover time for one element at a time (assume elements are not overlapping) TODO: this might be problematic
            }
            else if (element.startHoverTime != 0 || element.hoverTimeMs != 0)
            {
                element.startHoverTime = 0;
                element.hoverTimeMs = 0;
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseEventButton)
    {
        if (mouseEventButton != 0) // left click only
        {
            return;
        }

        textField.mouseClicked(mouseX, mouseY, mouseEventButton);
//        textField2.onMousePress(mouseX, mouseY);

        for (GuiInteractableElement element : elementMap.values())
        {
            // TODO: enabled -> "canPress"?
            if (element.isEnabled() && element.isVisible() && element.isHovered()/*element.canMousePress(mouseX, mouseY)*/)
            {
                lastInteracted = element;
                element.playPressSound(mc.getSoundHandler());
                element.onMousePress(mouseX, mouseY);
                break; // prevent clicking two elements at once
            }
        }

        for (GuiInteractableElement element : elementMap.values())
        {
            if (!element.isHovered())
            {
                element.setFocused(false);
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseEventButton)
    {
        if (mouseEventButton == 0 && this.lastInteracted != null)
        {
            lastInteracted.onMouseRelease(mouseX, mouseY);
            lastInteracted = null;
        }
    }

//    @Override
//    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
//    {
//        System.out.println("mouseClickMove: " + mouseX + " " + mouseY + " " + clickedMouseButton + " " + timeSinceLastClick);
//    }

    @Override
    public Color getBaseColor()
    {
        return new Color("#1450A0"); // 6e0d41 1450A0
    }

    @Override
    public Color getAccentColor()
    {
        return new Color();
    }

    @Override
    public Color getDisabledColor()
    {
        return new Color("#484848");
    }

    public float getOuterRadius()
    {
        return 4;
    }

    public float getInnerRadius()
    {
        return 32;
    }

    @Override
    public void onChange(GuiInteractableElement element)
    {
        String settingName = inverseElementMap.get(element);
        Setting<?> setting = settings.get(settingName);

        if (element instanceof GuiSwitch)
        {
            ((BoolSetting) setting).setValue(((GuiSwitch) element).getSwitchState());
        }
//        else if (element instanceof GuiSelectorSlider) // must be before superclass (GuiSlider)
//        {
//            ((StringSetting) setting).setValue(((GuiSelectorSlider<?>) element).getSelectedElement().toString());
//        }
//        else if (element instanceof GuiIntSlider) // must be before superclass (GuiSlider)
//        {
//            ((IntSetting) setting).setValue(((GuiIntSlider) element).getValueInt());
//        }
//        else if (element instanceof GuiSlider)
//        {
//            ((DoubleSetting) setting).setValue(((GuiSlider) element).getValue());
//        }
    }
}
