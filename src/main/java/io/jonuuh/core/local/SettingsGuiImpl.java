package io.jonuuh.core.local;

import io.jonuuh.core.module.config.gui.AbstractSettingsGui;
import io.jonuuh.core.module.config.gui.elements.interactable.sliders.GuiIntSlider;
import io.jonuuh.core.module.config.gui.elements.interactable.sliders.GuiSlider;
import io.jonuuh.core.module.config.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.core.module.config.gui.elements.interactable.GuiSwitch;
import io.jonuuh.core.module.config.Config;

import java.util.HashMap;
import java.util.Map;

public class SettingsGuiImpl extends AbstractSettingsGui
{
    public SettingsGuiImpl(String configCategory)
    {
        super(Config.getInstance().getSettings(configCategory));
    }

    @Override
    protected Map<String, GuiInteractableElement> initElementMap()
    {
        Map<String, GuiInteractableElement> map = new HashMap<>();

        map.put("DRAW_BACKGROUND", new GuiSwitch(this, -100, 0, "Whether to draw background", settings.getBoolSettingValue("DRAW_BACKGROUND")));

//        List<String> chatFormatting = Arrays.stream(EnumChatFormatting.values()).map(Enum::name).collect(Collectors.toList());
//        elementMap.put(new GuiSelectorSlider<String>(this, 100, 250, chatFormatting, settings.getStringSettingValue("BACKGROUND_COLOR")), "BACKGROUND_COLOR");

        map.put("BACKGROUND_OPACITY", new GuiSlider(this, -100, 25, 200, 12, 0, 100, settings.getDoubleSettingValue("BACKGROUND_OPACITY")));

        map.put("RENDER_RANGE", new GuiIntSlider(this, -100, 50, 200, 12, 0, 20/*mc.gameSettings.renderDistanceChunks * 16*/, settings.getIntListSettingValue("RENDER_RANGE")));

//        int[] ints = settings.getIntListSettingValue("RENDER_RANGE");
//        map.put("RENDER_RANGE", new GuiDualSlider(this, -50, 50, 0, 100, ints[0], ints[1]));
//
////        map.put("HELLOWORLD", new GuiSingleSlider(this, -50, 50, 100, 9, 0, 110, 50));
//
//        map.put("RENDER_RANGE_MAX", new GuiDualSlider(this, -100, 75, -41, 72, -27, 42));

        // TODO: (2.5 - 7.5): Arrays.asList(8D, 16D) bug? (both out of range therefore select <?> on click?)
        map.put("FAT_SLIDER", new GuiSlider(this, -100, 100, 0, 50,
                new double[]{10D, 15D, 20D, 5D, 30D, 45D, 1000D}));

        return map;
    }
}
