package io.jonuuh.core.local;

import com.google.common.collect.ImmutableMap;
import io.jonuuh.core.lib.config.gui.AbstractGuiScreen;
import io.jonuuh.core.lib.config.gui.GuiColorType;
import io.jonuuh.core.lib.config.gui.elements.container.GuiBaseContainer;
import io.jonuuh.core.lib.config.gui.elements.container.GuiContainer;
import io.jonuuh.core.lib.config.gui.elements.GuiElement;
import io.jonuuh.core.lib.config.gui.elements.GuiLabel;
import io.jonuuh.core.lib.config.gui.elements.GuiButton;
import io.jonuuh.core.lib.config.gui.elements.container.GuiFlexContainer;
import io.jonuuh.core.lib.config.gui.elements.sliders.GuiDualSlider;
import io.jonuuh.core.lib.config.gui.elements.GuiSwitch;
import io.jonuuh.core.lib.config.gui.elements.sliders.GuiSingleSlider;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Map;

public class SettingsGuiScreen extends AbstractGuiScreen
{
    protected final Settings settings;

    public SettingsGuiScreen(Settings settings)
    {
//        super();
        this.settings = settings;

        this.rootContainer = initRootContainer();
        rootContainer.getNestedChildren().forEach(System.out::println);
    }

    private boolean onMouseDownSpecial(GuiElement element)
    {
        System.out.println("[custom behavior for mouseDown] element: " + element);
        return false;
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        settings.save();
        //        System.out.println(settings);
    }

    protected GuiContainer initRootContainer()
    {
        System.out.println("settings: " + settings);

        // TODO: use this to pass setting into SettingElement constructors? `Setting<?> setting = settings.get(settingName);`
        //  if passing each setting into elements instead of later associating settings (bad), is there any need for an element to have a name anymore?
        //  maybe keep elementName(s) in case they are needed for something (forgot), but for a settingElement assign a name given the setting's name?
        //  this all implies a settingElement can only work in conjunction with a setting which should be terrible design anyway..

        //  TODO: option to pass in a setting to an element in place of a default value?

        // TODO: should this really be immutable?
        Map<GuiColorType, Color> colorMap = ImmutableMap.of(
                GuiColorType.ACCENT1, new Color(),
                GuiColorType.ACCENT2, new Color("#484848"),
                GuiColorType.BASE, new Color("#1450A0"),
                GuiColorType.BACKGROUND, new Color("#242424").setA(0.8F));

        // TODO: don't all these given widths and heights for elements depend on the this initial scaledRes?
        //  not literally but relatively, they were chosen with respect to this initial scaledRes, right?
//        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
//        int w = sr.getScaledWidth() / 2;
//        int h = sr.getScaledHeight() / 2;

        GuiFlexContainer rootContainer = new GuiFlexContainer(null, "ROOT",
                50, 50, 400, 300, 1, 3, colorMap);
        rootContainer.setVertical(true);


        GuiFlexContainer flexContainer = new GuiFlexContainer(rootContainer, "LABEL_FLEX", 50, 10, 300, 40, 2, 2);

        new GuiLabel(flexContainer, "SWITCH_LABEL", 5, 5, 80, 22, "LabelLabelLabel");


        GuiFlexContainer container = new GuiFlexContainer(flexContainer, "SWITCH_FLEX", 150, 5, 60, 30, 4, 4);
        container.setVertical(true);

        new GuiSwitch(container, "DRAW_BACKGROUND", 20, 5, 20, 10, "Whether to draw background",
                settings.get("DRAW_BACKGROUND", BoolSetting.class).getValue());

        new GuiSwitch(container, "SWITCH", 10, 17, 20, 10, "", false);

//        aSwitch.assignCustomEventBehavior(GuiEventType.MOUSE_DOWN, e -> onMouseDownSpecial(e));

//        System.out.println(new Color("#1450A0").adjustBrightness(50));

//        new GuiDoubleSlider(rootContainer, "BACKGROUND_OPACITY", 0, 25, 200, 12, 0, 100,
//                settings.get("BACKGROUND_OPACITY", DoubleSetting.class).getValue(), false);
//
//        new GuiIntSlider(rootContainer, "RENDER_RANGE", 0, 50, 200, 12, 0, 20,
//                ArrayUtils.toObject(settings.get("RENDER_RANGE", IntArrSetting.class).getValue()), false);

//        GuiScrollContainer containerLv2_1 = new GuiScrollContainer(container, "CONTAINER2_1", 240, 10, 100, 100,
//                colorMap, 0, 0, "tooltip2", 200);
//        new GuiCheckbox(containerLv2_1, "TEST_CHECKBOX", 10, 20);

//        new GuiDoubleSlider(container, "FAT_SLIDER", 0, 100, 0, 50,
//                ArrayUtils.toObject(settings.get("FAT_SLIDER", DoubleArrSetting.class).getValue())); // TODO: fix toObject design?

        GuiFlexContainer sliderContainer = new GuiFlexContainer(rootContainer, "SLIDER_FLEX", 20, 60, 300, 65, 2, 2);
//        sliderContainer.setVertical(true);

        new GuiDualSlider(sliderContainer, "RENDER_RANGE", 5, 5, 100, 16, 0, 50, 20, 30, false);

        new GuiSingleSlider(sliderContainer, "BACKGROUND_OPACITY", 5, 40, 100, 16, 0, 100, 20, false);

        // // //
        GuiFlexContainer container2 = new GuiFlexContainer(rootContainer, "BUTTON_FLEX", 75, 120, 100, 75,
                0, 0);
        container2.setVertical(true);

        new GuiSwitch(container2, "TEST", 5, 5, 32, 10, "", false);

        new GuiButton(container2, "BUTTON_TEST", 40, 25, 30, 40, this::onMouseDownSpecial);
        // //

//        GuiContainer container3 = new GuiBaseContainer(container2, "CONTAINER3", 10, 30, 150, 75,
//                colorMap, 4, 16, "tooltip2");
//
//        new GuiIntSlider(container3, "TEST2", 10, 20, 50, 8, 0, 100,
//                settings.get("TEST2", IntSetting.class).getValue(), false);
//
//        container2.putChild("NESTED_CONTAINER2", container3);
//        container3.putChild("TEST3", new GuiSwitch(container3, "test", 5, 5, true));
//        container2.putChild("NESTED_CONTAINER2", container3);
//        elementMap.put("NESTED_CONTAINER", container2);

        return rootContainer;
    }
}
