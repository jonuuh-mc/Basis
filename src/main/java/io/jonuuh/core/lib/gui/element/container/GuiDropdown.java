package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.element.GuiButton;
import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.element.container.flex.FlexItem;
import io.jonuuh.core.lib.gui.element.container.flex.GuiFlexContainer;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexDirection;

import java.util.ArrayList;
import java.util.Collection;

public class GuiDropdown extends GuiFlexContainer
{
    protected GuiFlexContainer optionsContainer;
    protected GuiButton header;

    public GuiDropdown(Builder builder)
    {
        super(builder);
        this.enabled = builder.enabled;
        this.setDirection(FlexDirection.COLUMN);

        this.header = new GuiButton.Builder(this.elementName + "$header")
                .size(this.getWidth(), this.getHeight())
                .buttonText(builder.prompt)
                .build();
        addItem(new FlexItem(header/*, 0, width, height, height*/).setShrink(0));

        this.optionsContainer = new GuiFlexContainer.Builder(elementName + "$option-container")
                .localPosition(0, getHeight())
                .size(getWidth(), getHeight() * builder.options.size())
                .visible(false)
                .direction(FlexDirection.COLUMN)
                .build();

        for (String option : builder.options)
        {
            GuiButton button = new GuiButton.Builder(option)
                    .size(this.getWidth(), this.getHeight())
                    .visible(false)
                    .buttonText(option)
                    .build();
            optionsContainer.addItem(new FlexItem(button));
        }
//        optionsContainer.setVisible(false);

        addItem(new FlexItem(optionsContainer).setShrink(0));
    }

    public String getHeaderText()
    {
        return header.getButtonLabel();
    }

    public void setHeaderText(String headerText)
    {
        header.setButtonLabel(headerText);
    }

    protected void toggleOpen(GuiElement element)
    {
//        if (handlePreCustomEvent())
//        {
        header.setButtonLabel(((GuiButton) element).getButtonLabel());
        dropdownContainer.setVisible(!dropdownContainer.isVisible());
//        }
//        handlePostCustomEvent();
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);
//        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), getWidth(), getHeight(), innerRadius, getColor(GuiColorType.BACKGROUND), true);

//        float size = getWidth() / 3F;
//        RenderUtils.drawTriangle(GL11.GL_POLYGON, worldXPos() + getWidth() - size, worldYPos() + getHeight() - size,
//                size, size, getColor(GuiColorType.ACCENT1), 0)
    }

    public static class Builder extends GuiFlexContainer.AbstractBuilder<Builder, GuiDropdown>
    {
        protected String prompt = "Dropdown";
        protected Collection<String> options = new ArrayList<>();
        protected boolean enabled = true;

        public Builder(String elementName)
        {
            super(elementName);
        }

        public Builder prompt(String prompt)
        {
            this.prompt = prompt;
            return self();
        }

        public Builder options(Collection<String> options)
        {
            this.options = options;
            return self();
        }

        public Builder enabled(boolean enabled)
        {
            this.enabled = enabled;
            return self();
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiDropdown build()
        {
            return new GuiDropdown(this);
        }
    }
}
