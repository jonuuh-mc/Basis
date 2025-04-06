package io.jonuuh.core.local;

import com.google.common.collect.ImmutableMap;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.gui.AbstractGuiScreen;
import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.element.GuiLabel;
import io.jonuuh.core.lib.gui.element.container.GuiBaseContainer;
import io.jonuuh.core.lib.gui.element.container.GuiScrollContainer;
import io.jonuuh.core.lib.gui.element.container.GuiWindow;
import io.jonuuh.core.lib.gui.element.container.flex.FlexAlign;
import io.jonuuh.core.lib.gui.element.container.flex.FlexDirection;
import io.jonuuh.core.lib.gui.element.container.flex.FlexJustify;
import io.jonuuh.core.lib.gui.element.container.flex.GuiFlexContainer;
import io.jonuuh.core.lib.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

import java.util.Map;

public class GuiScreenImpl extends AbstractGuiScreen
{
    protected final Settings settings;
    private GuiFlexContainer mainFlex;

    public GuiScreenImpl(Settings settings)
    {
        this.settings = settings;
        this.rootContainer = initRootContainer();
        rootContainer.getNestedChildren().forEach(System.out::println);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
//        settings.saveCurrentValues();
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
//        System.out.println(mc.gameSettings.guiScale);
    }

    @Override
    public void initGui()
    {
        super.initGui();
//        System.out.println(rootContainer.getGreatestZLevelHovered(rootContainer));
    }

    //    @SubscribeEvent
//    public void onSettingChange(SettingEvent<?> event)
//    {
//        if (event.setting == settings.get(LocalSettingKey.FLEX_DIRECTION_MAIN))
//        {
//            switch (mainFlex.getDirection())
//            {
//                case ROW:
//                    mainFlex.setDirection(FlexDirection.COLUMN);
//                    break;
//                case ROW_REVERSE:
//                    mainFlex.setDirection(FlexDirection.COLUMN_REVERSE);
//                    break;
//                case COLUMN:
//                    mainFlex.setDirection(FlexDirection.ROW);
//                    break;
//                case COLUMN_REVERSE:
//                    mainFlex.setDirection(FlexDirection.ROW_REVERSE);
//                    break;
//            }
//        }
//        else if (event.setting == settings.get(LocalSettingKey.FLEX_DIRECTION_REVERSE))
//        {
//            switch (mainFlex.getDirection())
//            {
//                case ROW:
//                    mainFlex.setDirection(FlexDirection.ROW_REVERSE);
//                    break;
//                case ROW_REVERSE:
//                    mainFlex.setDirection(FlexDirection.ROW);
//                    break;
//                case COLUMN:
//                    mainFlex.setDirection(FlexDirection.COLUMN_REVERSE);
//                    break;
//                case COLUMN_REVERSE:
//                    mainFlex.setDirection(FlexDirection.COLUMN);
//                    break;
//            }
//        }
//        else if (event.setting == settings.get(LocalSettingKey.FLEX_BASIS))
//        {
//
//        }
//        else
//        {
//            return;
//        }
//
////        System.out.println(mainFlex.getAlignItems());
//        forceUpdate(mainFlex);
//    }

