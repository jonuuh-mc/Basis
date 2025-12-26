package io.jonuuh.basis.v000309.local;

import io.jonuuh.basis.v000309.lib.config.setting.Settings;
import io.jonuuh.basis.v000309.lib.gui.BaseGuiScreen;
import io.jonuuh.basis.v000309.lib.gui.element.container.FlexItem;
import io.jonuuh.basis.v000309.lib.gui.element.container.GuiBasicContainer;
import io.jonuuh.basis.v000309.lib.gui.element.container.GuiRootContainer;
import io.jonuuh.basis.v000309.lib.gui.element.container.behavior.FlexBehavior;
import io.jonuuh.basis.v000309.lib.gui.element.container.behavior.ScrollBehavior;
import io.jonuuh.basis.v000309.lib.gui.element.slider.GuiSingleSlider;
import io.jonuuh.basis.v000309.lib.gui.properties.FlexAlign;
import io.jonuuh.basis.v000309.lib.gui.properties.FlexDirection;
import io.jonuuh.basis.v000309.lib.gui.properties.FlexJustify;
import io.jonuuh.basis.v000309.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.v000309.lib.gui.properties.Spacing;
import io.jonuuh.basis.v000309.lib.util.Color;

import java.util.HashMap;
import java.util.Map;

public class GuiScreenImpl extends BaseGuiScreen
{
    protected final Settings settings;
    private GuiBasicContainer mainFlex;
    //    private GuiContainer currFocusedContainer;
    private GuiSingleSlider growItemSlider;
    private GuiSingleSlider shrinkItemSlider;
//    private GuiDropdown alignItemDropdown;

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
        colorMap.put(GuiColorType.ACCENT1, new Color("#bbe0e0e0"));
        colorMap.put(GuiColorType.ACCENT2, new Color("#bb484848"));
        colorMap.put(GuiColorType.BASE, new Color("#bb1450A0"));
        colorMap.put(GuiColorType.BACKGROUND, new Color("#bb242424"));

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        this.growItemSlider = new GuiSingleSlider.Builder("growSlider")
                .size(100, 10)
                .bounds(0, 10)
                .startValue(0)
                .integer(true)
//                .stateChangeBehavior(this::forceChangeFocusedContainerGrow)
                .build();

        this.shrinkItemSlider = new GuiSingleSlider.Builder("shrinkSlider")
                .size(100, 10)
                .bounds(0, 10)
                .startValue(1)
                .integer(true)
//                .stateChangeBehavior(this::forceChangeFocusedContainerShrink)
                .build();

//        this.alignItemDropdown = new GuiDropdown.Builder("alignDropdown")
//                .size(75, 15)
//                .prompt("START")
//                .options(Arrays.asList("START", "END", "CENTER", "STRETCH"))
////                .mouseDownBehavior(this::forceChangeFocusedContainerFlexAlign)
//                .build();

        GuiBasicContainer itemControlFlex = new GuiBasicContainer.Builder("itemControlFlex")
                .size(125, 50)
                .flexBehavior(new FlexBehavior.Builder()
                        .direction(FlexDirection.COLUMN)
                        .justify(FlexJustify.AROUND)
                        .align(FlexAlign.CENTER)
                        .items(new FlexItem(growItemSlider), new FlexItem(shrinkItemSlider)/*, new FlexItem(alignItemDropdown)*/))
                .build();

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        GuiBasicContainer controlFlex = new GuiBasicContainer.Builder("controlFlex")
                .size(400, 60)
                .flexBehavior(new FlexBehavior.Builder()
                                .justify(FlexJustify.AROUND)
                                .align(FlexAlign.CENTER)
                                .items(
//                                        new FlexItem(new GuiDropdown.Builder("directionDropdown")
//                                                .size(75, 15)
//                                                .prompt("COLUMN")
//                                                .options(Arrays.asList("ROW", "COLUMN", "ROW_REVERSE", "COLUMN_REVERSE"))
////                                .mouseDownBehavior(this::forceChangeMainFlexFlexDirection)
//                                                .build()),
//                                        new FlexItem(new GuiDropdown.Builder("justifyDropdown")
//                                                .size(75, 15)
//                                                .prompt("BETWEEN")
//                                                .options(Arrays.asList("START", "END", "CENTER", "BETWEEN", "AROUND", "EVENLY"))
////                                .mouseDownBehavior(this::forceChangeMainFlexFlexJustify)
//                                                .build()),
//                                        new FlexItem(new GuiDropdown.Builder("alignDropdown")
//                                                .size(75, 15)
//                                                .prompt("START")
//                                                .options(Arrays.asList("START", "END", "CENTER", "STRETCH"))
////                                .mouseDownBehavior(this::forceChangeMainFlexFlexAlign)
//                                                .build()),
                                        new FlexItem(itemControlFlex))
                ).build();

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
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
//        currFocusedContainer = container1;

