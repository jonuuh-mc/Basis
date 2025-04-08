package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.element.GuiButton;
import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.element.container.flex.FlexAlign;
import io.jonuuh.core.lib.gui.element.container.flex.FlexDirection;
import io.jonuuh.core.lib.gui.element.container.flex.FlexItemProperties;
import io.jonuuh.core.lib.gui.element.container.flex.GuiFlexContainer;
import io.jonuuh.core.lib.gui.event.GuiEventBehavior;

import java.util.Collection;

// TODO: save me
public class GuiDropdown extends GuiFlexContainer
{
    protected GuiEventBehavior eventBehavior;
    protected GuiFlexContainer dropdownContainer;
    protected GuiButton header;
    protected float closedHeight;

    public GuiDropdown(String elementName, int xPos, int yPos, int width, int height, String prompt, Collection<String> options)
    {
        super(elementName, xPos, yPos, width, height * (options.size() + 1));
        this.closedHeight = height;
        this.setHeight(closedHeight);
        this.getDimensions().maxHeight = height;
//        setHeight(openHeight);

        this.setDirection(FlexDirection.COLUMN);
        this.setAlignItems(FlexAlign.STRETCH);

        GuiFlexContainer headerContainer = new GuiFlexContainer(elementName + "$header-container",
                0, 0, 1, height, 0, 0);

        // TODO: needs width > 0 for grow to work, fix?
        this.header = new GuiButton(elementName + "$header", 0, 0, 1, 1, prompt, eventBehavior);
        headerContainer.addChild(header, new FlexItemProperties().setGrow(1));
        headerContainer.setAlignItems(FlexAlign.STRETCH);
        super.addChild(headerContainer);

        GuiFlexContainer dropdownContainer = new GuiFlexContainer(elementName + "$dropdown-container",
                0, 0, 1, 1, 0, 0);
        dropdownContainer.setDirection(FlexDirection.COLUMN);
        dropdownContainer.setAlignItems(FlexAlign.STRETCH);

        for (String option : options)
        {
            dropdownContainer.addChild(createOption(option), new FlexItemProperties().setGrow(1));
        }
        dropdownContainer.setVisible(false);
        this.dropdownContainer = dropdownContainer;

        super.addChild(dropdownContainer, new FlexItemProperties().setGrow(1));
    }

    protected GuiButton createOption(String buttonLabel)
    {
        return new GuiButton(buttonLabel, 0, 0, 1, 1, buttonLabel, this::setHeaderText);
    }

    protected boolean setHeaderText(GuiElement element)
    {
        header.setButtonLabel(element.elementName);
        toggleOpen(null);
        return false;
    }

    protected boolean toggleOpen(GuiElement element)
    {
        dropdownContainer.setVisible(!this.dropdownContainer.isVisible());
        setHeight(dropdownContainer.isVisible() ? openHeight : getMaxHeight());
        return false;
    }

//    @Override
//    public void addChild(GuiElement child, FlexItemProperties flexProperties)
//    {
//    }
//
//    @Override
//    public void addChild(GuiElement child)
//    {
//    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        super.onScreenDraw(mouseX, mouseY, partialTicks);

//        float size = getWidth() / 3F;
//        RenderUtils.drawTriangle(GL11.GL_POLYGON, worldXPos() + getWidth() - size, worldYPos() + getHeight() - size,
//                size, size, getColor(GuiColorType.ACCENT1), 0);

//        if (isOpen)
//        {
//
//        }
    }
}
