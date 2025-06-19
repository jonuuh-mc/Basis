package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.event.GuiEvent;
import io.jonuuh.core.lib.gui.event.PostEventBehaviorHost;
import io.jonuuh.core.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.core.lib.gui.listener.input.MouseClickListener;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.RenderUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GuiButton extends GuiElement implements MouseClickListener, PostEventBehaviorHost
{
    private final Map<Class<? extends GuiEvent>, Consumer<GuiElement>> postBehaviors;
    private boolean enabled;
    private boolean mouseDown;
    private String buttonLabel;

    public GuiButton(Builder builder)
    {
        super(builder);
        this.enabled = builder.enabled;
        this.buttonLabel = builder.buttonText;
        this.postBehaviors = new HashMap<>();

        if (builder.mouseDownBehavior != null)
        {
            assignPostEventBehavior(MouseDownEvent.class, builder.mouseDownBehavior);
        }
    }

    public String getButtonLabel()
    {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel)
    {
        this.buttonLabel = buttonLabel;
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
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        RenderUtils.drawRectangle(worldXPos(), worldYPos(), getWidth(), getHeight(), getColor(GuiColorType.BASE));

        String buttonText = RenderUtils.trimStringToWidthWithEllipsis(buttonLabel, (int) getWidth());

        mc.fontRendererObj.drawString(buttonText, worldXPos() + (getWidth() / 2) - ((float) mc.fontRendererObj.getStringWidth(buttonText) / 2),
                worldYPos() + (getHeight() / 2) - ((float) mc.fontRendererObj.FONT_HEIGHT / 2), getColor(GuiColorType.ACCENT1).toPackedARGB(), true);
    }

    @Override
    public void onMouseDown(MouseDownEvent event)
    {
        MouseClickListener.super.onMouseDown(event);
        tryApplyPostEventBehavior(event.getClass());
    }

    public static class Builder extends GuiElement.AbstractBuilder<Builder, GuiButton>
    {
        protected String buttonText = "";
        protected Consumer<GuiElement> mouseDownBehavior = null;
        protected boolean enabled = true;

        public Builder(String elementName)
        {
            super(elementName);
        }

        public Builder buttonText(String buttonText)
        {
            this.buttonText = buttonText;
            return self();
        }

        public Builder mouseDownBehavior(Consumer<GuiElement> mouseDownBehavior)
        {
            this.mouseDownBehavior = mouseDownBehavior;
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
        public GuiButton build()
        {
            return new GuiButton(this);
        }
    }
}
