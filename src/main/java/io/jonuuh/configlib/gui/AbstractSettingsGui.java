package io.jonuuh.configlib.gui;

import io.jonuuh.configlib.gui.elements.GuiSelectorSlider;
import io.jonuuh.configlib.setting.BoolSetting;
import io.jonuuh.configlib.setting.DoubleSetting;
import io.jonuuh.configlib.setting.IntSetting;
import io.jonuuh.configlib.setting.Setting;
import io.jonuuh.configlib.setting.Settings;
import io.jonuuh.configlib.Config;
import io.jonuuh.configlib.gui.elements.GuiIntSlider;
import io.jonuuh.configlib.gui.elements.GuiInteractableElement;
import io.jonuuh.configlib.gui.elements.GuiSlider;
import io.jonuuh.configlib.gui.elements.GuiSwitch;
import io.jonuuh.configlib.setting.StringSetting;
import io.jonuuh.configlib.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Map;

public abstract class AbstractSettingsGui extends GuiScreen implements ISettingsGui
{
    protected final Minecraft mc;
    protected final Settings settings;
    protected final Map<GuiInteractableElement, String> elementSettingNameMap;

    protected int[] center;

    public AbstractSettingsGui(Settings settings) // maybe should pass subclass instance here for element parents?
    {
        this.mc = Minecraft.getMinecraft();
        this.settings = settings;
        this.elementSettingNameMap = initElementSettingNameMap();
    }

    protected abstract Map<GuiInteractableElement, String> initElementSettingNameMap();

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

        buttonList.addAll(elementSettingNameMap.keySet());
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void onGuiClosed()
    {
        Config.getInstance().saveSettings(settings.getConfigurationCategory());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
//        Util.drawRoundedRect(GL11.GL_POLYGON, center[0] - 50, center[1] - 50, 100, 200, 6, Util.hexToNormalizedRBG("#242424").setA(0.8F), true);
        super.drawScreen(mouseX, mouseY, partialTicks);

//        // Tooltip handling
//        for (GuiButton button : this.buttonList)
//        {
//            if (!button.isMouseOver())
//            {
//                continue;
//            }
//
//            this.drawHoveringText(Collections.singletonList("Tooltip"), mouseX, mouseY);
//            return; // mouse couldn't be hovering two buttons at once
//        }
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

    @Override
    public void onChange(GuiInteractableElement element)
    {
        String settingName = elementSettingNameMap.get(element);
        Setting<?> setting = settings.get(settingName);

        if (element instanceof GuiSwitch)
        {
            ((BoolSetting) setting).setValue(((GuiSwitch) element).getSwitchState());
        }
        else if (element instanceof GuiSelectorSlider) // must be before superclass (GuiSlider)
        {
            ((StringSetting) setting).setValue(((GuiSelectorSlider<?>) element).getSelectedElement().toString());
        }
        else if (element instanceof GuiIntSlider) // must be before superclass (GuiSlider)
        {
            ((IntSetting) setting).setValue(((GuiIntSlider) element).getValueInt());
        }
        else if (element instanceof GuiSlider)
        {
            ((DoubleSetting) setting).setValue(((GuiSlider) element).getValue());
        }
    }
}
