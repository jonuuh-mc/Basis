package io.jonuuh.basis.lib.gui.element.toggles;

import io.jonuuh.basis.lib.util.Color;
import io.jonuuh.basis.lib.util.RenderUtils;

public class GuiCheckbox extends GuiToggle
{
    protected Color pointerColor;

    public GuiCheckbox(Builder builder)
    {
        super(builder);
        this.pointerColor = builder.pointerColor;
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

        if (isToggled())
        {
            RenderUtils.drawRoundedRectWithBorder(getInnerLeftBound(), getInnerTopBound(), getInnerWidth(), getInnerHeight(),
                    getCornerRadius(), 1, pointerColor, getBorderColor());
        }
    }

    public static class Builder extends GuiToggle.AbstractBuilder<Builder, GuiCheckbox>
    {
        protected Color pointerColor = Color.DARK_GREEN;

        public Builder(String elementName)
        {
            super(elementName);
            this.width = this.height = DEFAULT_HEIGHT;
        }

        public Builder pointerColor(Color color)
        {
            this.pointerColor = color;
            return self();
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiCheckbox build()
        {
            return new GuiCheckbox(this);
        }
    }
}
