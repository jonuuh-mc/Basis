package io.jonuuh.basis.lib.gui.screen;

import io.jonuuh.basis.lib.config.setting.Settings;
import io.jonuuh.basis.lib.gui.BaseGuiScreen;
import io.jonuuh.basis.lib.gui.element.GuiLabel;
import io.jonuuh.basis.lib.gui.element.container.FlexItem;
import io.jonuuh.basis.lib.gui.element.container.GuiBasicContainer;
import io.jonuuh.basis.lib.gui.element.container.GuiContainer;
import io.jonuuh.basis.lib.gui.element.container.GuiRootContainer;
import io.jonuuh.basis.lib.gui.element.container.behavior.FlexBehavior;
import io.jonuuh.basis.lib.gui.element.container.behavior.ScrollBehavior;
import io.jonuuh.basis.lib.gui.element.slider.GuiSingleSlider;
import io.jonuuh.basis.lib.gui.element.toggles.GuiCheckbox;
import io.jonuuh.basis.lib.gui.properties.FlexAlign;
import io.jonuuh.basis.lib.gui.properties.FlexDirection;
import io.jonuuh.basis.lib.gui.properties.FlexJustify;
import io.jonuuh.basis.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.lib.gui.properties.Spacing;
import io.jonuuh.basis.lib.util.Color;
import io.jonuuh.basis.lib.util.MathUtils;

import java.util.HashMap;
import java.util.Map;

public class MainGuiScreen extends BaseGuiScreen
{
    protected final Settings settings;

    public MainGuiScreen(Settings settings)
    {
        this.settings = settings;
        this.rootContainer = initRootContainer();
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();

        if (settings != null)
        {
            settings.saveCurrentValues();
        }
    }

    @Override
    protected GuiRootContainer initRootContainer()
    {
        Map<GuiColorType, Color> colorMap = new HashMap<>();
        colorMap.put(GuiColorType.ACCENT1, new Color("#bbe0e0e0"));
        colorMap.put(GuiColorType.ACCENT2, new Color("#bb484848"));
        colorMap.put(GuiColorType.BASE, new Color("#bb1450A0"));
        colorMap.put(GuiColorType.BACKGROUND, new Color("#bb121212"));

        GuiBasicContainer menuBar = new GuiBasicContainer.Builder("menuBar")
                .size(575, 25)
                .color(GuiColorType.BACKGROUND, new Color("4d4d4d", 0.75F))
                .padding(new Spacing(50, 20, 0, 0))
                .flexBehavior(new FlexBehavior.Builder()
                        .direction(FlexDirection.ROW)
                        .justify(FlexJustify.END)
                        .align(FlexAlign.CENTER)
                        .items(new FlexItem(new GuiCheckbox.Builder("checkbox").padding(new Spacing(2)).build()),
                                new FlexItem(new GuiCheckbox.Builder("checkbox").padding(new Spacing(2)).build()),
                                new FlexItem(new GuiCheckbox.Builder("checkbox").padding(new Spacing(2)).build()))
                ).build();

        GuiBasicContainer mainContent = new GuiBasicContainer.Builder("mainContent")
                .size(575, 330)
                .color(GuiColorType.BACKGROUND, new Color("4d4d4d", 0.75F))
                .padding(new Spacing(10, 10, 10, 10))
                .flexBehavior(new FlexBehavior.Builder()
                        .direction(FlexDirection.COLUMN)
                        .justify(FlexJustify.BETWEEN)
                        .align(FlexAlign.CENTER)
                        .items(makeSettingContainer(), makeSettingContainer(), makeSettingContainer(), makeSettingContainer(),
                                makeSettingContainer(), makeSettingContainer(), makeSettingContainer(), makeSettingContainer()))
                .scrollBehavior(new ScrollBehavior.Builder().length(500))
                .build();


        GuiBasicContainer content = new GuiBasicContainer.Builder("content")
                .size(600, 400)
                .padding(new Spacing(10, 10, 10, 10))
                .margin(new Spacing(5))
                .flexBehavior(new FlexBehavior.Builder()
                        .direction(FlexDirection.COLUMN)
                        .justify(FlexJustify.AROUND)
                        .align(FlexAlign.CENTER)
                        .items(new FlexItem(menuBar).setShrink(0), new FlexItem(mainContent)/*.setGrow(1)*/))
//                .scrollBehavior(new ScrollBehavior.Builder().length(600))
                .build();

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        return new GuiRootContainer.Builder(this)
                .padding(new Spacing(20, 20, 20, 20))
                .colorMap(colorMap)
                .flexBehavior(new FlexBehavior.Builder().justify(FlexJustify.CENTER).align(FlexAlign.CENTER).item(new FlexItem(content)))
                .build();
    }

    private FlexItem makeSettingContainer()
    {
        GuiContainer labelContainer = new GuiBasicContainer.Builder("labelContainer")
                .size(100, 40)
                .flexBehavior(new FlexBehavior.Builder()
                        .direction(FlexDirection.ROW)
                        .justify(FlexJustify.CENTER)
                        .align(FlexAlign.CENTER)
                        .item(new FlexItem(new GuiLabel.Builder("label")/*.size(1, 1)*/
                                .text("Lorem ipsum something something something 123").build())/*.setGrow(1)*/))
                .build();

        return new FlexItem(new GuiBasicContainer.Builder("settingContainer")
                .size(500, 50)
                .color(GuiColorType.BACKGROUND, new Color("#bb242424"))
                .padding(new Spacing(5, 5, 5, 5))
                .flexBehavior(new FlexBehavior.Builder()
                        .direction(FlexDirection.ROW)
                        .justify(FlexJustify.BETWEEN)
                        .align(FlexAlign.CENTER)
                        .items(new FlexItem(labelContainer).setGrow(1),
                                new FlexItem(new GuiSingleSlider.Builder("slider").size(170, 20).bounds(0, 100).startValue(MathUtils.randomIntInclusiveRange(0, 100)).build()))
                ).build());
    }
}