        this.mainFlex = new GuiBasicContainer.Builder("mainFlex")
                .size(450, 270)
                .color(GuiColorType.BACKGROUND, new Color("4d4d4d", 0.75F))
                .padding(new Spacing(10, 10/*250*/, 10, 10))
                .flexBehavior(new FlexBehavior.Builder()
                        .direction(FlexDirection.COLUMN)
                        .justify(FlexJustify.BETWEEN)
                        .items(new FlexItem(container1), new FlexItem(container2), new FlexItem(container3), new FlexItem(container4)))
                .scrollBehavior(new ScrollBehavior.Builder().length(300))
                .build();

//        mainFlex.addItem(new FlexItem(new GuiCheckbox.Builder("checkbox").build()));
//        mainFlex.addItem(new FlexItem(new GuiDualSlider.Builder("dualSlider").bounds(0, 100).startValue(20).endPointerValue(80).build()));
//        mainFlex.addItem(new FlexItem(new GuiDualSlider.Builder("dualSlider").size(20, 200).bounds(0, 100).startValue(20).endPointerValue(80).vertical(true).build()));
//
//        mainFlex.addItem(new FlexItem(new GuiSingleSlider.Builder("singleSliderH").bounds(0, 100).startValue(60).build()));
//        mainFlex.addItem(new FlexItem(new GuiSingleSlider.Builder("singleSliderH").size(20, 200).bounds(0, 100).startValue(60).vertical(true).build()));
//        mainFlex.addItem(new FlexItem(new GuiSingleSlider.Builder("singleSliderH").size(10, 100).bounds(0, 20).startValue(60).vertical(true).build()));
//        mainFlex.addItem(new FlexItem(new GuiSingleSlider.Builder("singleSliderH").size(10, 100).bounds(300, 9000).startValue(60).vertical(true).build()));
//        mainFlex.addItem(new FlexItem(new GuiSingleSlider.Builder("singleSliderH").size(10, 100).bounds(-53, -50).startValue(60).vertical(true).build()));
//
//        mainFlex.addItem(new FlexItem(new GuiSingleSlider.Builder("singleSliderH").size(20, 200).bounds(0, 500).startValue(60).vertical(true).build()));
//        mainFlex.addItem(new FlexItem(new GuiSingleSlider.Builder("singleSliderH").size(20, 200).bounds(300, 9000).startValue(60).vertical(true).build()));
//        mainFlex.addItem(new FlexItem(new GuiSingleSlider.Builder("singleSliderH").size(20, 200).bounds(-53, -50).startValue(60).vertical(true).build()));
//
//        mainFlex.addItem(new FlexItem(new GuiSingleSlider.Builder("singleSliderH").size(20, 200).bounds(0, 20).startValue(60).vertical(true).integer(true).build()));
//        mainFlex.addItem(new FlexItem(new GuiSingleSlider.Builder("singleSliderH").size(20, 200).bounds(300, 320).startValue(60).vertical(true).integer(true).build()));
//        mainFlex.addItem(new FlexItem(new GuiSingleSlider.Builder("singleSliderH").size(20, 200).bounds(-53, -50).startValue(60).vertical(true).integer(true).build()));

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        GuiBasicContainer content = new GuiBasicContainer.Builder("content")
                .size(600, 400)
                .padding(new Spacing(10, 50, 0, 30))
                .margin(new Spacing(5))
                .flexBehavior(new FlexBehavior.Builder()
                        .direction(FlexDirection.COLUMN)
                        .justify(FlexJustify.AROUND)
                        .align(FlexAlign.CENTER)
                        .items(new FlexItem(mainFlex)/*.setGrow(1)*//*, new FlexItem(controlFlex)*/))
                .scrollBehavior(new ScrollBehavior.Builder().length(600))
                .build();

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        return new GuiRootContainer.Builder(this)
                .padding(new Spacing(20, 20, 20, 20))
                .colorMap(colorMap)
                .flexBehavior(new FlexBehavior.Builder().justify(FlexJustify.CENTER).align(FlexAlign.CENTER).item(new FlexItem(content)))
                .build();
    }

