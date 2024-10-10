package io.jonuuh.configlib.local;

import io.jonuuh.configlib.gui.AbstractSettingsGui;
import io.jonuuh.configlib.gui.elements.interactable.sliders.GuiSliderBase;
import io.jonuuh.configlib.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.configlib.gui.elements.interactable.GuiSwitch;
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
    protected Map<String, GuiInteractableElement> initElementMap()
    {
//        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
//        int sW = scaledresolution.getScaledWidth();
//        int sH = scaledresolution.getScaledHeight();
//        int sW2 = (sW / 2);
//        int sH2 = (sH / 2);
//
//        int sW4 = (sW / 4);
//        int sH4 = (sH / 4);

        Map<String, GuiInteractableElement> map = new HashMap<>();

        map.put("DRAW_BACKGROUND", new GuiSwitch(this, -100, 0, "Whether to draw background", settings.getBoolSettingValue("DRAW_BACKGROUND")));
//        elementMap.put(new GuiSlider(this, -100, 25, 0, 110, settings.getDoubleSettingValue("BACKGROUND_OPACITY")), "BACKGROUND_OPACITY");
//        elementMap.put(new GuiIntSlider(this, 100, 200, 0, 32, settings.getIntSettingValue("RENDER_RANGE_MIN")), "RENDER_RANGE_MIN");

//        List<String> chatFormatting = Arrays.stream(EnumChatFormatting.values()).map(Enum::name).collect(Collectors.toList());
//        elementMap.put(new GuiSelectorSlider<String>(this, 100, 250, chatFormatting, settings.getStringSettingValue("BACKGROUND_COLOR")), "BACKGROUND_COLOR");

        map.put("HELLOWORLD", new GuiSliderBase(this, -50, 50, 150, 14, 0, 110, Collections.singletonList(50D)));

        map.put("RENDER_RANGE_MAX", new GuiSliderBase(this, -100, 75, -41, 72, Arrays.asList(-27D, 42D)));

        // TODO: (2.5 - 7.5): Arrays.asList(8D, 16D) bug? (both out of range therefore select <?> on click?)
        map.put("RENDER_RANGE_MIN", new GuiSliderBase(this, -100, 100, 0, 50,
                Arrays.asList(10D, 15D, 20D, 5D, 30D, 45D, 1000D)));

        return map;
    }
}
