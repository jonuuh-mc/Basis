package io.jonuuh.basis.lib.gui.element.toggles;

import io.jonuuh.basis.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.lib.util.RenderUtils;

public class GuiCheckbox extends GuiToggle
{
    public GuiCheckbox(Builder builder)
    {
        super(builder);
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
            RenderUtils.drawRoundedRectWithBorder(getInnerLeftBound(), getInnerTopBound(),
                    getInnerWidth(), getInnerHeight(), getCornerRadius(), 1, getColor(GuiColorType.BASE), getBorderColor());
        }
    }

    public static class Builder extends GuiToggle.AbstractBuilder<Builder, GuiCheckbox>
    {
        public Builder(String elementName)
        {
            super(elementName);
            this.width = this.height = DEFAULT_HEIGHT;
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