//    private void updateCurrFocusedContainer(GuiElement element)
//    {
//        currFocusedContainer.putColor(GuiColorType.BACKGROUND, new Color("a6a6a6", 0.5F));
//
//        currFocusedContainer = (GuiContainer) element;
//        currFocusedContainer.putColor(GuiColorType.BACKGROUND, new Color("#BF1450A0"));
//
//        FlexItem item = mainFlex.getFlexItem(element);
//
//        growItemSlider.setValue(item.getGrow());
//        shrinkItemSlider.setValue(item.getShrink());
//
//        if (item.getAlign() != null)
//        {
//            alignItemDropdown.setHeaderText(item.getAlign().toString());
//        }
//        else
//        {
//            alignItemDropdown.setHeaderText(FlexAlign.START.toString());
//        }
//    }
//
//    private void forceChangeFocusedContainerFlexAlign(GuiElement element)
//    {
//        mainFlex.getFlexItem(currFocusedContainer).setAlign(FlexAlign.valueOf(((GuiDropdown) element).getHeaderText()));
//        mainFlex.getFlexBehavior().updateItemsLayout();
//    }
//
//    private void forceChangeFocusedContainerGrow(GuiElement element)
//    {
//        System.out.println("changed grow: " + ((GuiSingleSlider) element).getValueInt());
//        mainFlex.getFlexItem(currFocusedContainer).setGrow(((GuiSingleSlider) element).getValueInt());
//        mainFlex.getFlexBehavior().updateItemsLayout();
//    }
//
//    private void forceChangeFocusedContainerShrink(GuiElement element)
//    {
//        System.out.println("changed shrink: " + ((GuiSingleSlider) element).getValueInt());
//        mainFlex.getFlexItem(currFocusedContainer).setShrink(((GuiSingleSlider) element).getValueInt());
//        mainFlex.getFlexBehavior().updateItemsLayout();
//    }
//
//    private void forceChangeMainFlexFlexDirection(GuiElement element)
//    {
//        mainFlex.setDirection(FlexDirection.valueOf(((GuiDropdown) element).getHeaderText()));
//        mainFlex.getFlexBehavior().updateItemsLayout();
//    }
//
//    private void forceChangeMainFlexFlexJustify(GuiElement element)
//    {
//        mainFlex.setJustifyContent(FlexJustify.valueOf(((GuiDropdown) element).getHeaderText()));
//        mainFlex.getFlexBehavior().updateItemsLayout();
//    }
//
//    private void forceChangeMainFlexFlexAlign(GuiElement element)
//    {
//        mainFlex.setAlignItems(FlexAlign.valueOf(((GuiDropdown) element).getHeaderText()));
//        mainFlex.getFlexBehavior().updateItemsLayout();
//    }
}
