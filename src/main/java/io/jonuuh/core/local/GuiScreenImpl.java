package io.jonuuh.core.local;

import com.google.common.collect.ImmutableMap;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.gui.AbstractGuiScreen;
import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.GuiEventType;
import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.element.container.GuiBaseContainer;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import io.jonuuh.core.lib.gui.element.container.GuiDropdown;
import io.jonuuh.core.lib.gui.element.container.GuiRootContainer;
import io.jonuuh.core.lib.gui.element.container.flex.FlexAlign;
import io.jonuuh.core.lib.gui.element.container.flex.FlexDirection;
import io.jonuuh.core.lib.gui.element.container.flex.FlexItem;
import io.jonuuh.core.lib.gui.element.container.flex.FlexJustify;
import io.jonuuh.core.lib.gui.element.container.flex.GuiFlexContainer;
import io.jonuuh.core.lib.gui.element.sliders.GuiSingleSlider;
import io.jonuuh.core.lib.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Arrays;
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
        Map<GuiColorType, Color> colorMap = ImmutableMap.of(
                GuiColorType.ACCENT1, new Color(),
                GuiColorType.ACCENT2, new Color("#BF484848"),
                GuiColorType.BASE, new Color("#BF1450A0"),
                GuiColorType.BACKGROUND, new Color("#BF242424"));

        GuiRootContainer rootContainer = new GuiRootContainer(this, new ScaledResolution(Minecraft.getMinecraft()),
                0, colorMap);

        GuiFlexContainer mainContent = new GuiFlexContainer("mainContent", 0, 0, 600, 400);
        mainContent.setDirection(FlexDirection.COLUMN);
        mainContent.setJustifyContent(FlexJustify.AROUND);
        mainContent.setAlignItems(FlexAlign.CENTER);

