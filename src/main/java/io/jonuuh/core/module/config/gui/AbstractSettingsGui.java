package io.jonuuh.core.module.config.gui;

import io.jonuuh.core.module.config.gui.elements.GuiTooltip;
import io.jonuuh.core.module.config.gui.elements.interactable.GuiTextFieldVanilla;
import io.jonuuh.core.module.config.gui.elements.interactable.GuiTextField;
import io.jonuuh.core.module.config.gui.elements.interactable.sliders.GuiIntSlider;
import io.jonuuh.core.module.config.gui.elements.interactable.sliders.GuiSlider;
import io.jonuuh.core.module.config.setting.types.BoolSetting;
import io.jonuuh.core.module.config.setting.Setting;
import io.jonuuh.core.module.config.setting.SettingDefinition;
import io.jonuuh.core.module.config.setting.Settings;
import io.jonuuh.core.module.config.Config;
import io.jonuuh.core.module.config.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.core.module.config.gui.elements.interactable.GuiSwitch;
import io.jonuuh.core.module.config.setting.types.DoubleListSetting;
import io.jonuuh.core.module.config.setting.types.DoubleSetting;
import io.jonuuh.core.module.config.setting.types.IntListSetting;
import io.jonuuh.core.module.config.setting.types.IntSetting;
import io.jonuuh.core.module.util.Color;
import io.jonuuh.core.module.util.GuiUtils;
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
    protected GuiInteractableElement lastInteracted;
    public GuiTextFieldVanilla textField;
    public GuiTextField textField2;

    protected int[] center;

    public AbstractSettingsGui(Settings settings) // maybe should pass subclass instance here for element parents?
    {
        this.settings = settings;
        this.elementMap = initElementMap();

        if (elementMap.size() != settings.size())
        {
            for (String settingName : settings.keySet())
            {
                if (elementMap.get(settingName) == null)
                {
                    System.out.println("[CONFIG WARNING] No UI element found for setting '" + settingName + "'");
                }
            }

            for (String elementName : elementMap.keySet())
            {
                if (settings.get(elementName) == null)
                {
                    System.out.println("[CONFIG WARNING] No setting found for UI element '" + elementName + "'");
                }
            }
        }

        this.inverseElementMap = new HashMap<>();
        for (Map.Entry<String, GuiInteractableElement> entry : elementMap.entrySet())
        {
            inverseElementMap.put(entry.getValue(), entry.getKey());
        }

        GuiTooltip.createInstance(Minecraft.getMinecraft());
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
        textField = new GuiTextFieldVanilla(this.fontRendererObj, this.width / 2 - 100/*- 75*/, 50, 200, 20);

        textField2 = new GuiTextField(this, -100, -100, "12345678901234567890");
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
        Config.getInstance().saveSettings(settings.configurationCategory);

        System.out.println(settings);
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

//        System.out.println("mouse down");

        textField.mouseClicked(mouseX, mouseY, mouseEventButton);

        for (GuiInteractableElement element : elementMap.values())
        {
            if (element.onMouseDownAmbiguous(mouseX, mouseY))
            {
                lastInteracted = element;
                break;
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
    public Minecraft getMc()
    {
        return mc;
    }

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
        return 16;
    }

    @Override
    public void onChange(GuiInteractableElement element)
    {
        String settingName = inverseElementMap.get(element);
        Setting<?> setting = settings.get(settingName);

        if (setting == null)
        {
            return;
        }

        if (element instanceof GuiSwitch)
        {
            ((BoolSetting) setting).setValue(((GuiSwitch) element).getSwitchState());
        }
        else if (element instanceof GuiIntSlider)
        {
            GuiIntSlider intSlider = ((GuiIntSlider) element);
            if (intSlider.getLength() == 1)
            {
                ((IntSetting) setting).setValue(intSlider.getIntValue(0));
            }
            else
            {
                ((IntListSetting) setting).setValue(intSlider.getIntValues());
            }
        }
        else if (element instanceof GuiSlider)
        {
            GuiSlider slider = ((GuiSlider) element);
            if (slider.getLength() == 1)
            {
                ((DoubleSetting) setting).setValue(slider.getValue(0));
            }
            else
            {
                ((DoubleListSetting) setting).setValue(slider.getValues());
            }
        }

//        else if (element instanceof GuiSliderBase)
//        {
//            ((IntListSetting) setting).setValue(((GuiSliderBase) element).getValues());
//        }
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
