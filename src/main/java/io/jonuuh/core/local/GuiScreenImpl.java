package io.jonuuh.core.local;

import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.gui.AbstractGuiScreen;
import io.jonuuh.core.lib.gui.element.GuiCheckbox;
import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.element.container.GuiBasicContainer;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import io.jonuuh.core.lib.gui.element.container.GuiDropdown;
import io.jonuuh.core.lib.gui.element.container.GuiRootContainer;
import io.jonuuh.core.lib.gui.element.container.flex.FlexItem;
import io.jonuuh.core.lib.gui.element.container.flex.GuiFlexContainer;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexAlign;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexDirection;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexJustify;
import io.jonuuh.core.lib.gui.element.slider.GuiDualSlider;
import io.jonuuh.core.lib.gui.element.slider.GuiSingleSlider;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.gui.properties.Spacing;
import io.jonuuh.core.lib.util.Color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GuiScreenImpl extends AbstractGuiScreen
{
    protected final Settings settings;
    private GuiFlexContainer mainFlex;
    private GuiContainer currFocusedContainer;
    private GuiSingleSlider growItemSlider;
    private GuiSingleSlider shrinkItemSlider;
    private GuiDropdown alignItemDropdown;

    public GuiScreenImpl(Settings settings)
    {
        this.settings = settings;
        this.rootContainer = initRootContainer();
        rootContainer.getNestedChildren().forEach(System.out::println); // TODO: debug
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

    protected GuiRootContainer initRootContainer()
    {
        Map<GuiColorType, Color> colorMap = new HashMap<>();
        colorMap.put(GuiColorType.ACCENT1, new Color());
        colorMap.put(GuiColorType.ACCENT2, new Color("#BF484848"));
        colorMap.put(GuiColorType.BASE, new Color("#321450A0"));
        colorMap.put(GuiColorType.BACKGROUND, new Color("#BF242424"));

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        GuiFlexContainer itemControlFlex = new GuiFlexContainer.Builder("itemControlFlex")
                .size(125, 50)
                .direction(FlexDirection.COLUMN)
                .justify(FlexJustify.AROUND)
                .align(FlexAlign.CENTER)
                .build();

        this.growItemSlider = new GuiSingleSlider.Builder("growSlider")
                .size(100, 10)
                .bounds(0, 10)
                .startValue(0)
                .integer(true)
                .build();

        this.shrinkItemSlider = new GuiSingleSlider.Builder("shrinkSlider")
                .size(100, 10)
                .bounds(0, 10)
                .startValue(1)
                .integer(true)
                .build();

        this.alignItemDropdown = new GuiDropdown.Builder("alignDropdown")
                .size(75, 15)
                .prompt("START")
                .options(Arrays.asList("START", "END", "CENTER", "STRETCH"))
                .build();

//        growItemSlider.assignPostEventBehavior(GuiEventType.CUSTOM, this::forceChangeFocusedContainerGrow);
//        shrinkItemSlider.assignPostEventBehavior(GuiEventType.CUSTOM, this::forceChangeFocusedContainerShrink);
//        alignItemDropdown.assignPostEventBehavior(GuiEventType.CUSTOM, this::forceChangeFocusedContainerFlexAlign);
        itemControlFlex.addItems(new FlexItem(growItemSlider), new FlexItem(shrinkItemSlider), new FlexItem(alignItemDropdown));

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //

        GuiFlexContainer controlFlex = new GuiFlexContainer.Builder("controlFlex")
                .size(400, 60)
                .justify(FlexJustify.AROUND)
                .align(FlexAlign.CENTER)
                .items(
                        new FlexItem(new GuiDropdown.Builder("directionDropdown")
                                .size(75, 15)
                                .prompt("ROW")
                                .options(Arrays.asList("ROW", "COLUMN", "ROW_REVERSE", "COLUMN_REVERSE"))
                                .build()),
                        new FlexItem(new GuiDropdown.Builder("justifyDropdown")
                                .size(75, 15)
                                .prompt("START")
                                .options(Arrays.asList("START", "END", "CENTER", "BETWEEN", "AROUND", "EVENLY"))
                                .build()),
                        new FlexItem(new GuiDropdown.Builder("alignDropdown")
                                .size(75, 15)
                                .prompt("START")
                                .options(Arrays.asList("START", "END", "CENTER", "STRETCH"))
                                .build())
                )
                .build();


//        directionDropdown.assignPostEventBehavior(GuiEventType.CUSTOM, this::forceChangeMainFlexFlexDirection);
//        justifyDropdown.assignPostEventBehavior(GuiEventType.CUSTOM, this::forceChangeMainFlexFlexJustify);
//        alignDropdown.assignPostEventBehavior(GuiEventType.CUSTOM, this::forceChangeMainFlexFlexAlign);
        controlFlex.addItems(new FlexItem(itemControlFlex));

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        this.mainFlex = new GuiFlexContainer.Builder("mainFlex")
                .size(450, 270)
                .color(GuiColorType.BACKGROUND, new Color("4d4d4d", 0.75F))
                .padding(new Spacing(10, 10/*250*/, 10, 10))
                .build();

        GuiBasicContainer container1 = new GuiBasicContainer.Builder("container1")
                .size(125, 40)
                .color(GuiColorType.BACKGROUND, colorMap.get(GuiColorType.BASE))
                .build();
//        container1.assignPostEventBehavior(GuiEventType.MOUSE_DOWN, this::updateCurrFocusedContainer);

        GuiBasicContainer container2 = new GuiBasicContainer.Builder("container2")
                .size(50, 100)
                .color(GuiColorType.BACKGROUND, new Color("a6a6a6", 0.5F))
                .build();
//        container2.assignPostEventBehavior(GuiEventType.MOUSE_DOWN, this::updateCurrFocusedContainer);

        GuiBasicContainer container3 = new GuiBasicContainer.Builder("container3")
                .size(100, 30)
                .color(GuiColorType.BACKGROUND, new Color("a6a6a6", 0.5F))
                .build();
//        container3.assignPostEventBehavior(GuiEventType.MOUSE_DOWN, this::updateCurrFocusedContainer);

        GuiBasicContainer container4 = new GuiBasicContainer.Builder("container4")
                .size(75, 50)
                .color(GuiColorType.BACKGROUND, new Color("a6a6a6", 0.5F))
                .build();
//        container4.assignPostEventBehavior(GuiEventType.MOUSE_DOWN, this::updateCurrFocusedContainer);

        currFocusedContainer = container1;
//        mainFlex.addItems(new FlexItem(container1), new FlexItem(container2), new FlexItem(container3), new FlexItem(container4));
        mainFlex.addItem(new FlexItem(new GuiCheckbox.Builder("checkbox").build()));
        mainFlex.addItem(new FlexItem(new GuiDualSlider.Builder("dualSlider").bounds(0, 100).startValue(20).startValueRight(80).build()));
        mainFlex.addItem(new FlexItem(new GuiSingleSlider.Builder("singleSliderH").bounds(0, 100).startValue(60).build()));

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        GuiFlexContainer mainContent = new GuiFlexContainer.Builder("mainContent")
                .size(600, 400)
                /*.padding(new Spacing(10, 20, 0, 70))*/
                .direction(FlexDirection.COLUMN)
                .justify(FlexJustify.AROUND)
                .align(FlexAlign.CENTER)
                .build();

        mainContent.addItems(new FlexItem(mainFlex)/*.setGrow(1)*/, new FlexItem(controlFlex));

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        GuiRootContainer rootContainer = new GuiRootContainer.Builder(this)
                .colorMap(colorMap)
                .build();

        rootContainer.addItem(new FlexItem(mainContent));

        return rootContainer;
    }

    private void updateCurrFocusedContainer(GuiElement element)
    {
        currFocusedContainer.putColor(GuiColorType.BACKGROUND, new Color("a6a6a6", 0.5F));

        currFocusedContainer = (GuiContainer) element;
        currFocusedContainer.putColor(GuiColorType.BACKGROUND, new Color("#BF1450A0"));

        FlexItem item = mainFlex.getFlexItem(element);

        growItemSlider.setValue(item.getGrow());
        shrinkItemSlider.setValue(item.getShrink());

        if (item.getAlign() != null)
        {
            alignItemDropdown.setHeaderText(item.getAlign().toString());
        }
        else
        {
            alignItemDropdown.setHeaderText(FlexAlign.START.toString());
        }
    }

    private void forceChangeFocusedContainerFlexAlign(GuiElement element)
    {
        mainFlex.getFlexItem(currFocusedContainer).setAlign(FlexAlign.valueOf(((GuiDropdown) element).getHeaderText()));
        mainFlex.getFlexBehavior().updateItemsLayout();
    }

    private void forceChangeFocusedContainerGrow(GuiElement element)
    {
        mainFlex.getFlexItem(currFocusedContainer).setGrow(((GuiSingleSlider) element).getValueInt());
        mainFlex.getFlexBehavior().updateItemsLayout();
    }

    private void forceChangeFocusedContainerShrink(GuiElement element)
    {
        mainFlex.getFlexItem(currFocusedContainer).setShrink(((GuiSingleSlider) element).getValueInt());
        mainFlex.getFlexBehavior().updateItemsLayout();
    }

    private void forceChangeMainFlexFlexDirection(GuiElement element)
    {
        mainFlex.setDirection(FlexDirection.valueOf(((GuiDropdown) element).getHeaderText()));
        mainFlex.getFlexBehavior().updateItemsLayout();
    }

    private void forceChangeMainFlexFlexJustify(GuiElement element)
    {
        mainFlex.setJustifyContent(FlexJustify.valueOf(((GuiDropdown) element).getHeaderText()));
        mainFlex.getFlexBehavior().updateItemsLayout();
    }

    private void forceChangeMainFlexFlexAlign(GuiElement element)
    {
        mainFlex.setAlignItems(FlexAlign.valueOf(((GuiDropdown) element).getHeaderText()));
        mainFlex.getFlexBehavior().updateItemsLayout();
    }
}
