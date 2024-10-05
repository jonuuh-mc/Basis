package io.jonuuh.configlib.local;

import io.jonuuh.configlib.gui.AbstractSettingsGui;
import io.jonuuh.configlib.gui.elements.GuiAbstractSlider;
import io.jonuuh.configlib.gui.elements.GuiInteractableElement;
import io.jonuuh.configlib.gui.elements.GuiSlider;
import io.jonuuh.configlib.gui.elements.GuiSwitch;
import io.jonuuh.configlib.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SettingsGuiImpl extends AbstractSettingsGui
{
    public SettingsGuiImpl()
    {
        super(Config.getInstance().getSettings("master"));
    }

    @Override
    protected Map<GuiInteractableElement, String> initElementSettingNameMap()
    {
        Map<GuiInteractableElement, String> elementMap = new HashMap<>();

        elementMap.put(new GuiSwitch(this, 100, 100, settings.getBoolSettingValue("DRAW_BACKGROUND")), "DRAW_BACKGROUND");
        elementMap.put(new GuiSlider(this, 100, 125, 0, 110, settings.getDoubleSettingValue("BACKGROUND_OPACITY")), "BACKGROUND_OPACITY");
//        elementMap.put(new GuiIntSlider(this, 100, 200, 0, 32, settings.getIntSettingValue("RENDER_RANGE_MIN")), "RENDER_RANGE_MIN");

//        List<String> chatFormatting = Arrays.stream(EnumChatFormatting.values()).map(Enum::name).collect(Collectors.toList());
//        elementMap.put(new GuiSelectorSlider<String>(this, 100, 250, chatFormatting, settings.getStringSettingValue("BACKGROUND_COLOR")), "BACKGROUND_COLOR");

        elementMap.put(new GuiAbstractSlider(this, 100, 150, 0, 110, Collections.singletonList(50D)), "RENDER_RANGE_MAX");

        elementMap.put(new GuiAbstractSlider(this, 100, 175, -41, 72, Arrays.asList(-27D, 42D)), "RENDER_RANGE_MAX");

        // TODO: (2.5 - 7.5): Arrays.asList(8D, 16D) bug? (both out of range therefore select <?> on click?)
        elementMap.put(new GuiAbstractSlider(this, 100, 200, 200, 10, 10, 0, 50,
                Arrays.asList(10D, 15D, 20D, 5D, 30D, 45D, 1000D)), "RENDER_RANGE_MIN");



        return elementMap;
    }
}
