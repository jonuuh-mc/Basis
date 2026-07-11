package io.jonuuh.basis.lib.gui.element.toggles;

import io.jonuuh.basis.lib.util.Color;
import io.jonuuh.basis.lib.util.RenderUtils;

public class GuiSwitch extends GuiToggle
{
    protected Color pointerColor;
    protected Color enabledTrackColor;
    protected Color disabledTrackColor;

    // TODO: make vertical option
    public GuiSwitch(Builder builder)
    {
        super(builder);

        this.pointerColor = builder.pointerColor;
        this.enabledTrackColor = builder.enabledTrackColor;
        this.disabledTrackColor = builder.disabledTrackColor;
    }

    public float getPointerSize()
    {
        return getHeight() - 2F;
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }

        super.onScreenDraw(mouseX, mouseY, partialTicks);

        if (shouldDrawBackground())
        {
            RenderUtils.drawRoundedRectWithBorder(worldXPos(), worldYPos(), getWidth(), getHeight(),
                    getCornerRadius(), 1, getBackgroundColor(), getBorderColor());
        }

        float padding = (getHeight() - getPointerSize()) / 2F;
        float pointerX = isToggled() ? (worldXPos() + getWidth() - getPointerSize() - padding) : worldXPos() + padding;
        Color trackColor = isToggled() ? enabledTrackColor : disabledTrackColor;

        // Track
        RenderUtils.drawRoundedRectWithBorder(worldXPos(), worldYPos(),
                getWidth(), getHeight(),
                getCornerRadius(), 1, trackColor, getBorderColor());

        // Pointer
        RenderUtils.drawRoundedRectWithBorder(pointerX, worldYPos() + padding,
                getPointerSize(), getPointerSize(),
                getCornerRadius(), 1, pointerColor, getBorderColor());
    }

    public static class Builder extends GuiToggle.AbstractBuilder<Builder, GuiSwitch>
    {
        protected Color pointerColor = Color.WHITE;
        protected Color enabledTrackColor = Color.DARK_GREEN;
        protected Color disabledTrackColor = Color.DARK_GRAY;

        public Builder(String elementName)
        {
            super(elementName);
            this.width = DEFAULT_HEIGHT * 2;
            this.height = DEFAULT_HEIGHT;
            // Override GuiElement's default of drawing the background
            drawBackground(false);
        }

        public Builder pointerColor(Color color)
        {
            this.pointerColor = color;
            return self();
        }

        public Builder enabledTrackColor(Color color)
        {
            this.enabledTrackColor = color;
            return self();
        }

        public Builder disabledTrackColor(Color color)
        {
            this.disabledTrackColor = color;
            return self();
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiSwitch build()
        {
            return new GuiSwitch(this);
        }
    }
}

