package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.element.button.GuiLabeledButton;
import io.jonuuh.core.lib.gui.element.container.flex.FlexItem;
import io.jonuuh.core.lib.gui.element.container.flex.GuiFlexContainer;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexDirection;

import java.util.ArrayList;
import java.util.Collection;

public class GuiDropdown extends GuiFlexContainer
{
    protected GuiFlexContainer optionsContainer;
    protected GuiLabeledButton header;

    // TODO: just make this one container instead of nested containers and dynamically add/remove children or make them visible/invisible?
    //  also should make flex alg account for invisible elements? add boolean in flexcontainer whether to ignore invisibles?
    public GuiDropdown(Builder builder)
    {
        super(builder);
        this.enabled = builder.enabled;
        this.setDirection(FlexDirection.COLUMN);

        this.header = new GuiLabeledButton.Builder(this.elementName + "$header")
                .size(this.getWidth(), this.getHeight())
                .label(builder.prompt)
                .build();
        addItem(new FlexItem(header/*, 0, width, height, height*/).setShrink(0));

        // TODO: about .visible(false): would probably need to both make GuiElement constructor call the overridden setVisible()
        //  as well as add children before this in order for this to automatically propagate to child buttons?
        //  - on second thought this should just never work since GuiElement constructor will ALWAYS be called before GuiContainer constructor,
        //    and there is rightfully no way to add children to a GuiContainer before calling GuiElement constructor
        //  - instead just call .visible(false) here and when creating each button down below
        this.optionsContainer = new GuiFlexContainer.Builder(elementName + "$option-container")
                .localPosition(0, getHeight())
                .size(getWidth(), getHeight() * builder.options.size())
                .visible(false)
                .direction(FlexDirection.COLUMN)
                .build();

        for (String option : builder.options)
        {
            GuiLabeledButton button = new GuiLabeledButton.Builder(option)
                    .size(this.getWidth(), this.getHeight())
                    .visible(false)
                    .label(option)
                    .build();
            optionsContainer.addItem(new FlexItem(button));
        }
//        optionsContainer.setVisible(false);

        addItem(new FlexItem(optionsContainer).setShrink(0));
    }

    public String getHeaderText()
    {
        return header.getLabel();
    }

    public void setHeaderText(String headerText)
    {
        header.setLabel(headerText);
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
