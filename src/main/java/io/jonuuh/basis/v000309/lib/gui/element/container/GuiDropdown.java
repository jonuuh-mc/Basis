package io.jonuuh.basis.v000309.lib.gui.element.container;

import io.jonuuh.basis.v000309.lib.gui.element.GuiElement;
import io.jonuuh.basis.v000309.lib.gui.element.GuiLabel;
import io.jonuuh.basis.v000309.lib.gui.element.button.GuiLabeledButton;
import io.jonuuh.basis.v000309.lib.gui.element.container.behavior.FlexBehavior;
import io.jonuuh.basis.v000309.lib.gui.event.GuiEvent;
import io.jonuuh.basis.v000309.lib.gui.event.PostEventBehaviorHost;
import io.jonuuh.basis.v000309.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.basis.v000309.lib.gui.event.lifecycle.CloseGuiEvent;
import io.jonuuh.basis.v000309.lib.gui.listener.input.MouseClickListener;
import io.jonuuh.basis.v000309.lib.gui.listener.lifecycle.CloseGuiListener;
import io.jonuuh.basis.v000309.lib.gui.properties.FlexDirection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GuiDropdown extends GuiContainer implements MouseClickListener, CloseGuiListener, PostEventBehaviorHost
{
    private final Map<Class<? extends GuiEvent>, Consumer<GuiElement>> postBehaviors;
    protected GuiContainer optionsContainer;
    // TODO: make header a generic GuiElement instead? support guiTexturedButtons for example? maybe just make it a GuiButton type
    protected GuiLabeledButton header;
    private boolean enabled;
    private boolean mouseDown;

    // TODO: just make this one container instead of nested containers and dynamically add/remove children or make them visible/invisible?
    //  also should make flex alg account for invisible elements? add boolean in flexcontainer whether to ignore invisibles?
    public GuiDropdown(Builder builder)
    {
        super(builder);

        this.shouldScissor = false;

        this.enabled = builder.enabled;
        this.header = builder.header;
        this.optionsContainer = builder.optionsContainer;

        this.postBehaviors = new HashMap<>();
        if (builder.mouseDownBehavior != null)
        {
            assignPostEventBehavior(MouseDownEvent.class, builder.mouseDownBehavior);
        }
    }

    public String getHeaderText()
    {
        return header.getLabel();
    }

    public void setHeaderText(String headerText)
    {
        header.setLabel(headerText);
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public boolean isMouseDown()
    {
        return mouseDown;
    }

    @Override
    public void setMouseDown(boolean mouseDown)
    {
        this.mouseDown = mouseDown;
    }

    @Override
    public Map<Class<? extends GuiEvent>, Consumer<GuiElement>> getPostEventBehaviors()
    {
        return postBehaviors;
    }

    @Override
    public void onMouseDown(MouseDownEvent event)
    {
        super.onMouseDown(event);
        // TODO: class cast exception if target is ever something other than a GuiButton

        if (event.target instanceof GuiLabel)
        {
            header.setLabel(((GuiLabel) event.target).getText());
        }
        optionsContainer.setVisible(!optionsContainer.isVisible());
        tryApplyPostEventBehavior(event.getClass());

        // Prevent the event from traveling to the clicked option GuiButton within the container
        event.stopPropagation();
    }

    @Override
    public void onCloseGui(CloseGuiEvent event)
    {
//        optionsContainer.setVisible(!optionsContainer.isVisible());
        optionsContainer.setVisible(false);
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

    public static class Builder extends GuiContainer.AbstractBuilder<Builder, GuiDropdown>
    {
        protected Consumer<GuiElement> mouseDownBehavior = null;
        protected String prompt = "Dropdown";
        protected Collection<String> options = new ArrayList<>();
        protected boolean enabled = true;
        protected GuiContainer optionsContainer;
        protected GuiLabeledButton header;

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

        public Builder mouseDownBehavior(Consumer<GuiElement> mouseDownBehavior)
        {
            this.mouseDownBehavior = mouseDownBehavior;
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
            this.header = new GuiLabeledButton.Builder(this.elementName + "$header")
                    .size(width, height)
                    .label(prompt)
                    .build();

            List<FlexItem> optionLabels = new ArrayList<>();
            for (String option : options)
            {
                GuiLabel optionLabel = new GuiLabel.Builder(option)
                        .size(width, height)
                        .visible(false)
                        .text(option)
                        .build();
                optionLabels.add(new FlexItem(optionLabel));
            }

            this.optionsContainer = new GuiBasicContainer.Builder(elementName + "$option-container")
                    .localPosition(0, height)
                    .size(width, height * options.size())
                    .visible(false)
                    .flexBehavior(new FlexBehavior.Builder().direction(FlexDirection.COLUMN).items(optionLabels))
                    .build();

            this.flexBehavior(new FlexBehavior.Builder().direction(FlexDirection.COLUMN));

            flexBehaviorBuilder.item(new FlexItem(header/*, 0, width, height, height*/).setShrink(0));
            flexBehaviorBuilder.item(new FlexItem(optionsContainer).setShrink(0));

            return new GuiDropdown(this);
        }
    }
}