    private GuiWindow newRootContainerTest()
    {
        Map<GuiColorType, Color> colorMap = ImmutableMap.of(
                GuiColorType.ACCENT1, new Color(),
                GuiColorType.ACCENT2, new Color("#BF484848"),
                GuiColorType.BASE, new Color("#BF1450A0"),
                GuiColorType.BACKGROUND, new Color("#BF242424"));

        GuiWindow rootContainer = new GuiWindow(this, new ScaledResolution(Minecraft.getMinecraft()), 0, 0, colorMap);
//        rootContainer.setDirection(FlexDirection.COLUMN);

//        GuiFlexContainer mainContent = new GuiFlexContainer("mainContent", 200, 50, 600, 400, 4, 4);


        GuiFlexContainer mainFlex = new GuiFlexContainer("mainFlex", 0, 0, 500, 250, 4, 4);
        this.mainFlex = mainFlex;
        mainFlex.putColor(GuiColorType.BACKGROUND, new Color("4d4d4d", 0.75F));
//        mainFlex.setAlignItems(FlexAlign.START);
//        mainFlex.setJustifyContent(FlexJustify.CENTER);

        GuiBaseContainer container1 = new GuiBaseContainer("container1", 0, 0, 200, 75, 4, 4);
        container1.putColor(GuiColorType.BACKGROUND, new Color(EnumChatFormatting.GOLD));
        GuiBaseContainer container2 = new GuiBaseContainer("container2", 0, 0, 200, 75, 4, 4);
        container2.putColor(GuiColorType.BACKGROUND, new Color("a6a6a6", 0.5F));
//        GuiBaseContainer container3 = new GuiBaseContainer("container3", 0, 0, 200, 75, 4, 4);
//        container3.putColor(GuiColorType.BACKGROUND, new Color("a6a6a6", 0.5F));
//        GuiBaseContainer container4 = new GuiBaseContainer("container4", 0, 0, 100, 75, 4, 4);
//        container4.putColor(GuiColorType.BACKGROUND, new Color("a6a6a6", 0.5F));

//        GuiFlexContainer testCrossOverflow =
//                new GuiFlexContainer("testCrossOverflow", 0, 0, 200, 400, 4, 4);


        rootContainer.addChild(mainFlex);

//        rootContainer.addChild(testCrossOverflow);

//        mainContent.addChildren(mainFlex);
        mainFlex.addChildren(container1, container2/*, container3, container4*/);

        GuiScrollContainer scrollContainer = new GuiScrollContainer("testScroll", 0, 0, 50, 150, 300);
        scrollContainer.addChild(new GuiLabel("testLabel", 0, 0, 20, 20, "test"));

        mainFlex.addChild(scrollContainer);
//        GuiDropdown dropdown = new GuiDropdown("dropdown", 0, 0, 75, 20, "Select:",
//                Arrays.asList("One", "Two", "Three", "Four", "Five"));
//        mainFlex.addChild(dropdown);


//        GuiFlexContainer controlFlex = new GuiFlexContainer(rootContainer, "controlFlex", 0, 0, 400, 75, 4, 4);
//        controlFlex.putColor(GuiColorType.BACKGROUND, new Color("b49c9c", 0.5F));
//        controlFlex.setJustifyContent(FlexJustify.AROUND);
//        controlFlex.setAlignItems(FlexAlign.CENTER);
//
//        GuiFlexContainer directionFlex = new GuiFlexContainer(controlFlex, "directionFlex", 0, 0, 150, 60, 4, 4);
//        directionFlex.setDirection(FlexDirection.COLUMN);
//        directionFlex.setAlignItems(FlexAlign.CENTER);
//        directionFlex.putColor(GuiColorType.BACKGROUND, new Color("383838", 0.5F));
//        new GuiLabel(directionFlex, "directionLabel", 30, 70, 100, 22, "flex-direction");
//
//        GuiFlexContainer switchFlex = new GuiFlexContainer(directionFlex, "switchFlex", 0, 0, 100, 40, 4, 4);
//        switchFlex.setJustifyContent(FlexJustify.AROUND);
//        switchFlex.setAlignItems(FlexAlign.CENTER);
//        GuiSwitch rowSwitch = new GuiSwitch(switchFlex, "row_col", 20, 5, settings.getBoolSetting(LocalSettingKey.FLEX_DIRECTION_MAIN).getCurrentValue());
//        GuiSwitch revSwitch = new GuiSwitch(switchFlex, "reverse", 20, 5, settings.getBoolSetting(LocalSettingKey.FLEX_DIRECTION_REVERSE).getCurrentValue());
//        rowSwitch.associateSetting(settings.get(LocalSettingKey.FLEX_DIRECTION_MAIN));
//        revSwitch.associateSetting(settings.get(LocalSettingKey.FLEX_DIRECTION_REVERSE));
//
//        GuiFlexContainer basisFlex = new GuiFlexContainer(controlFlex, "basisFlex", 100, 0, 200, 90, 4, 4);
//        basisFlex.setDirection(FlexDirection.COLUMN);
//        basisFlex.setJustifyContent(FlexJustify.CENTER);
//        basisFlex.setAlignItems(FlexAlign.CENTER);
//        basisFlex.putColor(GuiColorType.BACKGROUND, new Color("121212", 0.5F));
//        new GuiLabel(basisFlex, "flex-basis-label", 30, 70, 200, 22, "flex-basis");
//        GuiSingleSlider basisSlider = new GuiSingleSlider(basisFlex, "flex-basis-slider", 5, 40, 150, 16, 0, 100,
//                settings.getIntSetting(LocalSettingKey.FLEX_BASIS).getCurrentValue(), false);
//        basisSlider.associateSetting(settings.get(LocalSettingKey.FLEX_BASIS));

        return rootContainer;
    }

