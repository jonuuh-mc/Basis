package io.jonuuh.basis.lib.gui.screen;

import io.jonuuh.basis.lib.config.setting.Settings;
import io.jonuuh.basis.lib.gui.BaseGuiScreen;
import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.element.GuiLabel;
import io.jonuuh.basis.lib.gui.element.GuiTextField;
import io.jonuuh.basis.lib.gui.element.button.GuiLabeledButton;
import io.jonuuh.basis.lib.gui.element.container.FlexItem;
import io.jonuuh.basis.lib.gui.element.container.GuiBasicContainer;
import io.jonuuh.basis.lib.gui.element.container.GuiContainer;
import io.jonuuh.basis.lib.gui.element.container.GuiDropdown;
import io.jonuuh.basis.lib.gui.element.container.GuiRootContainer;
import io.jonuuh.basis.lib.gui.element.container.behavior.FlexBehavior;
import io.jonuuh.basis.lib.gui.element.container.behavior.ScrollBehavior;
import io.jonuuh.basis.lib.gui.element.toggles.GuiCheckbox;
import io.jonuuh.basis.lib.gui.properties.FlexAlign;
import io.jonuuh.basis.lib.gui.properties.FlexDirection;
import io.jonuuh.basis.lib.gui.properties.FlexJustify;
import io.jonuuh.basis.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.lib.gui.properties.Spacing;
import io.jonuuh.basis.lib.util.Color;

import java.util.Arrays;
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
                .cornerRadius(5)
                .flexBehavior(new FlexBehavior.Builder()
                        .direction(FlexDirection.COLUMN)
                        .justify(FlexJustify.BETWEEN)
                        .align(FlexAlign.CENTER)
                        .items(makeSettingContainer()/*, makeSettingContainer(), makeSettingContainer(), makeSettingContainer(),
                                makeSettingContainer(), makeSettingContainer(), makeSettingContainer(), makeSettingContainer()*/))
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
                .size(200, 30)
                .flexBehavior(new FlexBehavior.Builder()
                        .direction(FlexDirection.ROW)
                        .justify(FlexJustify.CENTER)
                        .align(FlexAlign.CENTER)
                        .item(new FlexItem(new GuiLabel.Builder("label").padding(new Spacing(5))/*.size(1, 1)*/
                                .text("Lorem ipsum something something something 123").build())/*.setGrow(1)*/)
                )
                .scrollBehavior(new ScrollBehavior.Builder().length(60))
                .build();

        GuiContainer textFieldContainer = new GuiBasicContainer.Builder("fieldContainer")
                .size(300, 40)
                .flexBehavior(new FlexBehavior.Builder()
                        .direction(FlexDirection.ROW)
                        .justify(FlexJustify.CENTER)
                        .align(FlexAlign.CENTER)
                        .items(new FlexItem(new GuiTextField.Builder("textField").size(170, 20).padding(new Spacing(5)).build()),
                                new FlexItem(new GuiDropdown.Builder("fieldDropdown").size(75, 10).options(Arrays.asList("1", "2", "3")).build()),
                                new FlexItem(new GuiLabeledButton.Builder("fieldButton").label("Add").mouseDownBehavior(this::addBehavior).build()))
                )
                .build();

        return new FlexItem(new GuiBasicContainer.Builder("settingContainer")
                .size(500, 50)
                .color(GuiColorType.BACKGROUND, new Color("#bb242424"))
                .padding(new Spacing(5, 5, 5, 5))
                .flexBehavior(new FlexBehavior.Builder()
                        .direction(FlexDirection.ROW)
                        .justify(FlexJustify.AROUND)
                        .align(FlexAlign.CENTER)
                        .items(new FlexItem(labelContainer)/*.setGrow(1)*/, new FlexItem(textFieldContainer)/*,
                                new FlexItem(new GuiBasicContainer.Builder("a").build())*/)
                ).build());
    }

    private void addBehavior(GuiElement element)
    {
        GuiContainer container = (GuiContainer) rootContainer.getElementByName("mainContent");

        String text = ((GuiTextField) rootContainer.getElementByName("textField")).getText();
        ((GuiTextField) rootContainer.getElementByName("textField")).clearText();

        GuiLabel label = new GuiLabel.Builder("label").padding(new Spacing(2)).text(text).build();

        GuiElement element1 = new GuiDropdown.Builder("aDropdown").size(75, 10).options(Arrays.asList("1", "2", "3")).build();

        addToContainer(element1, container);

        container.getFlexBehavior().updateItemsLayout();
    }

    private void addToContainer(GuiElement addedElement, GuiContainer container)
    {
        if (container.getFlexBehavior() != null)
        {
            container.getFlexBehavior().addItem(new FlexItem(addedElement), container.getChildren().size() - 2);
        }
        else
        {
            container.addChild(addedElement, container.getChildren().size() - 2);
        }

//        System.out.println(container.getChildren());
    }
}