//        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        GuiFlexContainer mainFlex = new GuiFlexContainer("mainFlex", 0, 0, 450, 270);
        mainFlex.putColor(GuiColorType.BACKGROUND, new Color("4d4d4d", 0.75F));
        this.mainFlex = mainFlex;

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        GuiBaseContainer container1 = new GuiBaseContainer("container1", 0, 0, 125, 40);
        container1.putColor(GuiColorType.BACKGROUND, new Color("#BF1450A0"));
        container1.assignCustomPostEventBehavior(GuiEventType.MOUSE_DOWN, this::updateCurrFocusedContainer);

        GuiBaseContainer container2 = new GuiBaseContainer("container2", 0, 0, 50, 100);
        container2.putColor(GuiColorType.BACKGROUND, new Color("a6a6a6", 0.5F));
        container2.assignCustomPostEventBehavior(GuiEventType.MOUSE_DOWN, this::updateCurrFocusedContainer);

        GuiBaseContainer container3 = new GuiBaseContainer("container3", 0, 0, 100, 30);
        container3.putColor(GuiColorType.BACKGROUND, new Color("a6a6a6", 0.5F));
        container3.assignCustomPostEventBehavior(GuiEventType.MOUSE_DOWN, this::updateCurrFocusedContainer);

        GuiBaseContainer container4 = new GuiBaseContainer("container4", 0, 0, 75, 50);
        container4.putColor(GuiColorType.BACKGROUND, new Color("a6a6a6", 0.5F));
        container4.assignCustomPostEventBehavior(GuiEventType.MOUSE_DOWN, this::updateCurrFocusedContainer);

        currFocusedContainer = container1;
        this.mainFlex.addItems(new FlexItem(container1), new FlexItem(container2), new FlexItem(container3), new FlexItem(container4));

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        GuiFlexContainer controlFlex = new GuiFlexContainer("controlFlex", 0, 0, 400, 60);
        controlFlex.setJustifyContent(FlexJustify.AROUND);
        controlFlex.setAlignItems(FlexAlign.CENTER);

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        GuiDropdown directionDropdown = new GuiDropdown("directionDropdown", 0, 0, 75, 15, "ROW",
                Arrays.asList("ROW", "COLUMN", "ROW_REVERSE", "COLUMN_REVERSE"));
        directionDropdown.assignCustomPostEventBehavior(GuiEventType.SCREEN_TICK,
                element -> forceChangeFlexDirection((GuiDropdown) element, this.mainFlex));

        GuiDropdown justifyDropdown = new GuiDropdown("justifyDropdown", 0, 0, 75, 15, "START",
                Arrays.asList("START", "END", "CENTER", "BETWEEN", "AROUND", "EVENLY"));
        justifyDropdown.assignCustomPostEventBehavior(GuiEventType.SCREEN_TICK,
                element -> forceChangeFlexJustify((GuiDropdown) element, this.mainFlex));

        GuiDropdown alignDropdown = new GuiDropdown("alignDropdown", 0, 0, 75, 15, "START",
                Arrays.asList("START", "END", "CENTER", "STRETCH"));
        alignDropdown.assignCustomPostEventBehavior(GuiEventType.SCREEN_TICK,
                element -> forceChangeFlexAlign((GuiDropdown) element, this.mainFlex));

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        GuiFlexContainer itemControlFlex = new GuiFlexContainer("itemControlFlex", 0, 0, 125, 50);
        itemControlFlex.setDirection(FlexDirection.COLUMN);
        itemControlFlex.setJustifyContent(FlexJustify.AROUND);
        itemControlFlex.setAlignItems(FlexAlign.CENTER);

        GuiSingleSlider growItemSlider = new GuiSingleSlider("growSlider", 0, 0, 100, 10, 0, 10, 0, false, true);
        GuiSingleSlider shrinkItemSlider = new GuiSingleSlider("shrinkSlider", 0, 0, 100, 10, 0, 10, 1, false, true);
        GuiDropdown alignItemDropdown = new GuiDropdown("alignDropdown", 0, 0, 75, 15, "START",
                Arrays.asList("START", "END", "CENTER", "STRETCH"));

        growItemSlider.assignCustomPostEventBehavior(GuiEventType.MOUSE_DRAG,
                element -> mainFlex.getFlexItem(currFocusedContainer).setGrow(((GuiSingleSlider) element).getValueInt()));
        shrinkItemSlider.assignCustomPostEventBehavior(GuiEventType.MOUSE_DRAG,
                element -> mainFlex.getFlexItem(currFocusedContainer).setShrink(((GuiSingleSlider) element).getValueInt()));
        alignItemDropdown.assignCustomPostEventBehavior(GuiEventType.SCREEN_TICK,
                element -> mainFlex.getFlexItem(currFocusedContainer).setAlign(FlexAlign.valueOf(alignItemDropdown.getHeaderText())));

        this.growItemSlider = growItemSlider;
        this.shrinkItemSlider = shrinkItemSlider;
        this.alignItemDropdown = alignItemDropdown;
        itemControlFlex.addItems(new FlexItem(growItemSlider), new FlexItem(shrinkItemSlider), new FlexItem(alignItemDropdown));

        // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
        controlFlex.addItems(new FlexItem(directionDropdown), new FlexItem(justifyDropdown), new FlexItem(alignDropdown), new FlexItem(itemControlFlex));

        mainContent.addItems(new FlexItem(mainFlex)/*.setGrow(1)*/, new FlexItem(controlFlex));

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

    private void forceChangeFlexDirection(GuiDropdown dropdown, GuiFlexContainer flexContainer)
    {
        flexContainer.setDirection(FlexDirection.valueOf(dropdown.getHeaderText()));
        flexContainer.updateItemsLayout();
    }

    private void forceChangeFlexJustify(GuiDropdown dropdown, GuiFlexContainer flexContainer)
    {
        flexContainer.setJustifyContent(FlexJustify.valueOf(dropdown.getHeaderText()));
        flexContainer.updateItemsLayout();
    }

    private void forceChangeFlexAlign(GuiDropdown dropdown, GuiFlexContainer flexContainer)
    {
        flexContainer.setAlignItems(FlexAlign.valueOf(dropdown.getHeaderText()));
        flexContainer.updateItemsLayout();
    }
}