    protected GuiWindow initRootContainer()
    {
//        System.out.println("settings: " + settings);

        // TODO: use this to pass setting into SettingElement constructors? `Setting<?> setting = settings.get(settingName);`
        //  if passing each setting into elements instead of later associating settings (bad), is there any need for an element to have a name anymore?
        //  maybe keep elementName(s) in case they are needed for something (forgot), but for a settingElement assign a name given the setting's name?
        //  this all implies a settingElement can only work in conjunction with a setting which should be terrible design anyway..

        //  TODO: option to pass in a setting to an element in place of a default value?

        // TODO: should this really be immutable?
//        Map<GuiColorType, Color> colorMap = ImmutableMap.of(
//                GuiColorType.ACCENT1, new Color(),
//                GuiColorType.ACCENT2, new Color("#484848"),
//                GuiColorType.BASE, new Color("#1450A0"),
//                GuiColorType.BACKGROUND, new Color("#BF242424"));

        // TODO: don't all these given widths and heights for elements depend on the this initial scaledRes?
        //  not literally but relatively, they were chosen with respect to this initial scaledRes, right?
//        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
//        int w = sr.getScaledWidth() / 2;
//        int h = sr.getScaledHeight() / 2;

//        GuiFlexContainer rootContainer = new GuiFlexContainer(null, "ROOT",
//                200, 50, 600, 400, 1, 3, colorMap);
//        rootContainer.setDirection(FlexDirection.ROW);
//        rootContainer.setJustifyType(FlexJustifyType.CENTER);

//        GuiFlexContainer flexContainer = new GuiFlexContainer(rootContainer, "MAIN_FLEX", 50, 10, 350, 200, 2, 2);
//        flexContainer.putColor(GuiColorType.BACKGROUND, new Color("#615a5a", 0.5F));
//
//        GuiFlexContainer flexContainer2 = new GuiFlexContainer(rootContainer, "MAIN_FLEX2", 50, 10, 150, 200, 2, 2);
//        flexContainer2.putColor(GuiColorType.BACKGROUND, new Color("#898989", 0.7F));
//
////        flexContainer.setJustifyType(FlexJustify.BETWEEN);
////        new GuiLabel(flexContainer, "SWITCH_LABEL", 30, 70, 80, 22, "LabelLabelLabel");
////
////        GuiBaseContainer switchContainer = new GuiBaseContainer(flexContainer, "SWITCH_FLEX", 150, 35, 60, 30, 4, 4);
//////        switchContainer.setVertical(true);
//////        switchContainer.setJustifyType(FlexJustifyType.BETWEEN);
////        new GuiSwitch(switchContainer, "DRAW_BACKGROUND", 20, 5, 20, 10, "Whether to draw background",
////                settings.getBoolSetting(LocalSettingKey.DRAW_BACKGROUND).getCurrentValue());
////        new GuiSwitch(switchContainer, "SWITCH", 10, 17, 20, 10, "", false);
//
////        new GuiSingleSlider(flexContainer, "TEST_SLIDER", 200, 120, 100, 16, 0, 100, 70, false);
//
////        GuiFlexContainer buttonContainer = new GuiFlexContainer(rootContainer, "BUTTON_CONTAINER",
////                (rootContainer.getWidth() / 2) - (300 / 2), rootContainer.getHeight() - 100, 300, 100, 1, 3);
//
////        int bw = 50;
////        int bh = 30;
////
//        GuiBaseContainer directionContainer = new GuiBaseContainer(flexContainer, "directionContainer",
//                100, 100, 200, 75, 1, 3);
////        new GuiButton(directionContainer, "setDirectionRow", 0, 0, bw, bh, "row",
////                element -> setDirectionRow(element, flexContainer));
////        new GuiButton(directionContainer, "setDirectionRowRev", 50, 0, bw, bh, "row-rev",
////                element -> setDirectionRowRev(element, flexContainer));
////        new GuiButton(directionContainer, "setDirectionColumn", 100, 0, bw, bh, "col",
////                element -> setDirectionColumn(element, flexContainer));
////        new GuiButton(directionContainer, "setDirectionColumnRev", 150, 0, bw, bh, "col-rev",
////                element -> setDirectionColumnRev(element, flexContainer));
////        directionContainer.setDirection(FlexDirection.COLUMN);
//        directionContainer.putColor(GuiColorType.BACKGROUND, new Color("#708090", 0.7F));
//
//        GuiBaseContainer justifyContainer = new GuiBaseContainer(flexContainer, "justifyContainer",
//                100, 100, 200, 75, 1, 3);
////        new GuiButton(justifyContainer, "setJustifyStart", 0, 30, bw, bh, "start",
////                element -> setJustifyStart(element, flexContainer));
////        new GuiButton(justifyContainer, "setJustifyEnd", 50, 30, bw, bh, "end",
////                element -> setJustifyEnd(element, flexContainer));
////        new GuiButton(justifyContainer, "setJustifyCenter", 100, 30, bw, bh, "center",
////                element -> setJustifyCenter(element, flexContainer));
////        new GuiButton(justifyContainer, "setJustifyBetween", 150, 30, bw, bh, "between",
////                element -> setJustifyBetween(element, flexContainer));
////        new GuiButton(justifyContainer, "setJustifyAround", 200, 30, bw, bh, "around",
////                element -> setJustifyAround(element, flexContainer));
////        new GuiButton(justifyContainer, "setJustifyEvenly", 250, 30, bw, bh, "evenly",
////                element -> setJustifyEvenly(element, flexContainer));
////        justifyContainer.setDirection(FlexDirection.ROW);
//        directionContainer.putColor(GuiColorType.BACKGROUND, new Color("#323232", 0.7F));

//
//        GuiFlexContainer alignContainer = new GuiFlexContainer(rootContainer, "alignContainer",
//                100, 100, 200, 75, 1, 3);
//        new GuiButton(alignContainer, "setAlignStart", 0, 60, bw, bh, "start",
//                element -> setAlignStart(element, rootContainer));
//        new GuiButton(alignContainer, "setAlignEnd", 50, 60, bw, bh, "end",
//                element -> setAlignEnd(element, rootContainer));
//        new GuiButton(alignContainer, "setAlignCenter", 100, 60, bw, bh, "center",
//                element -> setAlignCenter(element, rootContainer));
//        new GuiButton(alignContainer, "setAlignStretch", 150, 60, bw, bh, "stretch",
//                element -> setAlignStretch(element, rootContainer));
//        alignContainer.setDirectionType(FlexDirection.COLUMN);

//        GuiFlexContainer sliderContainer = new GuiFlexContainer(rootContainer, "SLIDER_FLEX", 20, 60, 300, 65, 2, 2);
//        sliderContainer.setVertical(true);
//        sliderContainer.setJustifyType(FlexJustifyType.EVENLY);
//
//        new GuiDualSlider(sliderContainer, "RENDER_RANGE", 5, 5, 100, 16, 0, 50, 20, 30, false);
//        new GuiSingleSlider(sliderContainer, "BACKGROUND_OPACITY", 5, 40, 100, 16, 0, 100, 20, false);

//        GuiFlexContainer buttonContainer = new GuiFlexContainer(rootContainer, "BUTTON_FLEX", 75, 120, 100, 75,
//                0, 0);
//        buttonContainer.setVertical(true);
//        buttonContainer.setJustifyType(FlexJustifyType.END);
//
//        new GuiSwitch(buttonContainer, "TEST", 5, 5, 32, 10, "", false);
//        new GuiButton(buttonContainer, "BUTTON_TEST", 40, 25, 30, 40, this::onMouseDownSpecial);

        return newRootContainerTest();
    }

