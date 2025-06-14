package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.element.GuiButton;
import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.element.container.flex.FlexItem;
import io.jonuuh.core.lib.gui.element.container.flex.GuiFlexContainer;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexDirection;

import java.util.Collection;

public class GuiDropdown extends GuiFlexContainer
{
    protected GuiFlexContainer dropdownContainer;
    protected GuiButton header;

    public GuiDropdown(String elementName, int xPos, int yPos, int width, int height, String prompt, Collection<String> options)
    {
        super(elementName, xPos, yPos, width, height);
        this.setDirection(FlexDirection.COLUMN);

        this.header = new GuiButton(elementName + "$header", 0, 0, width, height, prompt, this::toggleOpen);
        addItem(new FlexItem(header/*, 0, width, height, height*/).setShrink(0));

        this.dropdownContainer = new GuiFlexContainer(elementName + "$option-container",
                0, height, width, height * options.size()).setDirection(FlexDirection.COLUMN);
        for (String option : options)
        {
            dropdownContainer.addItem(new FlexItem(new GuiButton(option, 0, 0, width, height, option, this::toggleOpen)));
        }
        dropdownContainer.setVisible(false);

        addItem(new FlexItem(dropdownContainer).setShrink(0));
    }

    public String getHeaderText()
    {
        return header.getButtonLabel();
    }

    public void setHeaderText(String headerText)
    {
        header.setButtonLabel(headerText);
    }

//    protected void setHeaderText(GuiElement element)
//    {
//        header.setButtonLabel(element.elementName);
//        toggleOpen(null);
//    }

    protected void toggleOpen(GuiElement element)
    {
        header.setButtonLabel(((GuiButton) element).getButtonLabel());
        dropdownContainer.setVisible(!dropdownContainer.isVisible());
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        super.onScreenDraw(mouseX, mouseY, partialTicks);
//        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), getWidth(), getHeight(), innerRadius, getColor(GuiColorType.BACKGROUND), true);

//        float size = getWidth() / 3F;
//        RenderUtils.drawTriangle(GL11.GL_POLYGON, worldXPos() + getWidth() - size, worldYPos() + getHeight() - size,
//                size, size, getColor(GuiColorType.ACCENT1), 0);

//        if (isOpen)
//        {
//
//        }
    }
}
