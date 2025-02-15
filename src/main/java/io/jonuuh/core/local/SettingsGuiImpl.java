package io.jonuuh.core.local;

import com.google.common.collect.ImmutableMap;
import io.jonuuh.core.lib.config.gui.AbstractSettingsGui;
import io.jonuuh.core.lib.config.gui.elements.GuiContainer;
import io.jonuuh.core.lib.config.gui.elements.GuiScrollContainer;
import io.jonuuh.core.lib.config.gui.elements.interactable.sliders.old.GuiDoubleSlider;
import io.jonuuh.core.lib.config.gui.elements.interactable.sliders.GuiDualSlider;
import io.jonuuh.core.lib.config.gui.elements.interactable.sliders.old.GuiIntSlider;
import io.jonuuh.core.lib.config.gui.elements.interactable.GuiSwitch;
import io.jonuuh.core.lib.config.Config;
import io.jonuuh.core.lib.config.gui.elements.interactable.sliders.GuiSingleSlider;
import io.jonuuh.core.lib.config.setting.types.array.IntArrSetting;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.config.setting.types.single.DoubleSetting;
import io.jonuuh.core.lib.util.Color;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;

public class SettingsGuiImpl extends AbstractSettingsGui
{
    public SettingsGuiImpl(String configCategory)
    {
        super(Config.INSTANCE.getSettings(configCategory)); // TODO: should pass Config thru constructor to ensure non-null?
    }

    @Override
    protected GuiContainer initRootContainer()
    {
        Map<String, Color> colorMap =
                ImmutableMap.of("ACCENT", new Color(), "DISABLED", new Color("#484848"));

        GuiScrollContainer container = new GuiScrollContainer(null, "CONTAINER", 100, 100, 400, 200,
                colorMap, 0, 0, "tooltip", 400);
//        container.putChildren(initElements(container));

        new GuiSwitch(container, "DRAW_BACKGROUND", 0, 0, "Whether to draw background",
                settings.get("DRAW_BACKGROUND", BoolSetting.class).getValue());

        new GuiDoubleSlider(container, "BACKGROUND_OPACITY", 0, 25, 200, 12, 0, 100,
                settings.get("BACKGROUND_OPACITY", DoubleSetting.class).getValue(), false);

        new GuiIntSlider(container, "RENDER_RANGE", 0, 50, 200, 12, 0, 20,
                ArrayUtils.toObject(settings.get("RENDER_RANGE", IntArrSetting.class).getValue()), false);


//        GuiScrollContainer containerLv2_1 = new GuiScrollContainer(container, "CONTAINER2_1", 240, 10, 100, 100,
//                colorMap, 0, 0, "tooltip2", 200);
//        new GuiCheckbox(containerLv2_1, "TEST_CHECKBOX", 10, 20);

//        new GuiDoubleSlider(container, "FAT_SLIDER", 0, 100, 0, 50,
//                ArrayUtils.toObject(settings.get("FAT_SLIDER", DoubleArrSetting.class).getValue())); // TODO: fix toObject design?


        new GuiDualSlider(container, "FAT_SLIDER", 0, 100, 0, 50, 20, 30);
//        new GuiScrollSlider(container, "SCROLL_SLIDER", 10, 300, 0, 100, 20);
        new GuiSingleSlider(container, "SINGLE_SLIDER", 10, 400 - 16, 0, 100, 20);

        // // //

        GuiContainer container2 = new GuiScrollContainer(container, "CONTAINER2", 50, 140, 200, 150,
                colorMap, 0, 0, "tooltip2", 200);

        new GuiSwitch(container2, "TEST", 0, 0, false);

        // // //

//        GuiContainer container3 = new GuiScrollContainer(container2, "CONTAINER3", 10, 30, 150, 75,
//                colorMap, 4, 16, "tooltip2", 100);
//
//        new GuiIntSlider(container3, "TEST2", 10, 20, 50, 8, 0, 100,
//                settings.get("TEST2", IntSetting.class).getValue(), false);
//
//        container2.putChild("NESTED_CONTAINER2", container3);


//        container3.putChild("TEST3", new GuiSwitch(container3, 5, 5, true));

//        container2.putChild("NESTED_CONTAINER2", container3);

//        elementMap.put("NESTED_CONTAINER", container2);

        return container;
    }

//    private Map<String, GuiElement> initElements(GuiContainer parent)
//    {
//        Map<String, GuiElement> elementMap = new HashMap<>();
//
//        elementMap.put("DRAW_BACKGROUND", new GuiSwitch(parent, 0, 0, "Whether to draw background", settings.getBoolSetting("DRAW_BACKGROUND").getValue()));
//
//        elementMap.put("BACKGROUND_OPACITY", new GuiDoubleSlider(parent, 0, 25, 200, 12, 0, 100,
//                settings.getDoubleSetting("BACKGROUND_OPACITY").getValue(), false));
//
//        elementMap.put("RENDER_RANGE", new GuiIntSlider(parent, 0, 50, 200, 12, 0, 20,
//                ArrayUtils.toObject(settings.getIntListSetting("RENDER_RANGE").getValue()), false));
//
////        int[] ints = settings.getIntListSettingValue("RENDER_RANGE");
////        map.put("RENDER_RANGE", new GuiDualSlider(this, -50, 50, 0, 100, ints[0], ints[1]));
////        map.put("HELLOWORLD", new GuiSingleSlider(this, -50, 50, 100, 9, 0, 110, 50));
////        map.put("RENDER_RANGE_MAX", new GuiDualSlider(this, -100, 75, -41, 72, -27, 42));
//
//        // TODO: (2.5 - 7.5): Arrays.asList(8D, 16D) bug? (both out of range therefore select <?> on click?)
//        elementMap.put("FAT_SLIDER", new GuiDoubleSlider(parent, 0, 100, 0, 50,
//                ArrayUtils.toObject(settings.getDoubleListSetting("FAT_SLIDER").getValue())));
//
////        elementMap.put("BACKGROUND_COLOR", new GuiTextField(parent, 0, 200, settings.getStringSetting("BACKGROUND_COLOR").getValue()));
//
////        GuiContainer container2 = new GuiScrollContainer(parent,50, 120, 200, 150,
////                ImmutableMap.of("ACCENT", new Color(), "DISABLED", new Color("#484848")), 4, 16, "tooltip2", 200);
////        container2.putChild("TEST", new GuiSwitch(container2, 0, 0, false));
////
////        GuiContainer container3 = new GuiScrollContainer(container2,10, 30, 150, 75,
////                ImmutableMap.of("ACCENT", new Color(), "DISABLED", new Color("#484848")), 4, 16, "tooltip2", 200);
////
////        container2.putChild("NESTED_CONTAINER2", container3);
////
////
////        container3.putChild("TEST2", new GuiIntSlider(container3, 0, 0, 50, 8, 0, 100, 33, false));
//////        container3.putChild("TEST3", new GuiSwitch(container3, 5, 5, true));
////
//////        container2.putChild("NESTED_CONTAINER2", container3);
////
////        elementMap.put("NESTED_CONTAINER", container2);
//
//        return elementMap;
//    }
}