    private boolean setDirectionRow(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setDirection(flexContainer, FlexDirection.ROW);
    }

    private boolean setDirectionRowRev(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setDirection(flexContainer, FlexDirection.ROW_REVERSE);
    }

    private boolean setDirectionColumn(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setDirection(flexContainer, FlexDirection.COLUMN);
    }

    private boolean setDirectionColumnRev(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setDirection(flexContainer, FlexDirection.COLUMN_REVERSE);
    }

    private boolean setDirection(GuiFlexContainer flexContainer, FlexDirection type)
    {
        flexContainer.setDirection(type);
        forceUpdate(flexContainer);
        return false;
    }

    private boolean setJustifyStart(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setJustifyContent(flexContainer, FlexJustify.START);
    }

    private boolean setJustifyEnd(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setJustifyContent(flexContainer, FlexJustify.END);
    }

    private boolean setJustifyCenter(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setJustifyContent(flexContainer, FlexJustify.CENTER);
    }

    private boolean setJustifyBetween(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setJustifyContent(flexContainer, FlexJustify.BETWEEN);
    }

    private boolean setJustifyAround(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setJustifyContent(flexContainer, FlexJustify.AROUND);
    }

    private boolean setJustifyEvenly(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setJustifyContent(flexContainer, FlexJustify.EVENLY);
    }

    private boolean setJustifyContent(GuiFlexContainer flexContainer, FlexJustify type)
    {
        flexContainer.setJustifyContent(type);
        forceUpdate(flexContainer);
        return false;
    }

    private boolean setAlignStart(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setAlignItems(flexContainer, FlexAlign.START);
    }

    private boolean setAlignEnd(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setAlignItems(flexContainer, FlexAlign.END);
    }

    private boolean setAlignCenter(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setAlignItems(flexContainer, FlexAlign.CENTER);
    }

    private boolean setAlignStretch(GuiElement element, GuiFlexContainer flexContainer)
    {
        return setAlignItems(flexContainer, FlexAlign.STRETCH);
    }

    private boolean setAlignItems(GuiFlexContainer flexContainer, FlexAlign type)
    {
        flexContainer.setAlignItems(type);
        forceUpdate(flexContainer);
        return false;
    }

    // obviously terrible, just for debugging
    private void forceUpdate(GuiFlexContainer flexContainer)
    {
        flexContainer.dispatchInitGuiEvent(new ScaledResolution(mc));
    }
}
